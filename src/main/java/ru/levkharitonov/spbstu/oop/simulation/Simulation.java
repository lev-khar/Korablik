package ru.levkharitonov.spbstu.oop.simulation;

import ru.levkharitonov.spbstu.oop.model.CargoType;
import ru.levkharitonov.spbstu.oop.model.Report;
import ru.levkharitonov.spbstu.oop.model.Ship;
import ru.levkharitonov.spbstu.oop.JSONUtility.JsonReader;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static ru.levkharitonov.spbstu.oop.Utility.*;

public class Simulation {
    final private static java.util.Random rand = new java.util.Random();
    final private File file;
    private Map<CargoType, ConcurrentLinkedQueue<Ship>> queues;
    private List<UnloadingEvent> unloads;
    private int threads = 0;

    public Simulation(File file) {
        if (!file.exists()) this.queues = null;
        this.file = file;
        try {
            this.queues = JsonReader.readSchedule(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void simulate() {
        try {
            this.queues = JsonReader.readSchedule(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<CargoType, Integer> craneCount = new HashMap<>();
        craneCount.put(CargoType.DRY, 1);
        craneCount.put(CargoType.LIQUID, 1);
        craneCount.put(CargoType.CONTAINER, 1);

        boolean modified;
        do {
            try {
                this.queues = JsonReader.readSchedule(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            infuseChaos(); //TODO разобраться в каком месте это делать
            Map<CargoType, Long> fines = runSimulation(craneCount, queues);
            System.out.println(craneCount);
            modified = false;
            for (CargoType ct: fines.keySet()) {
                if(fines.get(ct) >= CRANE_COST) {
                    craneCount.replace(ct, craneCount.get(ct) + 1);
                    modified = true;
                }
            }
        } while (modified);

    }

    private Map<CargoType, Long> runSimulation(Map<CargoType, Integer> craneCount, Map<CargoType, ConcurrentLinkedQueue<Ship>> workQueues) {

        List<Crane> cranes = new LinkedList<>();
        threads = 0;
        for (CargoType ct: workQueues.keySet()) {
            for(int i = 0; i < craneCount.get(ct); i++) {
                Crane crane = new Crane(workQueues.get(ct), craneCount.get(ct));
                cranes.add(crane);
                threads++;
            }
        }

        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        try {
            unloads = new LinkedList<>();
            List<Future<List<UnloadingEvent>>> results = executorService.invokeAll(cranes);
            for (Future<List<UnloadingEvent>> f: results) {
                unloads.addAll(f.get().stream().filter(Objects::nonNull).collect(Collectors.toList()));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        Map<CargoType, Long> delayFines = new HashMap<>();
        delayFines.put(CargoType.DRY, 0L);
        delayFines.put(CargoType.LIQUID, 0L);
        delayFines.put(CargoType.CONTAINER, 0L);
        for (UnloadingEvent unload: unloads) {
            delayFines.compute(unload.getCargo().getType(), (k, v) -> v + (unload.getDelay()/ UNIX_HOUR * FINE_HOUR));
        }
        return delayFines;
    }

    public Report formReport() {
        long avgDelay = 0;
        long allDelay = 0;
        long maxDelay = 0;
        for (UnloadingEvent unload: unloads) {
            maxDelay = Math.max(maxDelay, unload.getDelay());
            avgDelay += unload.getDelay();
            allDelay += unload.getDelay();
        }
        avgDelay /= unloads.size();
        return new Report(this.unloads, avgDelay, allDelay, maxDelay, this.threads);
    }

    Consumer<Ship> changeArrival = ship ->
            ship.setArrival(ship.getArrival().getTime() +
                    rand.nextInt(UNIX_DAY * 14) - (UNIX_DAY * 7));

    Consumer<Ship> changeUnloading = ship ->
            ship.setUnloadingMins(ship.getUnloadingMins() + rand.nextInt(1440));

    public void infuseChaos() {
        for(CargoType ct: CargoType.values()) {
            queues.get(ct).forEach(changeArrival);
            queues.get(ct).forEach(changeUnloading);
        }
    }

}

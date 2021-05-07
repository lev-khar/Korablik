package ru.levkharitonov.spbstu.oop;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Simulation {
    final private static java.util.Random rand = new java.util.Random();
    final private static long CRANE_COST = 30000;
    final private static int FINE_HOUR = 100;
    final private static int UNIX_DAY = 86400000;
    final private static long UNIX_MINS = 60000;
    final private static long UNIX_HOUR = 3600000;
    final private static Date DATE_START = new Date(1617235199000L);
    final private static Date DATE_END = new Date(1619827200000L);
    private Map<CargoType, ConcurrentLinkedQueue<Ship>> queues;
    private List<UnloadingEvent> unloads;
    private int threads = 0;

    public Simulation() {
        try {
            this.queues = JsonReader.readSchedule();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void simulate() {
        try {
            this.queues = JsonReader.readSchedule();
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
                this.queues = JsonReader.readSchedule();
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

    private void printUnloadingEvents() {
        unloads.forEach(System.out::println);
    }

    public void printReport() {
        printUnloadingEvents();
        long avgDelay = 0;
        long allDelay = 0;
        long maxDelay = 0;
        for (UnloadingEvent unload: unloads) {
            maxDelay = Math.max(maxDelay, unload.getDelay());
            avgDelay += unload.getDelay();
            allDelay += unload.getDelay();
        }
        avgDelay /= unloads.size();
        System.out.println("\nShips unloaded: " + unloads.size());
        System.out.println("Max delay: " + formatTime(maxDelay));
        System.out.println("Average delay: " + formatTime(avgDelay));
        System.out.println("Fines for all ships: " + allDelay / UNIX_HOUR * FINE_HOUR);
        System.out.println("Cranes needed: " + threads);

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
    private String formatTime(long time) {
        return (time / 1000) / 86400 + ":" + (time / 1000) % 86400 / 3600 + ":" + (time / 1000) % 3600 / 60;
    }
}

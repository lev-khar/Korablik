package ru.levkharitonov.spbstu.oop;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Crane implements Callable<List<UnloadingEvent>> {
    final private static long UNIX_MINS = 60000;
    private ConcurrentLinkedQueue<Ship> ships;
    private List<UnloadingEvent> unloads;
    final private int numberOfCranes;
    private long busy = 0;

    public Crane(ConcurrentLinkedQueue<Ship> ships, int numberOfCranes) {
        this.ships = ships;
        this.numberOfCranes = numberOfCranes;
    }

    @Override
    public List<UnloadingEvent> call() throws Exception {
        unloads = new LinkedList<>();

        while (!ships.isEmpty()) {
            Ship ship = ships.poll();
            if (ship == null) {
                continue;
            }
            long arrival = ship.getArrival().getTime();
            long start = Math.max(busy, arrival);
            long delay = start - arrival;
            long duration = countUnloading(ship) * UNIX_MINS;
            long end = start + duration;
            busy = end;
            UnloadingEvent unload = new UnloadingEvent(ship.getName(), ship.getCargo(), arrival, start, end, delay);
            unloads.add(unload);
            Thread.sleep(20);
        }
        return unloads;
    }

    private long countUnloading(Ship ship) {
        if (numberOfCranes <= 1) {
            return ship.getUnloadingMins();
        }
        int count = 0;
        UnloadingEvent working = null;
        for (UnloadingEvent unload: unloads) {
            if (unload.getShipName().equals(ship.getName()) || (unload.getArrival() == ship.getArrival().getTime())) {
                if (count >= 2) break;
                working = unload;
                count++;
            }
        }
        if (count >= 2) {
            return ship.getUnloadingMins(); // cannot add more cranes
        }
        if (working != null) {
            int index = unloads.indexOf(working);
            working.setEnd(working.getEnd() / 2);
            try {
                unloads.set(index, working);
            } catch (IndexOutOfBoundsException e) {
                return ship.getUnloadingMins();
            }
            ships.add(ship);
            return ship.getUnloadingMins() / 2;
        }
        return ship.getUnloadingMins();
    }
}

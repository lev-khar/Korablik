package ru.levkharitonov.spbstu.oop;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Crane implements Callable<List<UnloadingEvent>> {
    final private static long UNIX_MINS = 60000;
    private ConcurrentLinkedQueue<Ship> ships;
    private long busy = 0;

    public void setShips(ConcurrentLinkedQueue<Ship> ships) {
        this.ships = ships;
    }

    @Override
    public List<UnloadingEvent> call() throws Exception {
        final List<UnloadingEvent> unloads = new LinkedList<>();

        while (!ships.isEmpty()) {
            Ship ship = ships.poll();
            if (ship == null) {
                continue;
            }
            long arrival = ship.getArrival().getTime();
            long start = Math.max(busy, arrival);
            long delay = start - arrival;
            long duration = ship.getUnloadingMins() * UNIX_MINS;
            long end = start + duration;
            busy = end;
            UnloadingEvent unload = new UnloadingEvent(ship.getName(), ship.getCargo(), arrival, start, end, delay);
            unloads.add(unload);
            Thread.sleep(5);
        }
        return unloads;
    }
}

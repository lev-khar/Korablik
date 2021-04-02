package ru.levkharitonov.spbstu.oop;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;

import static ru.levkharitonov.spbstu.oop.Ship.print;

public class Simulation {
    final private static java.util.Random rand = new java.util.Random();
    final private static int CRANE_COST = 30000;
    final private static int UNIX_DAY = 86400000;
    final private static long UNIX_MINS = 60000;
    final private static Date DATE_START = new Date(1617235199000L);
    final private static Date DATE_END = new Date(1619827200000L);
    final private Map<CargoType, ArrayList<Ship>> queues;

    public Simulation(Map<CargoType, ArrayList<Ship>> queues) {
        this.queues = queues;
    }

    public class Unloader extends Thread {
        private ArrayList<Ship> ships;
        private int fine = 0;

        public Unloader(ArrayList<Ship> ships) {
            this.ships = ships;
        }

        @Override
        public void run() {
            int craneCount = 0;
            while (fine >= CRANE_COST * craneCount) {
                craneCount++;
                fine = 0;
                ArrayList<Crane> cranes = new ArrayList<>(craneCount);
                for(int i = 0; i < craneCount; i++) {
                    cranes.add(new Crane(ships));
                }
                //for (long time = DATE_START.getTime(); time < DATE_END.getTime(); time++) {
                    for(int i = 0; i < craneCount; i++) {
                        cranes.get(i).start();
                    }
                    for(int i = 0; i < craneCount; i++) {
                        try {
                            cranes.get(i).join();
                            fine += cranes.get(i).getFine();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                //}
            }
            System.out.println(fine + " " + craneCount);
        }


        class Crane extends Thread {
            private ArrayList<Ship> ships;
            private long time;
            private Ship current;
            private int fine = 0;

            public Crane(ArrayList<Ship> ships) {
                this.ships = ships;
                this.time = DATE_START.getTime();
            }

            public int getFine() {
                return fine;
            }

            @Override
            public void run() {
                for(Ship ship : ships) {
                    if (ship.getArrival().before(new Date(time))) {
                        if (ship.getCranes() >= 2) {
                            continue;
                        }
                        ship.setCranes(ship.getCranes() + 1);
                        current = ship;
                        break;
                    }
                    else {
                        time += UNIX_MINS;
                        return;
                    }
                }
                long finishTime = current.getArrival().getTime() + current.getUnloadingMins() * UNIX_MINS;
                if (time >= finishTime) {
                    ships.remove(current);
                    long queueTime = time - finishTime;
                    if(queueTime >= 0) {
                        current.setFine(current.getFine() + (int)queueTime/36000);
                    }
                    fine += current.getFine();
                }
                time += UNIX_MINS;
            }
        }

    }

    public void simulate() {
        infuseChaos();
        ArrayList<Thread> unloaders = new ArrayList<>(queues.keySet().size());
        for (CargoType ct: queues.keySet()) {
            ArrayList<Ship> ships = new ArrayList<>(queues.get(ct).size());
            for (Ship s : queues.get(ct)) {
                ships.add(new Ship(s));
            }
            unloaders.add(new Unloader(ships));
        }
        for (Thread unloader : unloaders) {
            unloader.start();
        }
        for (Thread unloader : unloaders) {
            try {
                unloader.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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

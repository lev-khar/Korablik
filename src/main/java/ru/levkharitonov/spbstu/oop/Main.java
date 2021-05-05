package ru.levkharitonov.spbstu.oop;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

import static ru.levkharitonov.spbstu.oop.Ship.print;

public class Main {
    public static void main(String[] args) {
        int quantity = Integer.parseInt(args[0]);
        ScheduleGenerator sg = JsonWriter.writeSchedule(quantity);
        Map<CargoType, ArrayList<Ship>> queues = ScheduleGenerator.getQueues();

        try {
            Map<CargoType, ConcurrentLinkedQueue<Ship>> red = JsonReader.readSchedule();
            for(CargoType ct: red.keySet()) {
                red.get(ct).forEach(print);
            }

            Simulation simulation = new Simulation();
            simulation.simulate();
            simulation.printReport();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

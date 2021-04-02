package ru.levkharitonov.spbstu.oop;

import java.util.*;
import java.util.function.Consumer;

import static ru.levkharitonov.spbstu.oop.Ship.print;

public class Main {
    public static void main(String[] args) {
        int quantity = Integer.parseInt(args[0]);
        ScheduleGenerator sg = JsonWriter.writeSchedule(quantity);
        Map<CargoType, ArrayList<Ship>> queues = ScheduleGenerator.getQueues();

        try {
            Map<CargoType, ArrayList<Ship>> red = JsonReader.readSchedule();
            for(CargoType ct: red.keySet()) {
                red.get(ct).forEach(print);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Simulation simulation = new Simulation(queues);
        simulation.simulate();
    }
}

package ru.levkharitonov.spbstu.oop;

import ru.levkharitonov.spbstu.oop.model.CargoType;
import ru.levkharitonov.spbstu.oop.model.Ship;
import ru.levkharitonov.spbstu.oop.JSONUtility.JsonReader;
import ru.levkharitonov.spbstu.oop.JSONUtility.JsonWriter;
import ru.levkharitonov.spbstu.oop.schedule.ScheduleGenerator;
import ru.levkharitonov.spbstu.oop.simulation.Simulation;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static ru.levkharitonov.spbstu.oop.model.Ship.print;

public class Main {
    public static void main(String[] args) {
        int quantity = Integer.parseInt(args[0]);
        ScheduleGenerator sg = JsonWriter.dwriteSchedule(quantity);
        Map<CargoType, ArrayList<Ship>> queues = ScheduleGenerator.getQueues();

        try {
            Map<CargoType, ConcurrentLinkedQueue<Ship>> red = JsonReader.readSchedule(new File("src/main/jsons/schedule.json"));
            for(CargoType ct: red.keySet()) {
                red.get(ct).forEach(print);
            }

            Simulation simulation = new Simulation(new File("src/main/jsons/schedule.json"));
            simulation.simulate();
            simulation.printReport();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

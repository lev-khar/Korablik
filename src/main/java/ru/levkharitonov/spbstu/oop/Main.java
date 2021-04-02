package ru.levkharitonov.spbstu.oop;

import java.util.*;
import java.util.function.Consumer;

import static ru.levkharitonov.spbstu.oop.Ship.print;

public class Main {
    public static void main(String[] args) {
        int quantity = Integer.parseInt(args[0]);
        ScheduleGenerator sg = JsonWriter.writeSchedule(quantity);
        Map<CargoType, ArrayList<Ship>> queues = ScheduleGenerator.getQueues();
        //sg.infuseChaos();
        System.out.println("\n\n");
        for(CargoType ct: CargoType.values()) {
            queues.get(ct).forEach(print);
        }
        try {
            Map<CargoType, ArrayList<Ship>> red = JsonReader.readSchedule();
            for(CargoType ct: CargoType.values()) {
                red.get(ct).forEach(print);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

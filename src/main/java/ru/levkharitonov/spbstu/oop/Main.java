package ru.levkharitonov.spbstu.oop;

import java.util.Deque;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.function.Consumer;

public class Main {
    static Consumer<Ship> print = new Consumer<Ship>() {
        @Override
        public void accept(Ship ship) {
            System.out.println(ship);
        }
    };

    public static void main(String[] args) {
        int quantity = Integer.parseInt(args[0]);

        ScheduleGenerator sg = JsonWriter.writeSchedule(quantity);
        Map<CargoType, PriorityQueue<Ship>> queues = ScheduleGenerator.getQueues();
        sg.infuseChaos();
        System.out.println("\n\n");
        for(CargoType ct: CargoType.values()) {
            queues.get(ct).forEach(print);
        }

        try {
            Map<CargoType, PriorityQueue<Ship>> red = JsonReader.readSchedule();
            //System.out.println(red.equals(queues));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

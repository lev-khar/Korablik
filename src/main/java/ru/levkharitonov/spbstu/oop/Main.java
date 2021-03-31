package ru.levkharitonov.spbstu.oop;

import java.util.Deque;

public class Main {
    public static void main(String[] args) {
        int quantity = Integer.parseInt(args[0]);
        Deque<Ship> ships = ScheduleGenerator.generate(quantity);
        for(int i = 0; i<quantity; i++){
            System.out.println(ships.pop());
        }
    }
}

package ru.levkharitonov.spbstu.oop;

import ru.levkharitonov.spbstu.oop.Cargo;
import ru.levkharitonov.spbstu.oop.CargoType;
import ru.levkharitonov.spbstu.oop.Ship;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;

public class ScheduleGenerator {
    final private static java.util.Random rand = new java.util.Random();

    private static Date generateDate(){
        long ms = (rand.nextInt(2592000) + 1617224400L) * 1000;
        return new Date(ms);
    }

    private static Cargo generateCargo(){
        CargoType type = CargoType.values()[rand.nextInt(3)];
        int quantity = switch(type){
            case DRY, LIQUID -> rand.nextInt(4000);
            case CONTAINER -> rand.nextInt(500);
        };
        return new Cargo(type, quantity);
    }

    public static Deque<Ship> generate(int quantity){
        Deque<Ship> ships = new ArrayDeque<>();
        NameGenerator namegen = new NameGenerator();
        for(int i = 0; i < quantity; i++){
            Ship ship = new Ship(generateDate(), namegen.generateName(), generateCargo());
            ships.add(ship);
        }
        return ships;
    }
}

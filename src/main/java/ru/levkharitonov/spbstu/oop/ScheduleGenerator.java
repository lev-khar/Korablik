package ru.levkharitonov.spbstu.oop;

import ru.levkharitonov.spbstu.oop.Cargo;
import ru.levkharitonov.spbstu.oop.CargoType;
import ru.levkharitonov.spbstu.oop.Ship;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

import static ru.levkharitonov.spbstu.oop.Ship.shipComparator;

public class ScheduleGenerator {
    final private static java.util.Random rand = new java.util.Random();
    final private static int CARGO_LIMIT = 300000;
    final private static int CONTAINER_LIMIT = 20000;
    final private static int UNIX_DAY = 86400000;

    static private Map<CargoType, PriorityQueue<Ship>> queues;

    public ScheduleGenerator() {
        queues = new HashMap<CargoType, PriorityQueue<Ship>>();
    }

    public static Map<CargoType, PriorityQueue<Ship>> getQueues() {
        return queues;
    }

    private static Date generateDate() {
        return new Date((rand.nextInt(2592000) + 1617224400L) * 1000);
    }

    private static Cargo generateCargo() {
        CargoType type = CargoType.values()[rand.nextInt(CargoType.values().length)];
        int quantity = switch(type){
            case DRY, LIQUID -> rand.nextInt(CARGO_LIMIT);
            case CONTAINER -> rand.nextInt(CONTAINER_LIMIT);
        };
        return new Cargo(type, quantity);
    }


    public void generate(int quantity) {
        PriorityQueue<Ship> dry = new PriorityQueue<>(shipComparator);
        PriorityQueue<Ship> liquid = new PriorityQueue<>(shipComparator);
        PriorityQueue<Ship> container = new PriorityQueue<>(shipComparator);
        queues.put(CargoType.DRY, dry);
        queues.put(CargoType.LIQUID, liquid);
        queues.put(CargoType.CONTAINER, container);
        NameGenerator namegen = new NameGenerator();
        for(int i = 0; i < quantity; i++) {
            Cargo cargo = generateCargo();
            switch (cargo.getType()){
                case DRY -> dry.add(new Ship(generateDate(),
                        namegen.generateName(), cargo));
                case LIQUID -> liquid.add(new Ship(generateDate(),
                        namegen.generateName(), cargo));
                case CONTAINER -> container.add(new Ship(generateDate(),
                        namegen.generateName(), cargo));
            }
        }
    }

    Consumer<Ship> changeArrival = new Consumer<Ship>() {
        @Override
        public void accept(Ship ship) {
            ship.setArrival(ship.getArrival().getTime() +
                    rand.nextInt(UNIX_DAY * 14) - (UNIX_DAY * 7));
        }
    };

    Consumer<Ship> changeUnloading = new Consumer<Ship>() {
        @Override
        public void accept(Ship ship) {
            ship.setUnloadingMins(ship.getUnloadingMins() + rand.nextInt(1440));
        }
    };

    public void infuseChaos() {
        for(CargoType ct: CargoType.values()) {
            queues.get(ct).forEach(changeArrival);
            queues.get(ct).forEach(changeUnloading);
        }
    }

    public void inputShip() throws InputMismatchException{
        Scanner in = new Scanner(System.in);
        String name;
        Date arrival;
        Cargo cargo = new Cargo();
        System.out.println("Enter arrival date (dd-MM-yyyy HH:mm:ss): ");
        try {
            arrival = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(in.nextLine());
        } catch (ParseException pe) {
            arrival = generateDate();
        }
        System.out.print("Enter ship name: ");
        name = in.nextLine();
        if (name.equals("Ever Given")) {
            arrival.setTime(arrival.getTime() + 604800000);
        }
        System.out.println("Choose cargo type:");
        for(CargoType ct: CargoType.values()) {
            System.out.println(ct.ordinal() + " - " + ct.name());
        }
        System.out.print("Enter a number: ");
        switch (in.nextInt()){
            case 0 -> cargo.setType(CargoType.DRY);
            case 1 -> cargo.setType(CargoType.LIQUID);
            case 2 -> cargo.setType(CargoType.CONTAINER);
            default -> throw new InputMismatchException("Wrong cargo type");
        }
        System.out.print("Enter cargo quantity: ");
        cargo.setQuantity(in.nextInt());
        if (cargo.getQuantity() < 0) {
            throw new InputMismatchException("Negative cargo quantity");
        }
        queues.get(cargo.getType()).add(new Ship(arrival, name, cargo));
    }
}

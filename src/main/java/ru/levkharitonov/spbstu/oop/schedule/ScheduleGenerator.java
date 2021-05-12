package ru.levkharitonov.spbstu.oop.schedule;

import ru.levkharitonov.spbstu.oop.model.Cargo;
import ru.levkharitonov.spbstu.oop.model.CargoType;
import ru.levkharitonov.spbstu.oop.model.Ship;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static ru.levkharitonov.spbstu.oop.model.Ship.shipComparator;

public class ScheduleGenerator {
    final private static java.util.Random rand = new java.util.Random();
    final private static int CARGO_LIMIT = 300000;
    final private static int CONTAINER_LIMIT = 20000;

    static private Map<CargoType, ArrayList<Ship>> queues;

    public ScheduleGenerator() {
        queues = new HashMap<>();
    }

    public static Map<CargoType, ArrayList<Ship>> getQueues() {
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
        queues.put(CargoType.DRY, new ArrayList<>());
        queues.put(CargoType.LIQUID, new ArrayList<>());
        queues.put(CargoType.CONTAINER, new ArrayList<>());
        NameGenerator namegen = new NameGenerator();
        for (int i = 0; i < quantity; i++) {
            Cargo cargo = generateCargo();
            Ship ship = new Ship(generateDate(), namegen.generateName(), cargo);
            queues.get(cargo.getType()).add(ship);
        }
        for (CargoType ct: queues.keySet()) {
            queues.get(ct).sort(shipComparator);
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
        queues.get(cargo.getType()).sort(shipComparator);
    }
}

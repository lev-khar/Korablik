package ru.levkharitonov.spbstu.oop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Comparator;
import java.util.Date;
import java.util.function.Consumer;

@JsonDeserialize(using = JsonDeserializer.class)
public class Ship {
    final private static float DRY_TONS_PER_MIN = 80;
    final private static float LIQUD_TONS_PER_MIN = 50;
    final private static float CONTAINER_PER_MIN = 4;
    private Date arrival;
    private String name;
    private Cargo cargo;
    private int unloadingMins;

    public Ship(Date arrival, String name, Cargo cargo, int unloadingMins) {
        this.arrival = arrival;
        this.name = name;
        this.cargo = cargo;
        this.unloadingMins = unloadingMins;
    }

    public Ship(Date arrival, String name, Cargo cargo) {
        this.arrival = arrival;
        this.name = name;
        this.cargo = cargo;
        this.unloadingMins = switch(cargo.getType()){
            case DRY -> (int)Math.ceil(cargo.getQuantity() / DRY_TONS_PER_MIN);
            case LIQUID -> (int)Math.ceil(cargo.getQuantity() / LIQUD_TONS_PER_MIN);
            case CONTAINER -> (int)Math.ceil(cargo.getQuantity() / CONTAINER_PER_MIN);
        };
    }

    public static Comparator<Ship> shipComparator = Comparator.comparing(Ship::getArrival);
    public static Consumer<Ship> print = System.out::println;

    public Date getArrival() {
        return arrival;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "arrival=" + arrival +
                ", name='" + name + '\'' +
                ", cargo=" + cargo +
                ", unloading minutes=" + unloadingMins +
                '}';
    }

    public void setArrival(long arrival) {
        this.arrival = new Date(arrival);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public int getUnloadingMins() {
        return unloadingMins;
    }

    public void setUnloadingMins(int um) {
        this.unloadingMins = um;
    }
}

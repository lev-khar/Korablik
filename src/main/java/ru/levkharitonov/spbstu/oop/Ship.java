package ru.levkharitonov.spbstu.oop;

import java.util.Date;

public class Ship {
    private Date arrival;
    private String name;
    private Cargo cargo;
    private int duration;

    public Ship(Date arrival, String name, Cargo cargo) {
        this.arrival = arrival;
        this.name = name;
        this.cargo = cargo;
        this.duration = 0;
    }

    public Date getArrival() {
        return arrival;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "arrival=" + arrival +
                ", name='" + name + '\'' +
                ", cargo=" + cargo +
                ", duration=" + duration +
                '}';
    }

    public void setArrival(Date arrival) {
        this.arrival = arrival;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}

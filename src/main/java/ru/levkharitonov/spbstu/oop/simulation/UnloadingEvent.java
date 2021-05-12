package ru.levkharitonov.spbstu.oop.simulation;

import ru.levkharitonov.spbstu.oop.model.Cargo;

import java.util.Date;

public class UnloadingEvent {
    private String shipName;
    private Cargo cargo;
    private long arrival;
    private long start;
    private long end;
    private long delay;

    public UnloadingEvent(String shipName, Cargo cargo, long arrival, long start, long end, long delay) {
        this.shipName = shipName;
        this.cargo = cargo;
        this.arrival = arrival;
        this.start = start;
        this.end = end;
        this.delay = delay;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public long getArrival() {
        return arrival;
    }

    public void setArrival(long arrival) {
        this.arrival = arrival;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "Unloading ship " + shipName +
                "\n\tarrived: " + new Date(arrival) +
                ", start: " + new Date(start) +
                ", end: " + new Date(end) +
                "\n\tdelay: " + formatTime(delay) +
                ", duration: " + formatTime(end - start);
    }

    private String formatTime(long time) {
        return (time / 1000) / 86400 + ":" + (time / 1000) % 86400 / 3600 + ":" + (time / 1000) % 3600 / 60;
    }
}

package ru.levkharitonov.spbstu.oop.simulation;

import java.util.List;

import static ru.levkharitonov.spbstu.oop.simulation.Simulation.*;

public class Report {
    private List<UnloadingEvent> unloads;
    private long avgDelay;
    private long allDelay;
    private long maxDelay;
    private int cranes;

    public Report(List<UnloadingEvent> unloads, long avgDelay, long allDelay, long maxDelay, int cranes) {
        this.unloads = unloads;
        this.avgDelay = avgDelay;
        this.allDelay = allDelay;
        this.maxDelay = maxDelay;
        this.cranes = cranes;
    }

    public List<UnloadingEvent> getUnloads() {
        return unloads;
    }

    public void setUnloads(List<UnloadingEvent> unloads) {
        this.unloads = unloads;
    }

    public long getAvgDelay() {
        return avgDelay;
    }

    public void setAvgDelay(long avgDelay) {
        this.avgDelay = avgDelay;
    }

    public long getAllDelay() {
        return allDelay;
    }

    public void setAllDelay(long allDelay) {
        this.allDelay = allDelay;
    }

    public long getMaxDelay() {
        return maxDelay;
    }

    public void setMaxDelay(long maxDelay) {
        this.maxDelay = maxDelay;
    }

    public int getCranes() {
        return cranes;
    }

    public void setCranes(int cranes) {
        this.cranes = cranes;
    }

    @Override
    public String toString() {
        String res = "";
        for (UnloadingEvent unload : unloads) {
            res = res.concat(unload.toString());
            res += "\n";
        }
        res += ("\nShips unloaded: " + unloads.size() +
                "\nMax delay: " + formatTime(maxDelay) +
                "\nAverage delay: " + formatTime(avgDelay) +
                "\nFines for all ships: " + allDelay / UNIX_HOUR * FINE_HOUR +
                "\nCranes needed: " + cranes);
        return res;
    }
}

package ru.levkharitonov.spbstu.oop;

import ru.levkharitonov.spbstu.oop.simulation.UnloadingEvent;

import java.util.List;

public class Utility {
    final public static long CRANE_COST = 30000;
    final public static int FINE_HOUR = 100;
    final public static int UNIX_DAY = 86400000;
    final public static long UNIX_MINS = 60000;
    final public static long UNIX_HOUR = 3600000;
    public static String formatTime(long time) {
        return (time / 1000) / 86400 + ":" + (time / 1000) % 86400 / 3600 + ":" + (time / 1000) % 3600 / 60;
    }
}

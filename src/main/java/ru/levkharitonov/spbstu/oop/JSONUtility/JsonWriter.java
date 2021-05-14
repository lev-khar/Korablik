package ru.levkharitonov.spbstu.oop.JSONUtility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.levkharitonov.spbstu.oop.model.CargoType;
import ru.levkharitonov.spbstu.oop.schedule.ScheduleGenerator;
import ru.levkharitonov.spbstu.oop.model.Ship;
import ru.levkharitonov.spbstu.oop.model.Report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class JsonWriter {
    public static void writeReport(Report report, File file) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(file, report);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static String organiseSchedule(int quantity, int addedShips) throws JsonProcessingException {
        ScheduleGenerator sg = new ScheduleGenerator();
        sg.generate(quantity);
        addManually(sg, addedShips);
        Map<CargoType, ArrayList<Ship>> queues = ScheduleGenerator.getQueues();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(queues);
    }

    public static void addManually(ScheduleGenerator sg, int addedShips) {
        for (int i = 0; i < addedShips; i++) {
            try {
                sg.inputShip();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.flush();
                i--;
            }
        }
    }
}

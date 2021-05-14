package ru.levkharitonov.spbstu.oop.JSONUtility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ru.levkharitonov.spbstu.oop.model.CargoType;
import ru.levkharitonov.spbstu.oop.schedule.ScheduleGenerator;
import ru.levkharitonov.spbstu.oop.model.Ship;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class JsonWriter {
    public static ScheduleGenerator dwriteSchedule(int quantity) {
        ScheduleGenerator sg = new ScheduleGenerator();
        sg.generate(quantity);
        addManually(sg);
        Map<CargoType, ArrayList<Ship>> queues = ScheduleGenerator.getQueues();
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("src/main/jsons/schedule.json"), queues);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return sg;
    }

    public static String organiseSchedule(int quantity) throws JsonProcessingException {
        ScheduleGenerator sg = new ScheduleGenerator();
        sg.generate(quantity);
        addManually(sg);
        Map<CargoType, ArrayList<Ship>> queues = ScheduleGenerator.getQueues();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(queues);
    }

    public static void addManually(ScheduleGenerator sg) {
        Scanner in = new Scanner(System.in);
        System.out.print("Number of manually added ships (0 if there are none): ");
        int addedShips = in.nextInt();
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

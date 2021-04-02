package ru.levkharitonov.spbstu.oop;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class JsonWriter {
    public static ScheduleGenerator writeSchedule(int quantity) {
        ScheduleGenerator sg = new ScheduleGenerator();
        Scanner in = new Scanner(System.in);
        sg.generate(quantity);
        Map<CargoType, PriorityQueue<Ship>> queues = ScheduleGenerator.getQueues();
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

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("src/main/jsons/schedule.json"), queues);
            String jsonString = mapper.writeValueAsString(queues);
            System.out.println(jsonString);
            String jsonString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(queues);
            System.out.println(jsonString2);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return sg;
    }
}

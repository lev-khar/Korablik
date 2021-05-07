package ru.levkharitonov.spbstu.oop;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static ru.levkharitonov.spbstu.oop.Ship.shipComparator;

public class JsonReader {
    public static Map<CargoType, ConcurrentLinkedQueue<Ship>> readSchedule() throws IOException {
        TypeReference<HashMap<String, ArrayList<Ship>>> typeRef = new TypeReference<>() {};
        HashMap<String, ArrayList<Ship>> queues = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            queues = mapper.readValue(new File("src/main/jsons/schedule.json"), typeRef);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        for (String ct: queues.keySet()) {
            queues.get(ct).sort(shipComparator);
        }
        HashMap<CargoType, ConcurrentLinkedQueue<Ship>> res = new HashMap<>();
        for( String ct: queues.keySet()) {
            switch (ct) {
                case "DRY" -> res.put(CargoType.DRY, new ConcurrentLinkedQueue<>(queues.get(ct)));
                case "LIQUID" -> res.put(CargoType.LIQUID, new ConcurrentLinkedQueue<>(queues.get(ct)));
                case "CONTAINER" -> res.put(CargoType.CONTAINER, new ConcurrentLinkedQueue<>(queues.get(ct)));
                default -> throw new IOException("Error reading Ship");
            }
        }
        return res;
    }
}

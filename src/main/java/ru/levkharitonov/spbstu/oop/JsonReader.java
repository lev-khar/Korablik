package ru.levkharitonov.spbstu.oop;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static ru.levkharitonov.spbstu.oop.Ship.shipComparator;

public class JsonReader {
    public static Map<CargoType, ArrayList<Ship>> readSchedule() throws IOException {
        TypeReference<HashMap<String, ArrayList<Ship>>> typeRef = new TypeReference<>() {};
        HashMap<String, ArrayList<Ship>> queues = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            queues = mapper.readValue(new File("src/main/jsons/schedule.json"), typeRef);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        HashMap<CargoType, ArrayList<Ship>> res = new HashMap<>();
        for( String ct: queues.keySet()) {
            switch (ct) {
                case "DRY" -> res.put(CargoType.DRY, queues.get(ct));
                case "LIQUID" -> res.put(CargoType.LIQUID, queues.get(ct));
                case "CONTAINER" -> res.put(CargoType.CONTAINER, queues.get(ct));
                default -> throw new IOException("Error reading Ship");
            }
        }
        for (CargoType ct: res.keySet()) {
            res.get(ct).sort(shipComparator);
        }
        return res;
    }
}

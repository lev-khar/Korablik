package ru.levkharitonov.spbstu.oop;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

import static ru.levkharitonov.spbstu.oop.Ship.shipComparator;

public class JsonReader {
    static Consumer<Ship> print = new Consumer<Ship>() {
        @Override
        public void accept(Ship ship) {
            System.out.println(ship);
        }
    };
    public static Map<CargoType, PriorityQueue<Ship>> readSchedule() throws IOException {
        TypeReference<HashMap<String, ArrayList<Ship>>> typeRef = new TypeReference<>() {};
        HashMap<String, ArrayList<Ship>> queues = new HashMap<String, ArrayList<Ship>>();
        ObjectMapper mapper = new ObjectMapper();
        /*SimpleModule module = new SimpleModule();
        module.addDeserializer(Ship.class, new JsonDeserializer());
        mapper.registerModule(module);*/
        try {
            queues = mapper.readValue(new File("src/main/jsons/schedule.json"), typeRef);

            String jsonString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(queues);
            System.out.println(jsonString2);


        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        HashMap<CargoType, PriorityQueue<Ship>> res = new HashMap<>();
        PriorityQueue<Ship> dry = new PriorityQueue<>(shipComparator);
        PriorityQueue<Ship> liquid = new PriorityQueue<>(shipComparator);
        PriorityQueue<Ship> container = new PriorityQueue<>(shipComparator);
        for( String ct: queues.keySet()) {
            //queues.get(ct).forEach(print);
            switch (ct) {
                case "DRY" -> dry.addAll(queues.get(ct));
                case "LIQUID" -> liquid.addAll(queues.get(ct));
                case "CONTAINER" -> container.addAll(queues.get(ct));
                default -> throw new IOException("Error reading Ship");
            }
        }

        res.put(CargoType.DRY, dry);
        res.put(CargoType.LIQUID, liquid);
        res.put(CargoType.CONTAINER, container);
        for(CargoType ct: CargoType.values()) {
            res.get(ct).forEach(print);
        }

        return res;
    }
}

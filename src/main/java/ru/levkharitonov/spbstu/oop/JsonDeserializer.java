package ru.levkharitonov.spbstu.oop;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;

import java.io.IOException;
import java.util.Date;

public class JsonDeserializer extends StdDeserializer<Ship> {
    public JsonDeserializer() {
        this(null);
    }

    protected JsonDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Ship deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        long arrivalTime = node.get("arrival").asLong();
        Date arrival = new Date(arrivalTime);
        String name = node.get("name").asText();
        String cargoType = node.get("cargo").get("type").asText();
        int cargoQuantity = node.get("cargo").get("quantity").asInt();
        int unloading = node.get("unloadingMins").asInt();
        Cargo cargo = new Cargo();
        switch (cargoType) {
            case "DRY" -> cargo.setType(CargoType.DRY);
            case "LIQUID" -> cargo.setType(CargoType.LIQUID);
            case "CONTAINER" -> cargo.setType(CargoType.CONTAINER);
            default -> throw new IOException("Error reading Ship");
        }
        cargo.setQuantity(cargoQuantity);
        return new Ship(arrival, name, cargo, unloading);
    }
}

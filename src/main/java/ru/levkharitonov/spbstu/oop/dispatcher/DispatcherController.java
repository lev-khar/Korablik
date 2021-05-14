package ru.levkharitonov.spbstu.oop.dispatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.levkharitonov.spbstu.oop.JSONUtility.JsonReader;
import ru.levkharitonov.spbstu.oop.JSONUtility.JsonWriter;
import ru.levkharitonov.spbstu.oop.model.CargoType;
import ru.levkharitonov.spbstu.oop.model.Ship;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

@Controller
public class DispatcherController {
    private Integer quantity = 15;
    private File sfile;

    @PostMapping("/init")
    @ResponseBody
    public void init(@RequestParam Integer quantity) {
        if (quantity <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only positive quantities allowed");
        this.quantity = Objects.requireNonNullElse(quantity, 15);
        this.sfile = new File("src/main/jsons/d.json");
    }

    @PostMapping("/report")
    @ResponseBody
    public void saveReport(@RequestParam String report) {
        System.out.println(report);
        //TODO write to JSON
    }

    @GetMapping(value ="/dispatch-schedule")
    @ResponseBody
    public String dispatchSchedule() {
        if((this.sfile == null) || !this.sfile.exists()) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not initialised");
        RestTemplate rt = new RestTemplate();
        String address = "http://localhost:8088/schedule?quantity=" + quantity;
        String schedule = rt.getForEntity(address, String.class).getBody();
        if (schedule == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty schedule");
        try {
            FileWriter writer = new FileWriter(this.sfile);
            writer.write(schedule);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return schedule;
    }

    @GetMapping(value ="/download-schedule")
    @ResponseBody
    public Map<CargoType, ConcurrentLinkedQueue<Ship>> downloadSchedule(@RequestParam String filename) {
        if((this.sfile == null) || !this.sfile.exists()) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not initialised");
        File file = new File(filename);
        if(!file.equals(this.sfile) || !file.exists()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad filename");
        try {
            return JsonReader.readSchedule(file);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in reading schedule");
        }
    }
}

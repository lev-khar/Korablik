package ru.levkharitonov.spbstu.oop.dispatcher;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.levkharitonov.spbstu.oop.JSONUtility.JsonReader;
import ru.levkharitonov.spbstu.oop.JSONUtility.JsonWriter;
import ru.levkharitonov.spbstu.oop.model.CargoType;
import ru.levkharitonov.spbstu.oop.model.Ship;
import ru.levkharitonov.spbstu.oop.model.Report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

@Controller
public class DispatcherController {
    private Integer quantity = 15;
    private Integer add = 0;
    private File sfile;
    private File rfile;

    @PostMapping("/init")
    @ResponseBody
    public void init(@RequestParam Integer quantity, @RequestParam Integer add) {
        if ((quantity <= 0) || (add < 0)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only positive quantities allowed");
        }
        this.add = add;
        this.quantity = Objects.requireNonNullElse(quantity, 15);
        this.sfile = new File("src/main/jsons/d.json");
        this.rfile = new File("src/main/jsons/report.json");
    }

    @PostMapping("/report")
    @ResponseBody
    public void saveReport(@RequestBody Report report) {
        System.out.println(report);
        JsonWriter.writeReport(report, rfile);
    }

    @GetMapping(value ="/dispatch-schedule")
    @ResponseBody
    public File dispatchSchedule() {
        if ((this.sfile == null) || !this.sfile.exists()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not initialised");
        }
        RestTemplate rt = new RestTemplate();
        String address = "http://localhost:8088/schedule?quantity=" + quantity + "&add=" + add;
        String schedule = rt.getForEntity(address, String.class).getBody();
        if (schedule == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty schedule");
        }
        try {
            FileWriter writer = new FileWriter(this.sfile);
            writer.write(schedule);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.sfile;
    }

    @GetMapping(value ="/download-schedule")
    @ResponseBody
    public Map<CargoType, ConcurrentLinkedQueue<Ship>> downloadSchedule(@RequestParam String filename) {
        if ((this.sfile == null) || !this.sfile.exists()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not initialised");
        }
        File file = new File(filename);
        if (!file.equals(this.sfile) || !file.exists()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad filename");
        }
        try {
            return JsonReader.readSchedule(file);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in reading schedule");
        }
    }

    @GetMapping(value ="/proceed")
    @ResponseBody
    public String proceed() {
        if ((this.sfile == null) || !this.sfile.exists()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not initialised");
        }
        RestTemplate rt = new RestTemplate();
        String address = "http://localhost:8085/simulate";
        rt.postForEntity(address, "",String.class);
        try {
            return Files.readString(rfile.toPath());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in reading report");
        }
    }
}

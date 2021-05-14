package ru.levkharitonov.spbstu.oop.simulation;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.levkharitonov.spbstu.oop.model.Report;

import java.io.File;

@Controller
public class SimulationController {

    @PostMapping("/simulate")
    @ResponseBody
    public void performSimulation() {
        RestTemplate rt = new RestTemplate();
        String saddress = "http://localhost:8080/dispatch-schedule";
        File file = rt.getForEntity(saddress, File.class).getBody();
        if ((file == null) || !file.exists()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule file not found");
        }
        Simulation simulation = new Simulation(file);
        simulation.simulate();
        String raddress = "http://localhost:8080/report";
        rt.postForEntity(raddress, simulation.formReport(), Report.class);
    }
}

package ru.levkharitonov.spbstu.oop.simulation;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.util.Arrays;

@Controller
public class SimulationController {

    @PostMapping("/simulate")
    @ResponseBody
    public void performSimulation() {
        RestTemplate rt = new RestTemplate();
        String saddress = "http://localhost:8080/dispatch-schedule";
        File file = rt.getForEntity(saddress, File.class).getBody();
        if((file == null) || !file.exists()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule file not found");
        Simulation simulation = new Simulation(file);
        simulation.simulate();
        String raddress = "http://localhost:8080/report";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Report> entity = new HttpEntity<Report>(simulation.formReport(), headers);
        ResponseEntity<Report> res = rt.exchange(raddress, HttpMethod.POST, entity, Report.class);
        //rt.postForEntity(raddress, simulation.formReport(), Report.class);
    }
}

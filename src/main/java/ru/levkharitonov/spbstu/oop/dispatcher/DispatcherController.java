package ru.levkharitonov.spbstu.oop.dispatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.levkharitonov.spbstu.oop.JSONUtility.JsonWriter;

@Controller
public class DispatcherController {

    @PostMapping("/report")
    @ResponseBody
    public void SaveReport(@RequestParam String report) {
        System.out.println(report);
        //TODO write to JSON
    }
}

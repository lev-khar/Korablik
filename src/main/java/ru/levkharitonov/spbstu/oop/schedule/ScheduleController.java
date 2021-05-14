package ru.levkharitonov.spbstu.oop.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.levkharitonov.spbstu.oop.JSONUtility.JsonWriter;

@Controller
public class ScheduleController {

    @GetMapping(value ="/schedule")
    @ResponseBody
    public String getSchedule(@RequestParam Integer quantity) { //TODO переместить сюда параметр колическая кораблей чеез консоль
        if(quantity == null) {
            quantity = 15;
        }
        try {
            return JsonWriter.organiseSchedule(quantity);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error in serialization";
        }
    }

}

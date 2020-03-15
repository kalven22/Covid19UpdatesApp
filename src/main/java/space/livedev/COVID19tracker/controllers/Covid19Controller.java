package space.livedev.COVID19tracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import space.livedev.COVID19tracker.services.CovidDataService;

@Controller
public class Covid19Controller
{
    @Autowired
    CovidDataService covidDataService;

    @GetMapping("/")
    public String display(Model model){

        model.addAttribute("getAllData", covidDataService.getAllData());
        model.addAttribute("test", "TEST");
        model.addAttribute("totalGlobalCases", covidDataService.getTotalGlobalCases());
        model.addAttribute("totalCanadaCases", covidDataService.getTotalCanadaCases());
        model.addAttribute("totalUSCases", covidDataService.getTotalUSCases());
        return "index";
    }
}

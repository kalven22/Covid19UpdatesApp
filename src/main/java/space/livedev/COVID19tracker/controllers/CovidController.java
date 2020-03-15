package space.livedev.COVID19tracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import space.livedev.COVID19tracker.services.CovidDataConfirmedService;

@Controller
public class CovidController
{
    @Autowired
    CovidDataConfirmedService covidDataConfirmedService;

    @GetMapping("/")
    public String display(Model model){

        model.addAttribute("getAllData", covidDataConfirmedService.getAllData());
        model.addAttribute("totalGlobalCases", covidDataConfirmedService.getTotalGlobalCases());
        model.addAttribute("totalCanadaCases", covidDataConfirmedService.getTotalCanadaCases());
        model.addAttribute("totalUSCases", covidDataConfirmedService.getTotalUSCases());
        return "index";
    }
}

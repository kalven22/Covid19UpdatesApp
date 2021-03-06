package space.livedev.COVID19tracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import space.livedev.COVID19tracker.services.CovidDataConfirmedService;
import space.livedev.COVID19tracker.services.CovidDataRecoveredService;

@Controller
public class CovidController
{
    @Autowired
    CovidDataConfirmedService covidDataConfirmedService;

    @Autowired
    CovidDataRecoveredService covidDataRecoveredService;


    @GetMapping("/")
    public String display(Model model){

        model.addAttribute("getAllData", covidDataConfirmedService.getAllData());
        model.addAttribute("totalGlobalCases", covidDataConfirmedService.getTotalGlobalCases());
        model.addAttribute("totalCanadaCases", covidDataConfirmedService.getTotalCanadaCases());
        model.addAttribute("totalUSCases", covidDataConfirmedService.getTotalUSCases());
        model.addAttribute("totalIndiaCases", covidDataConfirmedService.getTotalIndiaCases());

        model.addAttribute("getAllRecoveredData", covidDataRecoveredService.getAllRecoveredData());
        model.addAttribute("totalGlobalRecoveredCases", covidDataRecoveredService.getTotalGlobalRecoveredCases());
        model.addAttribute("totalCanadaRecoveredCases", covidDataRecoveredService.getTotalCanadaRecoveredCases());
        model.addAttribute("totalUSRecoveredCases", covidDataRecoveredService.getTotalUSRecoveredCases());
        model.addAttribute("totalIndiaRecoveredCases", covidDataRecoveredService.getTotalIndiaRecoveredCases());

        model.addAttribute("allCanadaData", covidDataConfirmedService.getAllCanadaData());
        model.addAttribute("allCanadaRecoveredData", covidDataRecoveredService.getAllCanadaRecoveredData());

        model.addAttribute("allUSData", covidDataConfirmedService.getAllUSData());
        model.addAttribute("allUSRecoveredData", covidDataRecoveredService.getAllUSRecoveredData());

        model.addAttribute("allIndiaData", covidDataConfirmedService.getAllIndiaData());
        model.addAttribute("allIndiaRecoveredData", covidDataRecoveredService.getAllIndiaRecoveredData());
        return "index";
    }
}

package com.chpok.logiweb.controller;

import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.service.DriverService;
import com.chpok.logiweb.service.LocationService;
import com.chpok.logiweb.service.TruckService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Collections;

@Controller
@RequestMapping("/employeeDriver")
public class EmployeeDriverPageController {
    private static final String REDIRECT_TO_MAIN_PAGE = "redirect:/employeeDriver";

    private final DriverService driverService;
    private final LocationService locationService;
    private final TruckService truckService;

    public EmployeeDriverPageController(DriverService driverService, LocationService locationService, TruckService truckService) {
        this.driverService = driverService;
        this.locationService = locationService;
        this.truckService = truckService;
    }

    @GetMapping
    public String getDrivers(Model model) {
        model.addAttribute("drivers", driverService.getAllDrivers());

        model.addAttribute("driver", new DriverDto());

        model.addAttribute("locations", locationService.getAllLocations());

        return "employeeDriverPage";
    }

    @GetMapping(value = "/drivers", produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String getDrivers() {
        try {
            final ObjectMapper mapper = new ObjectMapper();

            mapper.registerModule(new JavaTimeModule());

            return mapper.writeValueAsString(driverService.getAllDrivers());
        } catch (IOException ioe) {
            throw new EntityNotFoundException();
        }
    }

    @GetMapping(value = "/drivers/{id}", produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String getDriver(@PathVariable Long id) {
        try {
            final ObjectMapper mapper = new ObjectMapper();

            mapper.registerModule(new JavaTimeModule());

            return mapper.writeValueAsString(driverService.getDriverById(id));
        } catch (IOException ioe) {
            throw new EntityNotFoundException();
        }
    }

    @PostMapping
    public String addDriver(@ModelAttribute DriverDto driverDto) {
        driverService.saveDriver(driverDto);

        return REDIRECT_TO_MAIN_PAGE;
    }

    @DeleteMapping
    public String deleteDriver(@RequestParam(name = "delete-driver-id") Long id) {
        if (id != null) {
            driverService.deleteDriver(id);
        }

        return REDIRECT_TO_MAIN_PAGE;
    }

    @PutMapping
    public String updateDriver(@ModelAttribute DriverDto driverDto) {
        driverService.updateDriver(driverDto);

        return REDIRECT_TO_MAIN_PAGE;
    }

    @PostMapping("/search")
    public ModelAndView searchDriver(@RequestParam(name = "driverId") Long driverId) {
        final ModelAndView mav = new ModelAndView("employeeDriverPage");

        if (driverId != null) {
            try {
                mav.addObject("searchId", driverId);
                mav.addObject("drivers", Collections.singletonList(driverService.getDriverById(driverId)));
            } catch (EntityNotFoundException nfe) {
                mav.addObject("searchId", driverId);
                mav.addObject("drivers", Collections.emptyList());
            }
        } else {
            mav.addObject("drivers", driverService.getAllDrivers());
        }

        mav.addObject("driver", new DriverDto());
        mav.addObject("locations", locationService.getAllLocations());

        return mav;
    }
}

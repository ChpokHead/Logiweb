package com.chpok.logiweb.controller;

import com.chpok.logiweb.dto.CargoDto;
import com.chpok.logiweb.service.CargoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Controller
@RequestMapping("/employeeCargo")
public class EmployeeCargoPageController {
    private static final String REDIRECT_TO_MAIN_PAGE = "redirect:/employeeCargo";

    private final CargoService cargoService;

    public EmployeeCargoPageController(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @GetMapping
    public String getCargos(Model model) {
        model.addAttribute("cargo", new CargoDto());
        model.addAttribute("cargos", cargoService.getAllCargos());

        return "employeeCargoPage";
    }

    @GetMapping(value = "/{id}", produces={"application/json; charset=UTF-8"})
    @ResponseBody
    public String getCargo(@PathVariable Long id) {
        try {
            final ObjectMapper mapper = new ObjectMapper();

            return mapper.writeValueAsString(cargoService.getCargoById(id));
        } catch (IOException ioe) {
            throw new EntityNotFoundException();
        }
    }

    @PostMapping
    public String addCargo(@ModelAttribute CargoDto cargo) {
        cargoService.saveCargo(cargo);

        return REDIRECT_TO_MAIN_PAGE;
    }

    @PutMapping
    public String updateCargo(@ModelAttribute CargoDto cargoDto) {
        cargoService.updateCargo(cargoDto);

        return REDIRECT_TO_MAIN_PAGE;
    }

    @DeleteMapping
    public String deleteCargo(@RequestParam("delete-cargo-id")Long deletingCargoId) {
        if (deletingCargoId != null) {
            cargoService.deleteCargo(deletingCargoId);
        }

        return REDIRECT_TO_MAIN_PAGE;
    }
}

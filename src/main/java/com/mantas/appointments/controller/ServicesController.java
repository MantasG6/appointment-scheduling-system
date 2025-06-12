package com.mantas.appointments.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/services")
public class ServicesController {

    @GetMapping
    public String getAllServices() {
        return "All Services";
    }
}

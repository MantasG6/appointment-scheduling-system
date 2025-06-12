package com.mantas.appointments.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/appointments")
public class AppointmentsController {

    @PreAuthorize("hasRole('PROVIDER')")
    @GetMapping("/provider")
    public String provider() {
        return "Welcome, Provider!";
    }

    @GetMapping("/client")
    public String client() {
        return "Welcome, Client!";
    }

}

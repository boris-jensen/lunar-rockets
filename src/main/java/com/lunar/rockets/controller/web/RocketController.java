package com.lunar.rockets.controller.web;

import com.lunar.rockets.controller.web.dto.rocket.Rocket;
import com.lunar.rockets.domain.objects.DomainRocket;
import com.lunar.rockets.domain.service.RocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rockets")
@RequiredArgsConstructor
public class RocketController {

    private final RocketService rocketService;

    @GetMapping
    public ResponseEntity<List<Rocket>> getRockets() {
        List<Rocket> rockets = rocketService
                .getRockets()
                .stream()
                .map(RocketController::toDtoRocket)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rockets);
    }

    @GetMapping("/{rocketId}")
    public ResponseEntity<Rocket> getRocket(@PathVariable UUID rocketId) {
        Rocket rocket = toDtoRocket(rocketService.getRocket(rocketId));
        return ResponseEntity.ok(rocket);
    }

    private static Rocket toDtoRocket(DomainRocket rocket) {
        return new Rocket(rocket.rocketID(), rocket.type(), rocket.mission());
    }
}

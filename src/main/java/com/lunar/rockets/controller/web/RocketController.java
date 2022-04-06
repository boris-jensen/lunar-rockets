package com.lunar.rockets.controller.web;

import com.lunar.rockets.controller.web.dto.rocket.Rocket;
import com.lunar.rockets.domain.objects.RocketState;
import com.lunar.rockets.domain.service.RocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
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
        Optional<RocketState> rocketOpt = rocketService.getRocket(rocketId);
        return rocketOpt
            .map(RocketController::toDtoRocket)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    private static Rocket toDtoRocket(RocketState rocket) {
        if (rocket instanceof RocketState.Flying flying) {
            return new Rocket.FlyingRocket(flying.getChannel(), flying.getType(), flying.getMission(), flying.getSpeed());
        } else if (rocket instanceof RocketState.Exploded exploded) {
            return new Rocket.ExplodedRocket(exploded.getChannel(), exploded.getType(), exploded.getMission(), exploded.getReason());
        } else {
            throw new RuntimeException("Unknown rocket state: " + rocket.getClass().getName());
        }
    }
}

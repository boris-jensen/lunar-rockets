package com.lunar.rockets.domain.service;

import com.lunar.rockets.domain.objects.DomainMessage;
import com.lunar.rockets.domain.objects.RocketState;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class RocketService {

    ConcurrentMap<UUID, RocketState> rockets = new ConcurrentHashMap<>();

    public void acceptMessage(@NotNull DomainMessage<?> message) {
        rockets.compute(message.rocketID(), (k, state) -> (state == null)
                ? RocketState.initial(message)
                : state.receiveMessage(message.details()));
    }

    public List<RocketState> getRockets() {
        return rockets
            .values()
            .stream()
            .toList();
    }

    public Optional<RocketState> getRocket(UUID rocketId) {
        return Optional.ofNullable(rockets.get(rocketId));
    }
}

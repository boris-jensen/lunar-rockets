package com.lunar.rockets.domain.service;

import com.lunar.rockets.domain.objects.DomainMessage;
import com.lunar.rockets.domain.objects.DomainRocket;
import com.lunar.rockets.domain.objects.RocketState;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class RocketService {

    public void acceptMessage(@NotNull DomainMessage<?> message) {
        System.out.println(message.messageNumber());
    }

    public List<DomainRocket> getRockets() {
        return new ArrayList<>();
    }

    public DomainRocket getRocket(UUID rocketId) {
        return new DomainRocket(UUID.randomUUID(), "foo", "bar", new RocketState.Flying(1000));
    }
}

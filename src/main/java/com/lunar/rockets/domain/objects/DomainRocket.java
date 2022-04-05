package com.lunar.rockets.domain.objects;

import java.util.UUID;

public record DomainRocket(UUID rocketID, String type, String mission, RocketState state) {
}

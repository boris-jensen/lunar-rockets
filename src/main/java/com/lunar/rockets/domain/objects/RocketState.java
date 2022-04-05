package com.lunar.rockets.domain.objects;

public sealed interface RocketState permits RocketState.Flying, RocketState.Exploded{

    record Flying(int speed) implements RocketState {}
    record Exploded(String reason) implements RocketState {}
}

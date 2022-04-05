package com.lunar.rockets.controller.web.dto.rocket;

import lombok.Value;

import java.util.UUID;

@Value
public class Rocket {
    UUID channel;
    String type;
    String mission;
}

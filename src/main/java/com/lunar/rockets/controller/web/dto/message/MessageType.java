package com.lunar.rockets.controller.web.dto.message;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageType {
    ROCKET_LAUNCHED("RocketLaunched"),
    ROCKET_SPEED_INCREASED("RocketSpeedIncreased"),
    ROCKET_SPEED_DECREASED("RocketSpeedDecreased"),
    ROCKET_EXPLODED("RocketExploded"),
    ROCKET_MISSION_CHANGED("RocketMissionChanged");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }
}

package com.lunar.rockets.controller.web.dto.rocket;

import lombok.Value;

import java.util.UUID;

public interface Rocket {

    String getState();

    @Value
    class FlyingRocket implements Rocket {
        UUID channel;
        String type;
        String mission;
        int speed;

        @Override
        public String getState() {
            return "FLYING";
        }
    }

    @Value
    class ExplodedRocket implements Rocket {
        UUID channel;
        String type;
        String mission;
        String reason;

        @Override
        public String getState() {
            return "EXPLODED";
        }
    }
}

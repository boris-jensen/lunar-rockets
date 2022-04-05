package com.lunar.rockets.controller.web.dto.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

public abstract sealed class Message permits
    Message.RocketLaunchedMessage,
    Message.RocketSpeedIncreasedMessage,
    Message.RocketSpeedDecreasedMessage,
    Message.RocketExplodedMessage,
    Message.RocketMissionChangedMessage {

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static final class RocketLaunchedMessage extends Message {
        String type;
        int launchSpeed;
        String mission;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static final class RocketSpeedIncreasedMessage extends Message {
        int by;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static final class RocketSpeedDecreasedMessage extends Message {
        int by;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static final class RocketExplodedMessage extends Message {
        String reason;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static final class RocketMissionChangedMessage extends Message {
        String newMission;
    }
}

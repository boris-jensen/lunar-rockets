package com.lunar.rockets.domain.objects;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;
import com.lunar.rockets.domain.objects.DomainMessageDetails.RocketLaunched;
import com.lunar.rockets.domain.objects.DomainMessageDetails.RocketSpeedIncreased;
import com.lunar.rockets.domain.objects.DomainMessageDetails.RocketSpeedDecreased;
import com.lunar.rockets.domain.objects.DomainMessageDetails.RocketExploded;
import com.lunar.rockets.domain.objects.DomainMessageDetails.RocketMissionChanged;

import java.util.UUID;

@Value
@NonFinal
public abstract class RocketState {

    UUID channel;
    String type;
    String mission;

    public static RocketState initial(UUID rocketId, DomainMessageDetails details) {
        if (details instanceof RocketLaunched rocketLaunched) {
            return new Flying(rocketId, rocketLaunched.type(), rocketLaunched.mission(), rocketLaunched.launchSpeed());
        } else {
            return null; // TODO should we explode here, if exploding?
        }
    }

    public abstract RocketState receiveMessage(DomainMessageDetails message);

    @EqualsAndHashCode(callSuper = true)
    @Value
    public static class Flying extends RocketState {

        int speed;

        public Flying(UUID channel, String type, String mission, int speed) {
            super(channel, type, mission);
            this.speed = speed;
        }

        @Override
        public RocketState receiveMessage(DomainMessageDetails message) {
            if (message instanceof RocketLaunched) {
                return this; // TODO should we explode here?
            } else if (message instanceof RocketSpeedIncreased rocketSpeedIncreased) {
                return new Flying(getChannel(), getType(), getMission(), getSpeed() + rocketSpeedIncreased.by());
            } else if (message instanceof RocketSpeedDecreased rocketSpeedDecreased) { // TODO should we fail here, if speed is negative?
                return new Flying(getChannel(), getType(), getMission(), getSpeed() - rocketSpeedDecreased.by());
            } else if (message instanceof RocketExploded rocketExploded) {
                return new Exploded(getChannel(), getType(), getMission(), rocketExploded.reason());
            } else if (message instanceof RocketMissionChanged rocketMissionChanged) {
                return new Flying(getChannel(), getType(), rocketMissionChanged.newMission(), getSpeed());
            }
            throw new RuntimeException("Unknown message details type: " + message.getClass().getName());
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    public static class Exploded extends RocketState {

        String reason;

        public Exploded(UUID channel, String type, String mission, String reason) {
            super(channel, type, mission);
            this.reason = reason;
        }

        @Override
        public RocketState receiveMessage(DomainMessageDetails message) {
            return this;
        }
    }
}

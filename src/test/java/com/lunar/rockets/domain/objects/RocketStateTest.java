package com.lunar.rockets.domain.objects;

import org.junit.jupiter.api.Test;
import com.lunar.rockets.domain.objects.DomainMessageDetails.RocketLaunched;
import com.lunar.rockets.domain.objects.DomainMessageDetails.RocketSpeedIncreased;
import com.lunar.rockets.domain.objects.DomainMessageDetails.RocketSpeedDecreased;
import com.lunar.rockets.domain.objects.DomainMessageDetails.RocketExploded;
import com.lunar.rockets.domain.objects.DomainMessageDetails.RocketMissionChanged;

import java.util.UUID;

public class RocketStateTest {

    UUID uuid = UUID.randomUUID();
    String type = "type";
    String mission1 = "mission1";
    String mission2 = "mission2";
    String reason1 = "reason1";
    String reason2 = "reason2";

    @Test
    public void initialLaunchIsFlying() {
        RocketState state = RocketState.initial(uuid, new DomainMessageDetails.RocketLaunched(type, 100, mission1));
        assert(state instanceof RocketState.Flying);
    }

    @Test
    public void initialExplodedIsNull() {
        RocketState state = RocketState.initial(uuid, new DomainMessageDetails.RocketExploded(reason1));
        assert(state == null);
    }

    @Test
    public void duplicateLaunchedIsNoop() {
        RocketState state = RocketState
                .initial(uuid, new DomainMessageDetails.RocketLaunched(type, 100, mission1))
                .receiveMessage(new RocketLaunched(type, 200, mission2));
        RocketState.Flying newState = (RocketState.Flying)state;
        assert(newState.getSpeed() == 100);
        assert(newState.getType().equals(type));
        assert(newState.getMission().equals(mission1));
        assert(newState.getChannel().equals(uuid));
    }

    @Test
    public void speedIncreaseWorks() {
        RocketState state = RocketState
            .initial(uuid, new DomainMessageDetails.RocketLaunched(type, 100, mission1))
            .receiveMessage(new RocketSpeedIncreased(10));
        RocketState.Flying newState = (RocketState.Flying)state;
        assert(newState.getSpeed() == 110);
        assert(newState.getType().equals(type));
        assert(newState.getMission().equals(mission1));
        assert(newState.getChannel().equals(uuid));
    }

    @Test
    public void speedDecreaseWorks() {
        RocketState state = RocketState
                .initial(uuid, new DomainMessageDetails.RocketLaunched(type, 100, mission1))
                .receiveMessage(new RocketSpeedDecreased(10));
        RocketState.Flying newState = (RocketState.Flying)state;
        assert(newState.getSpeed() == 90);
        assert(newState.getType().equals(type));
        assert(newState.getMission().equals(mission1));
        assert(newState.getChannel().equals(uuid));
    }

    @Test
    public void exlodedWorks() {
        RocketState state = RocketState
                .initial(uuid, new DomainMessageDetails.RocketLaunched(type, 100, mission1))
                .receiveMessage(new RocketExploded(reason1));
        RocketState.Exploded newState = (RocketState.Exploded)state;
        assert(newState.getReason().equals(reason1));
        assert(newState.getType().equals(type));
        assert(newState.getMission().equals(mission1));
        assert(newState.getChannel().equals(uuid));
    }

    @Test
    public void missionChangeWorks() {
        RocketState state = RocketState
                .initial(uuid, new DomainMessageDetails.RocketLaunched(type, 100, mission1))
                .receiveMessage(new RocketMissionChanged(mission2));
        RocketState.Flying newState = (RocketState.Flying)state;
        assert(newState.getSpeed() == 100);
        assert(newState.getType().equals(type));
        assert(newState.getMission().equals(mission2));
        assert(newState.getChannel().equals(uuid));
    }

    @Test
    public void explodedReasonIsUnchanged() {
        RocketState state = RocketState
                .initial(uuid, new DomainMessageDetails.RocketLaunched(type, 100, mission1))
                .receiveMessage(new RocketExploded(reason1))
                .receiveMessage(new RocketExploded(reason2));
        RocketState.Exploded newState = (RocketState.Exploded)state;
        assert(newState.getReason().equals(reason1));
        assert(newState.getType().equals(type));
        assert(newState.getMission().equals(mission1));
        assert(newState.getChannel().equals(uuid));
    }
}

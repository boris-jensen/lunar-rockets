package com.lunar.rockets.domain.service;

import com.lunar.rockets.domain.objects.DomainMessage;
import com.lunar.rockets.domain.objects.RocketState;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;
import com.lunar.rockets.domain.objects.DomainMessageDetails.RocketLaunched;
import com.lunar.rockets.domain.objects.DomainMessageDetails.RocketSpeedIncreased;
import com.lunar.rockets.domain.objects.DomainMessageDetails.RocketSpeedDecreased;
import com.lunar.rockets.domain.objects.DomainMessageDetails.RocketExploded;
import com.lunar.rockets.domain.objects.DomainMessageDetails.RocketMissionChanged;
import com.lunar.rockets.domain.objects.RocketState.Flying;
import com.lunar.rockets.domain.objects.RocketState.Exploded;

public class RocketServiceTest {

    UUID uuid1 = UUID.randomUUID();
    UUID uuid2 = UUID.randomUUID();
    UUID uuid3 = UUID.randomUUID();
    String type = "type";
    String mission1 = "mission1";
    String mission2 = "mission2";
    String reason1 = "reason1";
    String reason2 = "reason2";

    @Test
    public void serviceHandlesMultipleRockets() {
        RocketService service = new RocketService();
        service.acceptMessage(new DomainMessage<>(uuid1, 1, new RocketLaunched(type, 100, mission1)));
        service.acceptMessage(new DomainMessage<>(uuid1, 3, new RocketSpeedIncreased(50)));
        service.acceptMessage(new DomainMessage<>(uuid1, 3, new RocketSpeedIncreased(50)));
        service.acceptMessage(new DomainMessage<>(uuid1, 2, new RocketMissionChanged(mission2)));
        service.acceptMessage(new DomainMessage<>(uuid1, 4, new RocketSpeedDecreased(30)));

        service.acceptMessage(new DomainMessage<>(uuid2, 1, new RocketLaunched(type, 100, mission1)));
        service.acceptMessage(new DomainMessage<>(uuid2, 3, new RocketExploded(reason1)));
        service.acceptMessage(new DomainMessage<>(uuid2, 3, new RocketExploded(reason2)));
        service.acceptMessage(new DomainMessage<>(uuid2, 2, new RocketMissionChanged(mission2)));

        service.acceptMessage(new DomainMessage<>(uuid3, 1, new RocketMissionChanged(mission2)));
        service.acceptMessage(new DomainMessage<>(uuid3, 2, new RocketSpeedIncreased(50)));
        service.acceptMessage(new DomainMessage<>(uuid3, 3, new RocketSpeedDecreased(30)));

        Optional<RocketState> rocket1Opt = service.getRocket(uuid1);
        Optional<RocketState> rocket2Opt = service.getRocket(uuid2);
        Optional<RocketState> rocket3Opt = service.getRocket(uuid3);

        assert(rocket1Opt.isPresent());
        assert(rocket2Opt.isPresent());
        assert(rocket3Opt.isEmpty());

        RocketState rocket1 = rocket1Opt.get();
        RocketState rocket2 = rocket2Opt.get();

        assert (rocket1.getType().equals(type));
        assert (rocket1.getMission().equals(mission2));
        assert (rocket1.getChannel().equals(uuid1));
        assert(rocket1 instanceof Flying);
        assert (((Flying)rocket1).getSpeed() == 120);

        assert (rocket2.getType().equals(type));
        assert (rocket2.getMission().equals(mission2));
        assert (rocket2.getChannel().equals(uuid2));
        assert(rocket2 instanceof Exploded);
        assert (((Exploded)rocket2).getReason().equals(reason1));
    }
}

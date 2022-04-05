package com.lunar.rockets.domain.objects;

public sealed interface DomainMessageDetails permits
    DomainMessageDetails.RocketLaunched,
    DomainMessageDetails.RocketSpeedIncreased,
    DomainMessageDetails.RocketSpeedDecreased,
    DomainMessageDetails.RocketExploded,
    DomainMessageDetails.RocketMissionChanged {

    record RocketLaunched(String type, int launchSpeed, String mission) implements DomainMessageDetails {}

    record RocketSpeedIncreased(int by) implements DomainMessageDetails {}

    record RocketSpeedDecreased(int by) implements DomainMessageDetails {}

    record RocketExploded(String reason) implements DomainMessageDetails {}

    record RocketMissionChanged(String newMission) implements DomainMessageDetails {}
}

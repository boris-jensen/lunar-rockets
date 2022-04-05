package com.lunar.rockets.domain.objects;

import java.util.UUID;

public record DomainMessage<T extends DomainMessageDetails>(UUID rocketID, int messageNumber, T details) {}

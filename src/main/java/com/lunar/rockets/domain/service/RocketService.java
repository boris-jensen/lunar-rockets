package com.lunar.rockets.domain.service;

import com.lunar.rockets.domain.objects.DomainMessage;
import com.lunar.rockets.domain.objects.RocketState;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class RocketService {
    ConcurrentMap<UUID, RocketState> rockets = new ConcurrentHashMap<>();
    MessageQueue queue = new MessageQueue();

    synchronized public void acceptMessage(DomainMessage<?> message) {
        queue.enqueueMessage(message);
        queue.consumeDistinctMessagesInOrder(message.rocketID(),
            (msg) -> rockets.compute(msg.rocketID(), (k, state) -> (state == null)
                ? RocketState.initial(msg.rocketID(), msg.details())
                : state.receiveMessage(msg.details())));
    }

    public List<RocketState> getRockets() {
        return rockets
            .values()
            .stream()
            .toList();
    }

    public Optional<RocketState> getRocket(UUID rocketId) {
        return Optional.ofNullable(rockets.get(rocketId));
    }
}

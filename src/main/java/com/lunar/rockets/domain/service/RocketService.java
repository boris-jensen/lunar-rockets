package com.lunar.rockets.domain.service;

import com.lunar.rockets.domain.objects.DomainMessage;
import com.lunar.rockets.domain.objects.RocketState;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class RocketService {
    ConcurrentMap<UUID, RocketState> rockets = new ConcurrentHashMap<>();
    ConcurrentMap<UUID, CounterAndQueue> rocketMessages = new ConcurrentHashMap<>();

    synchronized public void acceptMessage(DomainMessage<?> message) {
        enqueueMessage(message);
        consumeMessages(message.rocketID());
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

    static Comparator<DomainMessage<?>> domainMessageComparator = Comparator.comparing(DomainMessage::messageNumber);

    public void enqueueMessage(DomainMessage<?> message) {
        CounterAndQueue cq = rocketMessages.get(message.rocketID());
        if (cq == null) {
            PriorityQueue<DomainMessage<?>> queue = new PriorityQueue<>(domainMessageComparator.reversed());
            queue.add(message);
            cq = new CounterAndQueue(1, queue);
            rocketMessages.put(message.rocketID(), cq);
        } else {
            cq.queue.add(message);
        }
    }

    public void consumeMessages(UUID rocketId) {
        CounterAndQueue cq = rocketMessages.get(rocketId);
        while (!cq.queue.isEmpty() && cq.queue.peek().messageNumber() < cq.nextMessageToConsume) {
            cq.queue.remove();
        }
        while (!cq.queue.isEmpty() && cq.queue.peek().messageNumber() == cq.nextMessageToConsume) {
            consumeMessage(cq.queue.remove());
            cq = new CounterAndQueue(cq.nextMessageToConsume + 1, cq.queue);
        }
        rocketMessages.put(rocketId, cq);
    }

    public void consumeMessage(DomainMessage<?> message) {
        rockets.compute(message.rocketID(), (k, state) -> (state == null)
                ? RocketState.initial(message)
                : state.receiveMessage(message.details()));
    }

    @Value
    public static class CounterAndQueue {
        int nextMessageToConsume;
        PriorityQueue<DomainMessage<?>> queue;

        public CounterAndQueue withMessage(DomainMessage<?> message) {
            queue.add(message);
            return this;
        }
    }
}

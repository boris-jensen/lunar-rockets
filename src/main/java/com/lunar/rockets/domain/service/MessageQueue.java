package com.lunar.rockets.domain.service;

import com.lunar.rockets.domain.objects.DomainMessage;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/*
 * Accepts messages and provides them to a consumer for a channel in order of message number, discarding duplicate messages per channel.
 */
public class MessageQueue {

    ConcurrentMap<UUID, CounterAndQueue> rocketMessages = new ConcurrentHashMap<>();

    static Comparator<DomainMessage<?>> domainMessageComparator = Comparator.comparing(DomainMessage::messageNumber);

    public void enqueueMessage(DomainMessage<?> message) {
        CounterAndQueue cq = rocketMessages.get(message.rocketID());
        if (cq == null) {
            PriorityQueue<DomainMessage<?>> queue = new PriorityQueue<>(domainMessageComparator);
            queue.add(message);
            cq = new CounterAndQueue(1, queue);
            rocketMessages.put(message.rocketID(), cq);
        } else {
            cq.queue.add(message);
        }
    }

    public void consumeDistinctMessagesInOrder(UUID channel, Consumer<DomainMessage<?>> consumer) {
        CounterAndQueue cq = rocketMessages.get(channel);
        while (!cq.queue.isEmpty() && cq.queue.peek().messageNumber() < cq.nextMessageToConsume()) {
            cq.queue.remove();
        }
        while (!cq.queue.isEmpty() && cq.queue.peek().messageNumber() == cq.nextMessageToConsume()) {
            consumer.accept(cq.queue.remove());
            while(!cq.queue.isEmpty() && cq.queue.peek().messageNumber() == cq.nextMessageToConsume()) {
                cq.queue.remove();
            }
            cq = new CounterAndQueue(cq.nextMessageToConsume + 1, cq.queue);
            rocketMessages.put(channel, cq);
        }
    }

    public record CounterAndQueue(int nextMessageToConsume,
                                  PriorityQueue<DomainMessage<?>> queue) { }
}

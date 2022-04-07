package com.lunar.rockets.domain.service;

import com.lunar.rockets.domain.objects.DomainMessage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class MessageQueueTest {

    UUID uuid = UUID.randomUUID();

    @Test
    public void messageQueueConsumesMessagesInOrder() {
        MessageQueue queue = new MessageQueue();
        queue.enqueueMessage(new DomainMessage<>(uuid, 2, null));
        queue.enqueueMessage(new DomainMessage<>(uuid, 3, null));
        queue.enqueueMessage(new DomainMessage<>(uuid, 1, null));
        List<Integer> messageNumbers = new ArrayList<>();
        Consumer<DomainMessage<?>> consumer = (msg) -> messageNumbers.add(msg.messageNumber());
        queue.consumeDistinctMessagesInOrder(uuid, consumer);
        assert (messageNumbers.get(0) == 1);
        assert (messageNumbers.get(1) == 2);
        assert (messageNumbers.get(2) == 3);
    }

    @Test
    public void messageQueueConsumesDistinctMessages() {
        MessageQueue queue = new MessageQueue();
        queue.enqueueMessage(new DomainMessage<>(uuid, 2, null));
        queue.enqueueMessage(new DomainMessage<>(uuid, 2, null));
        queue.enqueueMessage(new DomainMessage<>(uuid, 3, null));
        queue.enqueueMessage(new DomainMessage<>(uuid, 1, null));
        queue.enqueueMessage(new DomainMessage<>(uuid, 1, null));
        List<Integer> messageNumbers = new ArrayList<>();
        Consumer<DomainMessage<?>> consumer = (msg) -> messageNumbers.add(msg.messageNumber());
        queue.consumeDistinctMessagesInOrder(uuid, consumer);
        assert (messageNumbers.get(0) == 1);
        assert (messageNumbers.get(1) == 2);
        assert (messageNumbers.get(2) == 3);
    }
}

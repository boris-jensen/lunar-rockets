package com.lunar.rockets.controller.web.dto.message.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.lunar.rockets.controller.web.dto.message.Message;
import com.lunar.rockets.controller.web.dto.message.Message.RocketLaunchedMessage;
import com.lunar.rockets.controller.web.dto.message.Message.RocketSpeedIncreasedMessage;
import com.lunar.rockets.controller.web.dto.message.Message.RocketSpeedDecreasedMessage;
import com.lunar.rockets.controller.web.dto.message.Message.RocketExplodedMessage;
import com.lunar.rockets.controller.web.dto.message.Message.RocketMissionChangedMessage;
import com.lunar.rockets.controller.web.dto.message.MessageType;
import com.lunar.rockets.controller.web.dto.message.MessageWrapper;
import com.lunar.rockets.controller.web.dto.message.MessageWrapper.RocketLaunchedWrapper;
import com.lunar.rockets.controller.web.dto.message.MessageWrapper.RocketSpeedIncreasedWrapper;
import com.lunar.rockets.controller.web.dto.message.MessageWrapper.RocketSpeedDecreasedWrapper;
import com.lunar.rockets.controller.web.dto.message.MessageWrapper.RocketExplodedWrapper;
import com.lunar.rockets.controller.web.dto.message.MessageWrapper.RocketMissionChangedWrapper;
import com.lunar.rockets.controller.web.dto.message.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageWrapperDeserializer extends StdDeserializer<MessageWrapper<?>> {

    @Autowired ObjectMapper mapper;

    public MessageWrapperDeserializer() {
        this(null);
    }

    public MessageWrapperDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public MessageWrapper<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode wrapperNode = p.getCodec().readTree(p);
        JsonNode metadataNode = wrapperNode.get("metadata");
        JsonNode messageNode = wrapperNode.get("message");

        Metadata metadata = mapper.treeToValue(metadataNode, Metadata.class);
        Message message = getMessage(messageNode, metadata.getMessageType());

        return getMessageWrapper(metadata, message);
    }

    private Message getMessage(JsonNode messageNode, MessageType messageType) throws JsonProcessingException {
        Class<? extends Message> messageClass = switch (messageType) {
            case ROCKET_LAUNCHED -> RocketLaunchedMessage.class;
            case ROCKET_SPEED_INCREASED -> RocketSpeedIncreasedMessage.class;
            case ROCKET_SPEED_DECREASED -> Message.RocketSpeedDecreasedMessage.class;
            case ROCKET_EXPLODED -> Message.RocketExplodedMessage.class;
            case ROCKET_MISSION_CHANGED -> Message.RocketMissionChangedMessage.class;
        };
        return mapper.readValue(messageNode.toString(), messageClass);
    }

    private MessageWrapper<?> getMessageWrapper(Metadata metadata, Message message) {
        if (message instanceof RocketLaunchedMessage rocketLaunchedMessage) {
            return new RocketLaunchedWrapper(metadata, rocketLaunchedMessage);
        } else if (message instanceof RocketSpeedIncreasedMessage rocketSpeedIncreasedMessage) {
            return new RocketSpeedIncreasedWrapper(metadata, rocketSpeedIncreasedMessage);
        } else if (message instanceof RocketSpeedDecreasedMessage rocketSpeedDecreasedMessage) {
            return new RocketSpeedDecreasedWrapper(metadata, rocketSpeedDecreasedMessage);
        } else if (message instanceof RocketExplodedMessage rocketExplodedMessage) {
            return new RocketExplodedWrapper(metadata, rocketExplodedMessage);
        } else if (message instanceof RocketMissionChangedMessage rocketMissionChangedMessage) {
            return new RocketMissionChangedWrapper(metadata, rocketMissionChangedMessage);
        } else {
            throw new RuntimeException("Unknown message type: " + message.getClass().getCanonicalName());
        }
    }
}

package com.lunar.rockets.controller.web.dto.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lunar.rockets.controller.web.dto.message.deserializer.MessageWrapperDeserializer;
import com.lunar.rockets.domain.objects.DomainMessageDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.lunar.rockets.controller.web.dto.message.Message.RocketLaunchedMessage;
import com.lunar.rockets.controller.web.dto.message.Message.RocketSpeedIncreasedMessage;
import com.lunar.rockets.controller.web.dto.message.Message.RocketSpeedDecreasedMessage;
import com.lunar.rockets.controller.web.dto.message.Message.RocketExplodedMessage;
import com.lunar.rockets.controller.web.dto.message.Message.RocketMissionChangedMessage;

@Data
@JsonDeserialize(using = MessageWrapperDeserializer.class)
@AllArgsConstructor
public sealed abstract class MessageWrapper<T extends Message> permits
    MessageWrapper.RocketLaunchedWrapper,
    MessageWrapper.RocketSpeedIncreasedWrapper,
    MessageWrapper.RocketSpeedDecreasedWrapper,
    MessageWrapper.RocketExplodedWrapper,
    MessageWrapper.RocketMissionChangedWrapper {

    private Metadata metadata;
    private T message;

    // Need to generate explicit constructors, since lombok constructors don't call super
    public final static class RocketLaunchedWrapper extends MessageWrapper<RocketLaunchedMessage> {
        public RocketLaunchedWrapper(Metadata metadata, RocketLaunchedMessage message) {
            super(metadata, message);
        }
    }

    public final static class RocketSpeedIncreasedWrapper extends MessageWrapper<RocketSpeedIncreasedMessage> {
        public RocketSpeedIncreasedWrapper(Metadata metadata, RocketSpeedIncreasedMessage message) {
            super(metadata, message);
        }
    }

    public final static class RocketSpeedDecreasedWrapper extends MessageWrapper<Message.RocketSpeedDecreasedMessage> {
        public RocketSpeedDecreasedWrapper(Metadata metadata, RocketSpeedDecreasedMessage message) {
            super(metadata, message);
        }
    }

    public final static class RocketExplodedWrapper extends MessageWrapper<Message.RocketExplodedMessage> {
        public RocketExplodedWrapper(Metadata metadata, RocketExplodedMessage message) {
            super(metadata, message);
        }
    }

    public final static class RocketMissionChangedWrapper extends MessageWrapper<Message.RocketMissionChangedMessage> {
        public RocketMissionChangedWrapper(Metadata metadata, RocketMissionChangedMessage message) {
            super(metadata, message);
        }
    }
}

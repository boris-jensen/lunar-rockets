package com.lunar.rockets.controller.web.dto.message;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class Metadata {
    private UUID channel;
    private int messageNumber;
    private ZonedDateTime messageTime;
    private MessageType messageType;
}

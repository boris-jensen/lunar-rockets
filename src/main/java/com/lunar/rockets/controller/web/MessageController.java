package com.lunar.rockets.controller.web;

import com.lunar.rockets.controller.web.dto.message.MessageWrapper;
import com.lunar.rockets.domain.objects.DomainMessage;
import com.lunar.rockets.domain.objects.DomainMessageDetails;
import com.lunar.rockets.domain.service.RocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final RocketService rocketService;

    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<Void> postMessage(@RequestBody MessageWrapper<?> wrapper) {
        DomainMessageDetails details = toDomainMessage(wrapper);
        DomainMessage<?> domainMessage = new DomainMessage<>(wrapper.getMetadata().getChannel(), wrapper.getMetadata().getMessageNumber(), details);
        rocketService.acceptMessage(domainMessage);
        return ResponseEntity.ok().build();
    }

    private DomainMessageDetails toDomainMessage(MessageWrapper<?> wrapper) {
        if (wrapper instanceof MessageWrapper.RocketLaunchedWrapper rocketLaunchedWrapper) {
            return new DomainMessageDetails.RocketLaunched(
                rocketLaunchedWrapper.getMessage().getType(),
                rocketLaunchedWrapper.getMessage().getLaunchSpeed(),
                rocketLaunchedWrapper.getMessage().getMission()
            );
        } else if (wrapper instanceof MessageWrapper.RocketSpeedIncreasedWrapper rocketSpeedIncreasedWrapper) {
            return new DomainMessageDetails.RocketSpeedDecreased(
                rocketSpeedIncreasedWrapper.getMessage().getBy()
            );
        } else if (wrapper instanceof MessageWrapper.RocketSpeedDecreasedWrapper rocketSpeedDecreasedWrapper) {
            return new DomainMessageDetails.RocketSpeedDecreased(
                rocketSpeedDecreasedWrapper.getMessage().getBy()
            );
        } else if (wrapper instanceof MessageWrapper.RocketExplodedWrapper rocketExplodedWrapper) {
            return new DomainMessageDetails.RocketExploded(
                rocketExplodedWrapper.getMessage().getReason()
            );
        } else if (wrapper instanceof MessageWrapper.RocketMissionChangedWrapper rocketMissionChangedWrapper) {
            return new DomainMessageDetails.RocketMissionChanged(
                rocketMissionChangedWrapper.getMessage().getNewMission()
            );
        }
        throw new RuntimeException("Unknown message wrapper type");
    }
}

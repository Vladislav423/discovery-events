package com.practice.mainservice.request.dto;

import com.practice.mainservice.request.entity.ParticipationRequest;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper {
    public ParticipationRequestDto toDto(ParticipationRequest participationRequest) {
        ParticipationRequestDto requestDto = new ParticipationRequestDto();

        requestDto.setId(participationRequest.getId());
        requestDto.setCreated(participationRequest.getCreated());
        requestDto.setStatus(participationRequest.getStatus());
        requestDto.setRequester(participationRequest.getRequester().getId());
        requestDto.setEvent(participationRequest.getEvent().getId());

        return requestDto;
    }
}

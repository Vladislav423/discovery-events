package com.practice.mainservice.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.practice.mainservice.request.entity.RequestStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ParticipationRequestDto {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    private RequestStatus status;

    private Long requester;

    private Long event;
}

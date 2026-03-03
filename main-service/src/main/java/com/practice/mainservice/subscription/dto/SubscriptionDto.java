package com.practice.mainservice.subscription.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.practice.mainservice.user.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubscriptionDto {
    private Long id;

    private Long follower;

    private Long followed;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
}

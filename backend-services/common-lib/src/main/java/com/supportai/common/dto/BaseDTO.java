package com.supportai.common.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public abstract class BaseDTO {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private Long version;
}
package com.home.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
public final class Book implements Serializable {
    private UUID id;
    private String name;
    private String format;
    private Cover cover;
}

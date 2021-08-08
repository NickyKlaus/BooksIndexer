package com.home.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public final class Cover implements Serializable {
    private byte[] image;
    private String format;
}

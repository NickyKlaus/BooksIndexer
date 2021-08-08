package com.home.model;

import lombok.Data;

import java.io.Serializable;

@Data
public final class Cover implements Serializable {
    private byte[] binaryData;
    private String format;
}

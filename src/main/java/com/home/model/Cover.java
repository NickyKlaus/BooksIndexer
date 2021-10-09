package com.home.model;

import java.io.Serializable;
import java.util.Arrays;

public final class Cover implements Serializable {
    private final byte[] image;
    private final String format;

    Cover(byte[] image, String format) {
        this.image = image;
        this.format = format;
    }

    public static Cover.CoverBuilder builder() {
        return new Cover.CoverBuilder();
    }

    public byte[] getImage() {
        return this.image;
    }

    public String getFormat() {
        return this.format;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Cover)) {
            return false;
        } else {
            Cover other = (Cover)o;
            if (!Arrays.equals(this.getImage(), other.getImage())) {
                return false;
            } else {
                Object currentFormat = this.getFormat();
                Object otherFormat = other.getFormat();
                if (currentFormat == null) {
                    return otherFormat == null;
                } else {
                    return currentFormat.equals(otherFormat);
                }
            }
        }
    }

    public int hashCode() {
        int result = 1;
        result = result * 59 + Arrays.hashCode(this.getImage());
        result = result * 59 + (this.getFormat() == null ? 43 : this.getFormat().hashCode());
        return result;
    }

    public String toString() {
        return "Cover(image=" + Arrays.toString(this.getImage()) + ", format=" + this.getFormat() + ")";
    }

    public static class CoverBuilder {
        private byte[] image;
        private String format;

        CoverBuilder() {
        }

        public Cover.CoverBuilder image(byte[] image) {
            this.image = image;
            return this;
        }

        public Cover.CoverBuilder format(String format) {
            this.format = format;
            return this;
        }

        public Cover build() {
            return new Cover(this.image, this.format);
        }

        public String toString() {
            return "Cover.CoverBuilder(image=" + Arrays.toString(this.image) + ", format=" + this.format + ")";
        }
    }
}

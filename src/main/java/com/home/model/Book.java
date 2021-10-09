package com.home.model;

import java.io.Serializable;
import java.util.UUID;

public final class Book implements Serializable {
    private final UUID id;
    private final String name;
    private final String format;
    private final Cover cover;
    private final String text;

    Book(UUID id, String name, String format, Cover cover, String text) {
        this.id = id;
        this.name = name;
        this.format = format;
        this.cover = cover;
        this.text = text;
    }

    public static Book.BookBuilder builder() {
        return new Book.BookBuilder();
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getFormat() {
        return this.format;
    }

    public Cover getCover() {
        return this.cover;
    }

    public String getText() {
        return this.text;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Book)) {
            return false;
        } else {
            Book other = (Book)o;
            Object currentId = this.getId();
            Object otherId = other.getId();
            if (currentId == null) {
                if (otherId != null) {
                    return false;
                }
            } else if (!currentId.equals(otherId)) {
                return false;
            }

            name_eq: {
                Object currentName = this.getName();
                Object otherName = other.getName();
                if (currentName == null) {
                    if (otherName == null) {
                        break name_eq;
                    }
                } else if (currentName.equals(otherName)) {
                    break name_eq;
                }

                return false;
            }

            format_eq: {
                Object currentFormat = this.getFormat();
                Object otherFormat = other.getFormat();
                if (currentFormat == null) {
                    if (otherFormat == null) {
                        break format_eq;
                    }
                } else if (currentFormat.equals(otherFormat)) {
                    break format_eq;
                }

                return false;
            }

            Object currentCover = this.getCover();
            Object otherCover = other.getCover();
            if (currentCover == null) {
                if (otherCover != null) {
                    return false;
                }
            } else if (!currentCover.equals(otherCover)) {
                return false;
            }

            Object currentText = this.getText();
            Object otherText = other.getText();
            if (currentText == null) {
                return otherText == null;
            } else {
                return currentText.equals(otherText);
            }
        }
    }

    public int hashCode() {
        int result = 1;
        result = result * 59 + (this.getId() == null ? 43 : this.getId().hashCode());
        result = result * 59 + (this.getName() == null ? 43 : this.getName().hashCode());
        result = result * 59 + (this.getFormat() == null ? 43 : this.getFormat().hashCode());
        result = result * 59 + (this.getCover() == null ? 43 : this.getCover().hashCode());
        result = result * 59 + (this.getText() == null ? 43 : this.getText().hashCode());
        return result;
    }

    public String toString() {
        return "Book(id=" + this.getId() + ", " +
                "name=" + this.getName() + ", " +
                "format=" + this.getFormat() + ", " +
                "cover=" + this.getCover() + ", " +
                "text=" + this.getText() + ")";
    }

    public static class BookBuilder {
        private UUID id;
        private String name;
        private String format;
        private Cover cover;
        private String text;

        BookBuilder() {
        }

        public Book.BookBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public Book.BookBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Book.BookBuilder format(String format) {
            this.format = format;
            return this;
        }

        public Book.BookBuilder cover(Cover cover) {
            this.cover = cover;
            return this;
        }

        public Book.BookBuilder text(String text) {
            this.text = text;
            return this;
        }

        public Book build() {
            return new Book(this.id, this.name, this.format, this.cover, this.text);
        }

        public String toString() {
            return "Book.BookBuilder(id=" + this.id + ", name=" + this.name + ", format=" + this.format + ", cover=" + this.cover + ", text=" + this.text + ")";
        }
    }
}

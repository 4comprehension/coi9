package com.pivovarit.movies.api;

import java.util.Objects;

public class MovieAddRequest {
    private final long id;
    private final String title;
    private final String type;

    public MovieAddRequest(long id, String title, String type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieAddRequest that = (MovieAddRequest) o;
        return id == that.id &&
          Objects.equals(title, that.title) &&
          Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, type);
    }

    @Override
    public String toString() {
        return "MovieAddRequest{" +
          "id=" + id +
          ", title='" + title + '\'' +
          ", type='" + type + '\'' +
          '}';
    }
}

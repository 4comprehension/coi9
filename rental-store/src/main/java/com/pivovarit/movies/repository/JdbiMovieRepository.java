package com.pivovarit.movies.repository;

import com.pivovarit.movies.domain.Movie;
import com.pivovarit.movies.domain.MovieId;
import com.pivovarit.movies.domain.MovieType;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Optional;

public class JdbiMovieRepository implements MovieRepository {

    private final Jdbi jdbi;

    public JdbiMovieRepository(DataSource dataSource) {
        this.jdbi = Jdbi.create(dataSource);
    }

    @Override
    public MovieId save(Movie movie) {
        jdbi.useHandle(handle -> handle
          .createUpdate("""
            INSERT INTO movies (id, title, type) VALUES (:id, :title, :type)
            """)
          .bind("id", movie.id().id())
          .bind("title", movie.title())
          .bind("type", movie.type().name())
          .execute());
        return movie.id();
    }

    @Override
    public Collection<Movie> findAll() {
        return jdbi.withHandle(handle -> handle
          .createQuery("SELECT * FROM movies")
          .map(toMovie())
          .list());
    }

    @Override
    public Optional<Movie> findByTitle(String title) {
        return jdbi.withHandle(handle -> handle
          .createQuery("SELECT * FROM movies WHERE title = :title")
          .bind("title", title)
          .map(toMovie())
          .findFirst());
    }

    @Override
    public Optional<Movie> findById(MovieId id) {
        return jdbi.withHandle(handle -> handle
          .createQuery("SELECT * FROM movies WHERE id = :id")
          .bind("id", id.id())
          .map(toMovie())
          .findFirst());
    }

    private static RowMapper<Movie> toMovie() {
        return (rs, _) -> new Movie(
          new MovieId(rs.getLong("id")),
          rs.getString("title"),
          MovieType.valueOf(rs.getString("type")));
    }
}

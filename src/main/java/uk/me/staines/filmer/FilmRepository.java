package uk.me.staines.filmer;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface FilmRepository {

    Optional<Film> findById(@NotNull Long id);
    Optional<Film> findByImdbId(@NotNull String id);

    Film save(@NotNull FilmDetails details);

    void deleteById(@NotNull Long id);

    List<Film> find(@NotNull ListArguments args);
    long count(@NotNull ListArguments args);
    int update(@NotNull Film details);

}
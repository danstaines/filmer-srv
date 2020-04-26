package uk.me.staines.filmer;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    Optional<Film> findById(@NotNull Long id);

    Film save(@NotNull FilmDetails details);

    void deleteById(@NotNull Long id);

    List<FilmDetails> findAll(@NotNull SortingAndOrderArguments args);

    int update(@NotNull Film details);

}
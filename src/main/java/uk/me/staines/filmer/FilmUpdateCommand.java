package uk.me.staines.filmer;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotNull;

@Introspected
public class FilmUpdateCommand {
    @NotNull
    private Long id;

    @NotNull
    private FilmDetails filmDetails;


    public FilmUpdateCommand() {
    }

    public FilmUpdateCommand(Long id, FilmDetails details) {
        this.id = id;
        this.filmDetails = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FilmDetails getFilmDetails() {
        return filmDetails;
    }

    public void setFilmDetails(FilmDetails filmDetails) {
        this.filmDetails = filmDetails;
    }
}
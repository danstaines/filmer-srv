package uk.me.staines.filmer;


import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotBlank;

@Introspected
public class FilmSaveCommand {

    @NotBlank
    private String name;
    @NotBlank
    private String imdbId;

    public FilmSaveCommand() {
    }

    public FilmSaveCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbdId(String imdbId) {
        this.imdbId = imdbId;
    }
}
package uk.me.staines.filmer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public class FilmDetails {

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "imdb_id", nullable = false)
    private String imdbId;

    public FilmDetails() {
    }

    public FilmDetails( @NotNull String name, @NotNull String imdbId) {
        this.name = name;
        this.imdbId = imdbId;
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

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }


}

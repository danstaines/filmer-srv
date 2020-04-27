package uk.me.staines.filmer.omdb;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.me.staines.filmer.FilmDetails;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

/**
 * Separate class to deal with
 */
public class OmdbFilmDetails {

    @NotNull
    @Column(name = "title", nullable = false, unique = true)
    @JsonProperty("title")
    @JsonAlias("Title")
    protected String title;

    @NotNull
    @Column(name = "imdb_id", nullable = false, unique = true)
    @JsonProperty("imdb_id")
    @JsonAlias("imdbID")
    protected String imdbId;

    @NotNull
    @Column(name = "year")
    @JsonProperty("year")
    @JsonAlias("Year")
    protected String year;

    @NotNull
    @Column(name = "runtime")
    @JsonProperty("runtime")
    @JsonAlias("Runtime")
    protected String runTime;

    public OmdbFilmDetails() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public FilmDetails toFilmDetails() {
        return new FilmDetails(this.title, this.imdbId, toInt(this.year), toInt(this.runTime));
    }

    private int toInt(String s) {
        if(s == null) {
            return 0;
        } else {
            return Integer.parseInt(s.replaceAll("[^0-9]+", ""));
        }
    }

}

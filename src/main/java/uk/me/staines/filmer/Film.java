package uk.me.staines.filmer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "film")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Film extends FilmDetails {

    enum Location {
        DVD, DVD_ARCHIVE, NETFLIX, NAS, NOT_OWNED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Boolean watched = false;

    private Location location = Location.DVD;

    private Boolean favourite = false;

    public Film() {
        super();
    }

    public Film(@NotNull FilmDetails filmDetails) {
        super(filmDetails.getTitle(), filmDetails.getImdbId(),
                filmDetails.getYear(), filmDetails.getRuntime());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", watched=" + watched +
                ", favourite=" + favourite +
                ", location=" + location +
                ", title='" + title + '\'' +
                ", imdbId='" + imdbId + '\'' +
                ", year='" + year + '\'' +
                ", runTime='" + runtime + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Film film = (Film) o;
        return id.equals(film.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    public String toCsv() {
        return String.join(",", toStr(this.id), toStr(this.imdbId), escape(this.title),
                toStr(this.year), toStr(this.runtime), toStr(this.location),
                toStr(this.watched), toStr(this.favourite));
    }

    private static String toStr(Object o) {
        return o == null ? "" : o.toString();
    }

    private static String escape(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    public Boolean getWatched() {
        return watched;
    }

    public void setWatched(Boolean watched) {
        this.watched = watched;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }
}

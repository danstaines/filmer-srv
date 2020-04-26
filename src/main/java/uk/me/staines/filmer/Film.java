package uk.me.staines.filmer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "film")
public class Film extends FilmDetails {

    enum Location {
        DVD,NETFLIX,NAS;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Boolean watched = false;

    private Location location = Location.DVD;

    public Film() {
        super();
    }

    public Film(@NotNull FilmDetails filmDetails) {
        super(filmDetails.getTitle(), filmDetails.getImdbId(),
                filmDetails.getYear(), filmDetails.getRunTime());
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
                ", location=" + location +
                ", title='" + title + '\'' +
                ", imdbId='" + imdbId + '\'' +
                ", year='" + year + '\'' +
                ", runTime='" + runTime + '\'' +
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
}

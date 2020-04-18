package uk.me.staines.filmer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "film")
public class Film extends FilmDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
}

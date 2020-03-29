package uk.me.staines.filmer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "film")
public class Film extends FilmDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Film() {
        super();
    }

    public Film(@NotNull String name, @NotNull String imdbId) {
        super(name, imdbId);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

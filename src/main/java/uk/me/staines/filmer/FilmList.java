package uk.me.staines.filmer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class FilmList {

    public final List<Film> films;
    public final long count;
    @JsonCreator
    public FilmList(@JsonProperty("films") List<Film> films,
                    @JsonProperty("count") long count) {
        this.films = films == null ? Collections.emptyList() : films;
        this.count = count;
    }

    @Override
    public String toString() {
        return "FilmList{" +
                "films=" + films +
                ", count=" + count +
                '}';
    }
}

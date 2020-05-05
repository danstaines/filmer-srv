package uk.me.staines.filmer;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilmDetails {

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
    protected int year;

    @NotNull
    @Column(name = "runtime")
    @JsonProperty("runtime")
    @JsonAlias("Runtime")
    protected int runtime;

    public FilmDetails() {
    }

    public FilmDetails(@NotNull String title, @NotNull String imdbId, @NotNull int year, @NotNull int runtime) {
        this.title = title;
        this.imdbId = imdbId;
        this.year = year;
        this.runtime = runtime;
    }

    public FilmDetails(FilmDetails details) {
        this(details.getTitle(), details.getImdbId(), details.getYear(), details.runtime);
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

    public int getYear() { return year; }

    public void setYear(int year) { this.year = year; }

    public int getRuntime() { return runtime; }

    public void setRuntime(int runTime) { this.runtime = runTime; }

    @Override
    public String toString() {
        return "FilmDetails{" +
                "title='" + title + '\'' +
                ", imdbId='" + imdbId + '\'' +
                ", year=" + year +
                ", runtime=" + runtime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilmDetails that = (FilmDetails) o;
        return Objects.equals(imdbId, that.imdbId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imdbId);
    }

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FilmDetails filmDetails = mapper.readValue("{\"Title\":\"Guardians of the Galaxy Vol. 2\",\"Year\":\"2017\",\"Rated\":\"PG-13\",\"Released\":\"05 May 2017\",\"Runtime\":\"136 min\",\"Genre\":\"Action, Adventure, Comedy, Sci-Fi\",\"Director\":\"James Gunn\",\"Writer\":\"James Gunn, Dan Abnett (based on the Marvel comics by), Andy Lanning (based on the Marvel comics by), Steve Englehart (Star-Lord created by), Steve Gan (Star-Lord created by), Jim Starlin (Gamora and Drax created by), Stan Lee (Groot created by), Larry Lieber (Groot created by), Jack Kirby (Groot created by), Bill Mantlo (Rocket Raccoon created by), Keith Giffen (Rocket Raccoon created by), Steve Gerber (Howard the Duck created by), Val Mayerik (Howard the Duck created by)\",\"Actors\":\"Chris Pratt, Zoe Saldana, Dave Bautista, Vin Diesel\",\"Plot\":\"The Guardians struggle to keep together as a team while dealing with their personal family issues, notably Star-Lord's encounter with his father the ambitious celestial being Ego.\",\"Language\":\"English\",\"Country\":\"USA\",\"Awards\":\"Nominated for 1 Oscar. Another 14 wins & 52 nominations.\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BNjM0NTc0NzItM2FlYS00YzEwLWE0YmUtNTA2ZWIzODc2OTgxXkEyXkFqcGdeQXVyNTgwNzIyNzg@._V1_SX300.jpg\",\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"7.6/10\"},{\"Source\":\"Rotten Tomatoes\",\"Value\":\"85%\"},{\"Source\":\"Metacritic\",\"Value\":\"67/100\"}],\"Metascore\":\"67\",\"imdbRating\":\"7.6\",\"imdbVotes\":\"531,435\",\"imdbID\":\"tt3896198\",\"Type\":\"movie\",\"DVD\":\"22 Aug 2017\",\"BoxOffice\":\"$389,804,217\",\"Production\":\"Walt Disney Pictures\",\"Website\":\"N/A\",\"Response\":\"True\"}", FilmDetails.class);
        System.out.println(filmDetails);
    }
}

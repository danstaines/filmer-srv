package uk.me.staines.filmer.omdb;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.common.collect.ImmutableMap;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.me.staines.filmer.FilmDetails;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings({"unchecked", "OptionalGetWithoutIsPresent"})
@MicronautTest
public class OmdbControllerTests {

    final Logger log = LoggerFactory.getLogger(OmdbClientTests.class);
    private final String testKey = "xyz";
    private WireMockServer wireMockServer;

    @BeforeEach
    public void startWireMock() {
        wireMockServer = new WireMockServer(wireMockConfig().port(8888));
        wireMockServer.start();
        log.info("Started wiremock on " + wireMockServer.port());
        configureFor(this.wireMockServer.port());
    }

    @AfterEach
    public void stopWireMock() {
        wireMockServer.stop();
    }

    @Inject
    @Client("/omdb")
    RxHttpClient client;

    @Test
    public void testPing() {

        HttpRequest<String> request = HttpRequest.GET("/ping");
        Map<String, String> body = client.toBlocking().retrieve(request, Map.class);
        assertNotNull(body);
        System.out.println(body);
    }

    @Test
    public void testId() {
        String id = "tt3896198";
        String json = "{\"Title\":\"Guardians of the Galaxy Vol. 2\",\"Year\":\"2017\",\"Rated\":\"PG-13\",\"Released\":\"05 May 2017\",\"Runtime\":\"136 min\",\"Genre\":\"Action, Adventure, Comedy, Sci-Fi\",\"Director\":\"James Gunn\",\"Writer\":\"James Gunn, Dan Abnett (based on the Marvel comics by), Andy Lanning (based on the Marvel comics by), Steve Englehart (Star-Lord created by), Steve Gan (Star-Lord created by), Jim Starlin (Gamora and Drax created by), Stan Lee (Groot created by), Larry Lieber (Groot created by), Jack Kirby (Groot created by), Bill Mantlo (Rocket Raccoon created by), Keith Giffen (Rocket Raccoon created by), Steve Gerber (Howard the Duck created by), Val Mayerik (Howard the Duck created by)\",\"Actors\":\"Chris Pratt, Zoe Saldana, Dave Bautista, Vin Diesel\",\"Plot\":\"The Guardians struggle to keep together as a team while dealing with their personal family issues, notably Star-Lord's encounter with his father the ambitious celestial being Ego.\",\"Language\":\"English\",\"Country\":\"USA\",\"Awards\":\"Nominated for 1 Oscar. Another 14 wins & 52 nominations.\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BNjM0NTc0NzItM2FlYS00YzEwLWE0YmUtNTA2ZWIzODc2OTgxXkEyXkFqcGdeQXVyNTgwNzIyNzg@._V1_SX300.jpg\",\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"7.6/10\"},{\"Source\":\"Rotten Tomatoes\",\"Value\":\"85%\"},{\"Source\":\"Metacritic\",\"Value\":\"67/100\"}],\"Metascore\":\"67\",\"imdbRating\":\"7.6\",\"imdbVotes\":\"531,435\",\"imdbID\":\"tt3896198\",\"Type\":\"movie\",\"DVD\":\"22 Aug 2017\",\"BoxOffice\":\"$389,804,217\",\"Production\":\"Walt Disney Pictures\",\"Website\":\"N/A\",\"Response\":\"True\"}";
        stubFor(get(urlPathEqualTo("/"))
                .withQueryParams(ImmutableMap.of("apikey", equalTo(testKey), "i", equalTo(id)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(json)));
        HttpRequest<String> request = HttpRequest.GET("/find/" + id);
        FilmDetails film = client.toBlocking().retrieve(request, FilmDetails.class);
        assertNotNull(film);
        log.info("Found: " + film);
        assertEquals("Guardians of the Galaxy Vol. 2", film.getTitle());
        assertEquals(id, film.getImdbId());
        assertEquals(136, film.getRuntime());
        assertEquals(2017, film.getYear());
    }

    @Test
    public void testSearch() {
        String searchTerm = "godfat";
        String json = "{\"Search\":[{\"Title\":\"The Godfather\",\"Year\":\"1972\",\"imdbID\":\"tt0068646\",\"Type\":\"movie\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg\"},{\"Title\":\"The Godfather: Part II\",\"Year\":\"1974\",\"imdbID\":\"tt0071562\",\"Type\":\"movie\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BMWMwMGQzZTItY2JlNC00OWZiLWIyMDctNDk2ZDQ2YjRjMWQ0XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg\"},{\"Title\":\"The Godfather: Part III\",\"Year\":\"1990\",\"imdbID\":\"tt0099674\",\"Type\":\"movie\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BNTc1YjhiNzktMjEyNS00YmNhLWExYjItZDhkNWJjZjYxOWZiXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg\"},{\"Title\":\"Tokyo Godfathers\",\"Year\":\"2003\",\"imdbID\":\"tt0388473\",\"Type\":\"movie\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BZjgyYjBiZGYtMWUyZS00NDUwLWFlZDAtMGQ2NTQzZDExOTYzXkEyXkFqcGdeQXVyNjUxMDQ0MTg@._V1_SX300.jpg\"},{\"Title\":\"The Godfather Trilogy: 1901-1980\",\"Year\":\"1992\",\"imdbID\":\"tt0150742\",\"Type\":\"movie\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BMjk5ODZjYmMtYTJjNy00MTU2LWI5OTYtYTg5YjFlMDk3ZjI0XkEyXkFqcGdeQXVyODAyNDE3Mw@@._V1_SX300.jpg\"},{\"Title\":\"3 Godfathers\",\"Year\":\"1948\",\"imdbID\":\"tt0040064\",\"Type\":\"movie\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BMTc2MGQ1YzEtZDY0OC00M2ZkLTgyOGEtMDc1ODFjZjhhOWZkXkEyXkFqcGdeQXVyNjc1NTYyMjg@._V1_SX300.jpg\"},{\"Title\":\"The Godfather Saga\",\"Year\":\"1977\",\"imdbID\":\"tt0809488\",\"Type\":\"series\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BNzk3NmZmMjgtMjA4NS00MjdkLTlkZmMtZGFkMDAyNWU4NDdlXkEyXkFqcGdeQXVyODAyNDE3Mw@@._V1_SX300.jpg\"},{\"Title\":\"Godfather of Harlem\",\"Year\":\"2019–\",\"imdbID\":\"tt8080122\",\"Type\":\"series\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BZDM0ZTNlODItOTY1MS00ODZjLWI1ZmEtZTdmN2YxYTFkYTU0XkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_SX300.jpg\"},{\"Title\":\"The Godfather\",\"Year\":\"2006\",\"imdbID\":\"tt0442674\",\"Type\":\"game\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BMTQyNTE4NzMzNF5BMl5BanBnXkFtZTgwMDgzNTY3MDE@._V1_SX300.jpg\"},{\"Title\":\"Godfather\",\"Year\":\"1991\",\"imdbID\":\"tt0353496\",\"Type\":\"movie\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BZTkyYzc5MGEtYTBiYS00ZmYyLThlZWUtOWY3ZWE4ZDhlN2MzXkEyXkFqcGdeQXVyMjM0ODk5MDU@._V1_SX300.jpg\"}],\"totalResults\":\"115\",\"Response\":\"True\"}";
        stubFor(get(urlPathEqualTo("/"))
                .withQueryParams(ImmutableMap.of("apikey", equalTo(testKey), "s", equalTo(searchTerm + "*")))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(json)));

        log.info("Searching for {}", searchTerm);
        HttpRequest<String> request = HttpRequest.GET("/search/?query=" + searchTerm);
        List<OmdbFilmDetails> films = client.toBlocking().retrieve(request, Argument.listOf(OmdbFilmDetails.class));
        log.info("Found: " + films);
        assertEquals(10, films.size());
        String id = "tt0068646";
        OmdbFilmDetails film = films.stream().filter(f -> f.getImdbId().equals(id)).findFirst().get();
        assertEquals("The Godfather", film.getTitle());
        assertEquals(id, film.getImdbId());
        assertEquals("1972", film.getYear());
    }

}

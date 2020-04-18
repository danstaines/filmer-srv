package uk.me.staines.filmer;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.common.collect.ImmutableMap;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.me.staines.filmer.omdb.OmdbClientTests;

import javax.inject.Inject;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"unchecked", "OptionalGetWithoutIsPresent"})
@MicronautTest
public class FilmControllerTests {

    final Logger log = LoggerFactory.getLogger(OmdbClientTests.class);
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
    @Client("/films")
    RxHttpClient client;

    @Test
    public void testPing() {
        HttpRequest<String> request = HttpRequest.GET("/ping");
        Map<String, String> body = client.toBlocking().retrieve(request, Map.class);
        log.info("Received ping: {}", body);
        assertNotNull(body);
        assertEquals("ok", body.get("status"));
        assertTrue(body.containsKey("timestamp"));
    }

    @Test
    public void testAdd() {
        String id = "tt3896198";
        String json = "{\"Title\":\"Guardians of the Galaxy Vol. 2\",\"Year\":\"2017\",\"Rated\":\"PG-13\",\"Released\":\"05 May 2017\",\"Runtime\":\"136 min\",\"Genre\":\"Action, Adventure, Comedy, Sci-Fi\",\"Director\":\"James Gunn\",\"Writer\":\"James Gunn, Dan Abnett (based on the Marvel comics by), Andy Lanning (based on the Marvel comics by), Steve Englehart (Star-Lord created by), Steve Gan (Star-Lord created by), Jim Starlin (Gamora and Drax created by), Stan Lee (Groot created by), Larry Lieber (Groot created by), Jack Kirby (Groot created by), Bill Mantlo (Rocket Raccoon created by), Keith Giffen (Rocket Raccoon created by), Steve Gerber (Howard the Duck created by), Val Mayerik (Howard the Duck created by)\",\"Actors\":\"Chris Pratt, Zoe Saldana, Dave Bautista, Vin Diesel\",\"Plot\":\"The Guardians struggle to keep together as a team while dealing with their personal family issues, notably Star-Lord's encounter with his father the ambitious celestial being Ego.\",\"Language\":\"English\",\"Country\":\"USA\",\"Awards\":\"Nominated for 1 Oscar. Another 14 wins & 52 nominations.\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BNjM0NTc0NzItM2FlYS00YzEwLWE0YmUtNTA2ZWIzODc2OTgxXkEyXkFqcGdeQXVyNTgwNzIyNzg@._V1_SX300.jpg\",\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"7.6/10\"},{\"Source\":\"Rotten Tomatoes\",\"Value\":\"85%\"},{\"Source\":\"Metacritic\",\"Value\":\"67/100\"}],\"Metascore\":\"67\",\"imdbRating\":\"7.6\",\"imdbVotes\":\"531,435\",\"imdbID\":\"tt3896198\",\"Type\":\"movie\",\"DVD\":\"22 Aug 2017\",\"BoxOffice\":\"$389,804,217\",\"Production\":\"Walt Disney Pictures\",\"Website\":\"N/A\",\"Response\":\"True\"}";
        String testKey = "xyz";
        stubFor(get(urlPathEqualTo("/"))
                .withQueryParams(ImmutableMap.of("apikey", equalTo(testKey), "i", equalTo(id)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(json)));
        log.info("Adding film ID " + id);
        HttpRequest<String> request = HttpRequest.POST("/add", id);
        Film film = client.toBlocking().retrieve(request, Film.class);
        log.info("Created film: {}", film);
        assertNotNull(film.getId());
        assertEquals("Guardians of the Galaxy Vol. 2", film.getTitle());
        assertEquals(id, film.getImdbId());
        assertEquals("136 min", film.getRunTime());
        assertEquals("2017", film.getYear());

        HttpRequest<String> request2 = HttpRequest.GET("/" + film.getId());
        Film film1 = client.toBlocking().retrieve(request2, Film.class);
        log.info("Retrieved film: {}", film1);
        assertEquals(film, film1);
        assertEquals("Guardians of the Galaxy Vol. 2", film1.getTitle());
        assertEquals(id, film1.getImdbId());
        assertEquals("136 min", film1.getRunTime());
        assertEquals("2017", film1.getYear());

        HttpRequest<String> request3 = HttpRequest.GET("/list");
        List<Film> films = client.toBlocking().retrieve(request3, Argument.listOf(Film.class));
        log.info("Retrieved films: {}", films);
        Film film2 = films.stream().filter(f -> f.getId().equals(film.getId())).findFirst().get();
        assertEquals(film, film2);
        assertEquals("Guardians of the Galaxy Vol. 2", film2.getTitle());
        assertEquals(id, film2.getImdbId());
        assertEquals("136 min", film2.getRunTime());
        assertEquals("2017", film2.getYear());

        HttpRequest<String> request4 = HttpRequest.DELETE("/" + film.getId());
        HttpResponse<Object> response = client.toBlocking().exchange(request4);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());

        HttpRequest<String> request5 = HttpRequest.GET("/list");
        List<Film> films2 = client.toBlocking().retrieve(request3, Argument.listOf(Film.class));
        log.info("Retrieved films: {}", films2);
        Optional<Film> film3 = films2.stream().filter(f -> f.getId().equals(film.getId())).findFirst();
        assertTrue(film3.isEmpty());

    }

}

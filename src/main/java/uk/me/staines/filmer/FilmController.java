package uk.me.staines.filmer;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.me.staines.filmer.omdb.OmdbClient;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Controller("/films")
public class FilmController {

    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    protected final FilmRepository filmRepository;
    private final OmdbClient client;

    public FilmController(FilmRepository filmRepository, OmdbClient client) {
        this.filmRepository = filmRepository;
        this.client = client;
    }

    @Get("/ping")
    public Map<String,String> ping() {
        return Map.of("status","ok","timestamp", Instant.now().toString());
    }

    @Get("/{id}")
    public FilmDetails show(Long id) {
        return filmRepository
                .findById(id)
                .orElse(null);
    }

    @Put()
    public HttpResponse<?> update(@Body @Valid FilmUpdateCommand command) {
        int numberOfEntitiesUpdated = filmRepository.update(command.getId(), command.getFilmDetails());

        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(command.getId()).getPath());
    }

    @Get(value = "/list{?args*}")
    public List<FilmDetails> list(@Valid SortingAndOrderArguments args) {
        return filmRepository.findAll(args);
    }

    @Post("/add")
    public HttpResponse<Film> save(@Body @NotBlank String id) {
        log.info("Saving film with IMDB ID {}", id);
        FilmDetails details = client.find(id).blockingGet();
        return this.save(details);
    }

    @Put()
    public HttpResponse<Film> save(@Body @Valid FilmDetails details) {
        log.info("Saving film {}", details);
        Film film = filmRepository.save(details);
        log.info("Saved film with ID {}", film.getId());
        return HttpResponse
                .created(film)
                .headers(headers -> headers.location(location(film)));
    }

    @Delete("/{id}")
    public HttpResponse<?> delete(Long id) {
        filmRepository.deleteById(id);
        return HttpResponse.noContent();
    }

    protected URI location(Long id) {
        return URI.create("/films/" + id);
    }

    protected URI location(Film film) {
        return location(film.getId());
    }
}

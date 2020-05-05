package uk.me.staines.filmer;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.me.staines.filmer.omdb.OmdbClient;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Get(value = "/list/csv{?args*}")
    @Produces(MediaType.TEXT_PLAIN)
    public String listText(@Valid ListArguments args) {
        return filmRepository.find(args).stream().map(Film::toCsv).collect(Collectors.joining("\n"));
    }

    @Get(value = "/list{?args*}")
    @Produces(MediaType.APPLICATION_JSON)
    public FilmList list(@Valid ListArguments args) {
        List<Film> films = filmRepository.find(args);
        long count = filmRepository.count(args);
        return new FilmList(films, count);
    }

    @Post(value = "/add", consumes = MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<Film> save(@Body @NotBlank String id) {
        log.info("Saving film with IMDB ID {}", id);
        FilmDetails details = client.find(id).blockingGet().toFilmDetails();
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

    @Post()
    public HttpResponse<Film> update(@Body @Valid Film film) {
        log.info("Updating film {}", film);
        filmRepository.update(film);
        log.info("Updated film with ID {}", film.getId());
        return HttpResponse
                .ok(film)
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

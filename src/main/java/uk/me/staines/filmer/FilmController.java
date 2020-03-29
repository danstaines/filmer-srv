package uk.me.staines.filmer;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller("/films")
public class FilmController {

    protected final FilmRepository filmRepository;

    public FilmController(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Get("/{id}")
    public FilmDetails show(Long id) {
        return filmRepository
                .findById(id)
                .orElse(null);
    }

    @Put("/")
    public HttpResponse update(@Body @Valid FilmUpdateCommand command) {
        int numberOfEntitiesUpdated = filmRepository.update(command.getId(), command.getName(), command.getImdbId());

        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(command.getId()).getPath());
    }

    @Get(value = "/list{?args*}")
    public List<FilmDetails> list(@Valid SortingAndOrderArguments args) {
        return filmRepository.findAll(args);
    }

    @Post("/")
    public HttpResponse<Film> save(@Body @Valid FilmSaveCommand cmd) {
        Film film = filmRepository.save(cmd.getName(), cmd.getImdbId());

        return HttpResponse
                .created(film)
                .headers(headers -> headers.location(location(film.getId())));
    }

    @Delete("/{id}")
    public HttpResponse delete(Long id) {
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

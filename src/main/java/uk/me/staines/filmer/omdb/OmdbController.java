package uk.me.staines.filmer.omdb;

import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import uk.me.staines.filmer.FilmDetails;

import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller("/omdb")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class OmdbController {


    private final OmdbClient client;
    public OmdbController(OmdbClient client) {
        this.client = client;
    }

    @Get("/ping")
    public Map<String,String> ping(Principal user) {
        return Map.of("status","ok","timestamp", Instant.now().toString(), "user", user.getName());
    }

    @Get("/search")
    public List<FilmDetails> search(@QueryValue("query") String searchStr) {
        return client.search(searchStr).blockingGet().search.stream().map(OmdbFilmDetails::toFilmDetails).collect(Collectors.toList());
    }

    @Get(value = "/find/{id}")
    public FilmDetails find(@NotBlank String id) {
        OmdbFilmDetails omdbFilmDetails = client.find(id).blockingGet();
        if(omdbFilmDetails != null)
            return omdbFilmDetails.toFilmDetails();
        else
            return null;
    }
}

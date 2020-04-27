package uk.me.staines.filmer.omdb;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.uri.UriBuilder;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import uk.me.staines.filmer.FilmDetails;

import javax.inject.Singleton;
import java.net.URI;

@Singleton
public class OmdbClient {

    private final RxHttpClient httpClient;
    private final String apiKey;

    public OmdbClient(OmdbConfiguration configuration) {
        this(RxHttpClient.create(configuration.getUrl()), configuration.getApiKey());
    }

    public OmdbClient(RxHttpClient rxHttpClient, String apiKey) {
        httpClient = rxHttpClient;
        this.apiKey = apiKey;
    }


    public Maybe<OmdbSearchResult> search(String term) {
        URI uri = UriBuilder.of("/")
                .queryParam("apikey", apiKey)
                .queryParam("s", term + "*").build();
        HttpRequest<?> req = HttpRequest.GET(uri);
        Flowable<OmdbSearchResult> flowable = httpClient.retrieve(req, OmdbSearchResult.class);
        return flowable.firstElement();
    }

    public Maybe<OmdbFilmDetails> find(String id) {
        URI uri = UriBuilder.of("/")
                .queryParam("apikey", apiKey)
                .queryParam("i", id).build();
        HttpRequest<?> req = HttpRequest.GET(uri);
        Flowable<OmdbFilmDetails> flowable = httpClient.retrieve(req, OmdbFilmDetails.class);
        return flowable.firstElement();
    }

    public void stop() {
        httpClient.stop();
    }

    public static void main(String[] args) {
        OmdbClient client = new OmdbClient(new OmdbConfiguration(OmdbConfiguration.OMDB_API_URL, "dff9dc43"));
        Maybe<OmdbFilmDetails> film = client.find("tt3896198");
        System.out.println(film.blockingGet());
        Maybe<OmdbSearchResult> films = client.search("godfat");
        for (OmdbFilmDetails f : films.blockingGet().search) {
            System.out.println(f.toFilmDetails());
        }
        client.stop();
    }
}

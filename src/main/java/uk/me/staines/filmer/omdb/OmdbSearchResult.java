package uk.me.staines.filmer.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.me.staines.filmer.FilmDetails;

import java.util.List;

public class OmdbSearchResult {

    @JsonProperty("Search")
    public List<FilmDetails> search;
    @JsonProperty("totalResults")
    public int totalResults;
    @JsonProperty("Response")
    public String response;

    @Override
    public String toString() {
        return "OmdbSearchResult{" +
                "search=" + search +
                ", totalResults=" + totalResults +
                ", response='" + response + '\'' +
                '}';
    }
}

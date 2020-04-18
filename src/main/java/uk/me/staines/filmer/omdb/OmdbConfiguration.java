package uk.me.staines.filmer.omdb;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;

import java.net.MalformedURLException;
import java.net.URL;

@ConfigurationProperties(OmdbConfiguration.PREFIX)
@Requires(property = OmdbConfiguration.PREFIX)
public class OmdbConfiguration {
    public static final  String PREFIX = "omdb";
    public static final String OMDB_API_URL = "http://www.omdbapi.com";

    public OmdbConfiguration() {}

    public OmdbConfiguration(String url, String apiKey) {
        setUrlString(url);
        this.apiKey = apiKey;
    }

    private URL url;
    private String apiKey;
    public String getApiKey() {
        return apiKey;
    }
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public URL getUrl() {
        return url;
    }
    public void setUrlString(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Could not parse URL " + url, e);
        }
    }
    public void setUrl(URL url) {
        this.url = url;
    }
}

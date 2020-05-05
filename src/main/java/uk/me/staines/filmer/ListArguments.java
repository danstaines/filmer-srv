package uk.me.staines.filmer;

import io.micronaut.core.annotation.Introspected;

import javax.annotation.Nullable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Optional;

@Introspected
public class ListArguments {

    public enum WatchedFilter {
        WATCHED,UNWATCHED,ALL
    }

    @Nullable
    @PositiveOrZero
    private Integer offset;

    @Nullable
    @Positive
    private Integer max;

    @Nullable
    @Pattern(regexp = "id|title|runtime|watched|year|location|favourite")
    private String sort = "id";

    @Pattern(regexp = "asc|ASC|desc|DESC")
    @Nullable
    private String order = "asc";

    @Nullable
    private WatchedFilter watchedFilter = WatchedFilter.ALL;

    @Nullable
    private Film.Location locationFilter;

    @Nullable
    Boolean favourite;

    @Nullable
    private String filter;

    public ListArguments() {

    }

    public Optional<Integer> getOffset() {
        return Optional.ofNullable(offset);
    }

    public void setOffset(@Nullable Integer offset) {
        this.offset = offset;
    }

    public Optional<Integer> getMax() {
        return Optional.ofNullable(max);
    }

    public void setMax(@Nullable Integer max) {
        this.max = max;
    }

    public Optional<String> getSort() {
        return Optional.ofNullable(sort);
    }

    public void setSort(@Nullable String sort) {
        this.sort = sort;
    }

    public Optional<String> getOrder() {
        return Optional.ofNullable(order);
    }

    public void setOrder(@Nullable String order) {
        this.order = order;
    }

    public WatchedFilter getWatchedFilter() {
        return watchedFilter;
    }

    public void setWatchedFilter(@Nullable WatchedFilter watchedFilter) {
        this.watchedFilter = watchedFilter;
    }

    public Optional<Film.Location> getLocationFilter() {
        return Optional.ofNullable(locationFilter);
    }

    public void setLocationFilter(@Nullable Film.Location locationFilter) {
        this.locationFilter = locationFilter;
    }

    public Optional<String> getFilter() {
        return Optional.ofNullable(filter);
    }

    public Optional<Boolean> isFavourite() {
        return Optional.ofNullable(favourite);
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }

    public void setFilter(@Nullable String filter) {
        this.filter = filter;
    }
}
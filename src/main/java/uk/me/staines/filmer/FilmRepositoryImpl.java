package uk.me.staines.filmer;

import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static uk.me.staines.filmer.ListArguments.WatchedFilter.UNWATCHED;
import static uk.me.staines.filmer.ListArguments.WatchedFilter.WATCHED;

@Singleton
public class FilmRepositoryImpl implements FilmRepository {

    @PersistenceContext
    private final EntityManager entityManager;
    private final ApplicationConfiguration applicationConfiguration;

    public FilmRepositoryImpl(@CurrentSession EntityManager entityManager,
                              ApplicationConfiguration applicationConfiguration) {
        this.entityManager = entityManager;
        this.applicationConfiguration = applicationConfiguration;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Film> findById(@NotNull Long id) {
        return Optional.ofNullable(entityManager.find(Film.class, id));
    }

    @Override
    @Transactional
    public Film save(@NotNull FilmDetails details) {
        Film film = new Film(details);
        entityManager.persist(film);
        return film;
    }

    @Override
    @Transactional
    public void deleteById(@NotNull Long id) {
        findById(id).ifPresent(entityManager::remove);
    }

    private final static List<String> VALID_PROPERTY_NAMES = Arrays.asList("id", "name");

    @Transactional(readOnly = true)
    public List<FilmDetails> findAll(@NotNull ListArguments args) {
        String qlString = "SELECT f FROM Film as f";
        List<String> clauses = new ArrayList<>();
        List<Object[]> bindParams = new ArrayList<>();
        if (args.getWatchedFilter() == WATCHED) {
            clauses.add("f.watched = :watched");
            bindParams.add(new Object[]{"watched", true});
        } else if (args.getWatchedFilter() == UNWATCHED) {
            clauses.add("f.watched = :watched");
            bindParams.add(new Object[]{"watched", false});
        }
        if (args.getFilter().isPresent()) {
            clauses.add("lower(f.title) LIKE :title");
            bindParams.add(new Object[]{"title", "%" + args.getFilter().get().toLowerCase() + "%"});
        }
        if (!clauses.isEmpty()) {
            qlString += " WHERE " + clauses.stream().collect(Collectors.joining(" AND "));
        }
        if (args.getOrder().isPresent() && args.getSort().isPresent() && VALID_PROPERTY_NAMES.contains(args.getSort().get())) {
            qlString += " ORDER BY f." + args.getSort().get() + " " + args.getOrder().get().toLowerCase();
        }
        TypedQuery<FilmDetails> query = entityManager.createQuery(qlString, FilmDetails.class);
        bindParams.stream().forEach(
                (p) -> {
                    query.setParameter((String) p[0], p[1]);
                }
        );
        query.setMaxResults(args.getMax().orElseGet(applicationConfiguration::getMax));
        args.getOffset().ifPresent(query::setFirstResult);
        return query.getResultList();
    }

    @Override
    @Transactional
    public int update(@NotNull Film film) {
        return entityManager.createQuery("UPDATE Film f SET " +
                "title = :title, " +
                "imdb_id = :imdb_id, " +
                "runtime = :runtime, " +
                "year = :year, " +
                "location = :location, " +
                "watched = :watched " +
                "where id = :id")
                .setParameter("title", film.getTitle())
                .setParameter("imdb_id", film.getImdbId())
                .setParameter("runtime", film.getRunTime())
                .setParameter("year", film.getYear())
                .setParameter("watched", film.getWatched())
                .setParameter("location", film.getLocation())
                .setParameter("id", film.getId())
                .executeUpdate();
    }

}

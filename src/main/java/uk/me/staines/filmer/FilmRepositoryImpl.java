package uk.me.staines.filmer;

import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        public List<FilmDetails> findAll(@NotNull SortingAndOrderArguments args) {
            String qlString = "SELECT f FROM Film as f";
            if (args.getOrder().isPresent() && args.getSort().isPresent() && VALID_PROPERTY_NAMES.contains(args.getSort().get())) {
                qlString += " ORDER BY f." + args.getSort().get() + " " + args.getOrder().get().toLowerCase();
            }
            TypedQuery<FilmDetails> query = entityManager.createQuery(qlString, FilmDetails.class);
            query.setMaxResults(args.getMax().orElseGet(applicationConfiguration::getMax));
            args.getOffset().ifPresent(query::setFirstResult);
            return query.getResultList();
        }

        @Override
        @Transactional
        public int update(@NotNull Long id, @NotNull FilmDetails details) {
            return entityManager.createQuery("UPDATE Film f SET " +
                    "title = :title, " +
                    "imdb_id = :imdbId " +
                    "runtime = :runtime " +
                    "year = :year " +
                    "where id = :id")
                    .setParameter("title", details.getTitle())
                    .setParameter("imdb_id", details.getImdbId())
                    .setParameter("runtime", details.getRunTime())
                    .setParameter("year", details.getYear())
                    .setParameter("id", id)
                    .executeUpdate();
        }

}

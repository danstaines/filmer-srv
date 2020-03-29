package uk.me.staines.filmer;

import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public class FilmRepositoryImpl implements FilmRepository {

        @PersistenceContext
        private EntityManager entityManager;
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
        public Film save(@NotBlank String name, String imdbId) {
            Film film = new Film(name, imdbId);
            entityManager.persist(film);
            return film;
        }

        @Override
        @Transactional
        public void deleteById(@NotNull Long id) {
            findById(id).ifPresent(filmDetails -> entityManager.remove(filmDetails));
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
        public int update(@NotNull Long id, @NotBlank String name, @NotBlank String imdbId) {
            return entityManager.createQuery("UPDATE Film f SET name = :name, imdb_id = :imdbId where id = :id")
                    .setParameter("name", name)
                    .setParameter("imdb_id", imdbId)
                    .setParameter("id", id)
                    .executeUpdate();
        }

}

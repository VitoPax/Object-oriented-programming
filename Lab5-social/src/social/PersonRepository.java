package social;

import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class PersonRepository extends GenericRepository<Person, String> {
  public PersonRepository() {
    super(Person.class);
  }

  public Optional<Person> findPersonWithMostFriends() {
      EntityManager em = JPAUtil.getEntityManager();

      TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p ORDER BY SIZE(p.friends) DESC LIMIT 1", Person.class);

      try {
          return Optional.of(query.getSingleResult());
      } catch (NoResultException e) {
          return Optional.empty();
      } finally {
          em.close();
      }
  }

  public Optional<Person> findPersonInMostGroups() {
      EntityManager em = JPAUtil.getEntityManager();

      TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p ORDER BY SIZE(p.groups) DESC LIMIT 1", Person.class);

      try {
          return Optional.of(query.getSingleResult());
      } catch (NoResultException e) {
          return Optional.empty();
      } finally {
          em.close();
      }
  }
}

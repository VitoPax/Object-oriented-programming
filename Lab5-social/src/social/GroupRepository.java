package social;

import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class GroupRepository extends GenericRepository<Group, Integer> {
    public GroupRepository() {
        super(Group.class);
    }

    public Optional<Group> findByName(String name) {
        EntityManager em = JPAUtil.getEntityManager();

        TypedQuery<Group> query = em.createQuery("SELECT g FROM Group g WHERE g.name = :name", Group.class);
        query.setParameter("name", name);

        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    public Optional<Group> findGroupWithMostMembers() {
        EntityManager em = JPAUtil.getEntityManager();

        TypedQuery<Group> query = em.createQuery("SELECT g FROM Group g ORDER BY SIZE(g.members) DESC LIMIT 1", Group.class);

        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
}

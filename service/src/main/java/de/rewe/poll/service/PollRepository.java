package de.rewe.poll.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional
public class PollRepository {

    @PersistenceContext
    private EntityManager em;


    public List<Poll> entries() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Poll> cq = cb.createQuery(Poll.class);
        Root<Poll> rootEntry = cq.from(Poll.class);
        CriteriaQuery<Poll> all = cq.select(rootEntry);
        TypedQuery<Poll> allQuery = em.createQuery(all);
        return allQuery.getResultList();
    }
    public Poll get(String id) {
        return em.find(Poll.class, id);
    }

    public void put(Poll poll) {
        em.persist(poll);
    }

    public void remove(String id) {
        em.remove(get(id));
    }


}

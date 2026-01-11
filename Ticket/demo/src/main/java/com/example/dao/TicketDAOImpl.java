package com.example.dao;

import com.example.entity.Ticket;
import com.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class TicketDAOImpl extends GenericDAOImpl<Ticket, Long> implements TicketDAO {

    public TicketDAOImpl() {
        super(Ticket.class);
    }

    @Override
    public List<Ticket> findByMatchId(Long matchId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Ticket t where t.match.id = :matchId", Ticket.class)
                    .setParameter("matchId", matchId)
                    .list();
        }
    }

    @Override
    public List<Ticket> findAvailableByMatchId(Long matchId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Ticket t where t.match.id = :matchId and t.status = 'AVAILABLE'", Ticket.class)
                    .setParameter("matchId", matchId)
                    .list();
        }
    }
}
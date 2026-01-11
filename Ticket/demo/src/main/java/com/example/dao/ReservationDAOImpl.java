package com.example.dao;

import com.example.entity.Reservation;
import com.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class ReservationDAOImpl extends GenericDAOImpl<Reservation, Long> implements ReservationDAO {

    public ReservationDAOImpl() {
        super(Reservation.class);
    }

    @Override
    public List<Reservation> findByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Reservation r where r.user.id = :userId", Reservation.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }
}
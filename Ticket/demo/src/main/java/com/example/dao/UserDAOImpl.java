package com.example.dao;

import com.example.entity.User;
import com.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Optional;

public class UserDAOImpl extends GenericDAOImpl<User, Long> implements UserDAO {

    public UserDAOImpl() {
        super(User.class);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User u where u.email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResultOptional();
        }
    }
}
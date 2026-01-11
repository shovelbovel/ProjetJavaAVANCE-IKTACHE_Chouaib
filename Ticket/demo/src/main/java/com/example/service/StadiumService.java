package com.example.service;

import com.example.dao.StadiumDAO;
import com.example.dao.StadiumDAOImpl;
import com.example.entity.Stadium;

import java.util.List;
import java.util.Optional;

public class StadiumService {

    private final StadiumDAO stadiumDAO = new StadiumDAOImpl();

    public void createStadium(String name, String city, int capacity) {
        Stadium stadium = new Stadium(name, city, capacity);
        stadiumDAO.save(stadium);
    }

    public void updateStadium(Stadium stadium) {
        stadiumDAO.update(stadium);
    }

    public void deleteStadium(Stadium stadium) {
        stadiumDAO.delete(stadium);
    }

    public List<Stadium> getAllStadiums() {
        return stadiumDAO.findAll();
    }

    public Optional<Stadium> getStadiumById(Long id) {
        return stadiumDAO.findById(id);
    }
}
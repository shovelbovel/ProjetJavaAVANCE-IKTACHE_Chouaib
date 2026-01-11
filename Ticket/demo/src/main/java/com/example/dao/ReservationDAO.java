package com.example.dao;

import com.example.entity.Reservation;

import java.util.List;

public interface ReservationDAO extends GenericDAO<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
}
package com.example.dao;

import com.example.entity.Ticket;

import java.util.List;

public interface TicketDAO extends GenericDAO<Ticket, Long> {
    List<Ticket> findByMatchId(Long matchId);
    List<Ticket> findAvailableByMatchId(Long matchId);
}
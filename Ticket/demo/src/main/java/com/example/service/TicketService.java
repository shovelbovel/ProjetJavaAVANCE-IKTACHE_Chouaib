package com.example.service;

import com.example.dao.TicketDAO;
import com.example.dao.TicketDAOImpl;
import com.example.entity.Match;
import com.example.entity.Ticket;
import com.example.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TicketService {

    private final TicketDAO ticketDAO = new TicketDAOImpl();

    public void generateTicketsForMatch(Match match, int vipCount, BigDecimal vipPrice,
                                        int standardCount, BigDecimal standardPrice,
                                        int economyCount, BigDecimal economyPrice) {
        for (int i = 0; i < vipCount; i++) {
            Ticket ticket = new Ticket(Ticket.Category.VIP, vipPrice, match);
            ticketDAO.save(ticket);
        }
        for (int i = 0; i < standardCount; i++) {
            Ticket ticket = new Ticket(Ticket.Category.STANDARD, standardPrice, match);
            ticketDAO.save(ticket);
        }
        for (int i = 0; i < economyCount; i++) {
            Ticket ticket = new Ticket(Ticket.Category.ECONOMY, economyPrice, match);
            ticketDAO.save(ticket);
        }
    }

    public void generateTickets(Match match, Ticket.Category category, int quantity, double price) {
        for (int i = 0; i < quantity; i++) {
            Ticket ticket = new Ticket(category, BigDecimal.valueOf(price), match);
            ticketDAO.save(ticket);
        }
    }

    public List<Ticket> getTicketsByMatch(Long matchId) {
        return ticketDAO.findByMatchId(matchId);
    }

    public List<Ticket> getAvailableTicketsByMatch(Long matchId) {
        return ticketDAO.findAvailableByMatchId(matchId);
    }

    public List<Ticket> getAvailableTicketsForMatch(Long matchId) {
        return ticketDAO.findAvailableByMatchId(matchId);
    }

    public List<Ticket> getAllTickets() {
        return ticketDAO.findAll();
    }

    public List<Ticket> getSoldTickets() {
        return ticketDAO.findAll().stream()
                .filter(t -> t.getStatus() == Ticket.Status.SOLD)
                .collect(Collectors.toList());
    }

    public List<Ticket> getAvailableTickets() {
        return ticketDAO.findAll().stream()
                .filter(t -> t.getStatus() == Ticket.Status.AVAILABLE)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalRevenue() {
        return getSoldTickets().stream()
                .map(Ticket::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Ticket> getTicketsByUser(Long userId) {
        return ticketDAO.findAll().stream()
                .filter(t -> t.getStatus() == Ticket.Status.SOLD)
                .collect(Collectors.toList());
    }

    public void purchaseTicket(Long ticketId, User buyer) {
        Optional<Ticket> ticketOpt = ticketDAO.findById(ticketId);
        if (ticketOpt.isPresent()) {
            Ticket ticket = ticketOpt.get();
            if (ticket.getStatus() != Ticket.Status.AVAILABLE) {
                throw new IllegalStateException("Ticket is not available");
            }
            ticket.setStatus(Ticket.Status.SOLD);
            ticketDAO.update(ticket);
        } else {
            throw new IllegalArgumentException("Ticket not found");
        }
    }

    public void updateTicket(Ticket ticket) {
        ticketDAO.update(ticket);
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketDAO.findById(id);
    }
}
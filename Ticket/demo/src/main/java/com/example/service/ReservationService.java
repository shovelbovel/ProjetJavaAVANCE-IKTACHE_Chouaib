package com.example.service;

import com.example.dao.ReservationDAO;
import com.example.dao.ReservationDAOImpl;
import com.example.dao.TicketDAO;
import com.example.dao.TicketDAOImpl;
import com.example.entity.Reservation;
import com.example.entity.Ticket;
import com.example.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ReservationService {

    private final ReservationDAO reservationDAO = new ReservationDAOImpl();
    private final TicketDAO ticketDAO = new TicketDAOImpl();

    public void reserveTicket(User user, Ticket ticket) {
        if (ticket.getStatus() != Ticket.Status.AVAILABLE) {
            throw new IllegalStateException("Ticket not available");
        }
        ticket.setStatus(Ticket.Status.RESERVED);
        ticketDAO.update(ticket);
        Reservation reservation = new Reservation(LocalDateTime.now(), user, ticket);
        reservationDAO.save(reservation);
    }

    public void createReservation(User user, Ticket ticket) {
        reserveTicket(user, ticket);
    }

    public void buyTicket(Reservation reservation) {
        reservation.getTicket().setStatus(Ticket.Status.SOLD);
        ticketDAO.update(reservation.getTicket());
        reservationDAO.update(reservation);
    }

    public void confirmReservation(Long reservationId) {
        Optional<Reservation> resOpt = reservationDAO.findById(reservationId);
        if (resOpt.isPresent()) {
            buyTicket(resOpt.get());
        } else {
            throw new IllegalArgumentException("Reservation not found");
        }
    }

    public void cancelReservation(Reservation reservation) {
        reservation.getTicket().setStatus(Ticket.Status.AVAILABLE);
        ticketDAO.update(reservation.getTicket());
        reservationDAO.delete(reservation);
    }

    public void cancelReservation(Long reservationId) {
        Optional<Reservation> resOpt = reservationDAO.findById(reservationId);
        if (resOpt.isPresent()) {
            cancelReservation(resOpt.get());
        } else {
            throw new IllegalArgumentException("Reservation not found");
        }
    }

    public List<Reservation> getReservationsByUser(Long userId) {
        return reservationDAO.findByUserId(userId);
    }

    public List<Reservation> getUserReservations(Long userId) {
        return getReservationsByUser(userId);
    }

    public List<Reservation> getAllReservations() {
        return reservationDAO.findAll();
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationDAO.findById(id);
    }
}
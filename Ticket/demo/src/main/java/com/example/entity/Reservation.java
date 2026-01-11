package com.example.entity;

import java.time.LocalDateTime;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "reservation_date")
    private LocalDateTime reservationDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    // Constructors
    public Reservation() {}

    public Reservation(LocalDateTime reservationDate, User user, Ticket ticket) {
        this.reservationDate = reservationDate;
        this.user = user;
        this.ticket = ticket;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDateTime reservationDate) { this.reservationDate = reservationDate; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", reservationDate=" + reservationDate +
                ", user=" + user.getName() +
                ", ticket=" + ticket.getId() +
                '}';
    }
}
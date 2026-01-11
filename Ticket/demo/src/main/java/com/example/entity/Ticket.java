package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    public enum Category {
        VIP, STANDARD, ECONOMY
    }

    public enum Status {
        AVAILABLE, RESERVED, SOLD
    }

    // Constructors
    public Ticket() {}

    public Ticket(Category category, BigDecimal price, Match match) {
        this.category = category;
        this.price = price;
        this.status = Status.AVAILABLE;
        this.match = match;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Match getMatch() { return match; }
    public void setMatch(Match match) { this.match = match; }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", category=" + category +
                ", price=" + price +
                ", status=" + status +
                ", match=" + match.getTeam1() + " vs " + match.getTeam2() +
                '}';
    }
}
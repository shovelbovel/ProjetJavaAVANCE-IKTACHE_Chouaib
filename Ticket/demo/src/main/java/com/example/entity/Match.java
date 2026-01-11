package com.example.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String team1;

    @NotBlank
    private String team2;

    @NotNull
    @Column(name = "match_date")
    private LocalDateTime matchDate;

    @ManyToOne
    @JoinColumn(name = "stadium_id", nullable = false)
    private Stadium stadium;

    @OneToMany(mappedBy = "match", fetch = FetchType.EAGER)
    private List<Ticket> tickets = new java.util.ArrayList<>();

    // Constructors
    public Match() {}

    public Match(String team1, String team2, LocalDateTime matchDate, Stadium stadium) {
        this.team1 = team1;
        this.team2 = team2;
        this.matchDate = matchDate;
        this.stadium = stadium;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTeam1() { return team1; }
    public void setTeam1(String team1) { this.team1 = team1; }

    public String getTeam2() { return team2; }
    public void setTeam2(String team2) { this.team2 = team2; }

    public LocalDateTime getMatchDate() { return matchDate; }
    public void setMatchDate(LocalDateTime matchDate) { this.matchDate = matchDate; }

    public Stadium getStadium() { return stadium; }
    public void setStadium(Stadium stadium) { this.stadium = stadium; }

    public List<Ticket> getTickets() { return tickets; }
    public void setTickets(List<Ticket> tickets) { this.tickets = tickets; }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", team1='" + team1 + '\'' +
                ", team2='" + team2 + '\'' +
                ", matchDate=" + matchDate +
                ", stadium=" + stadium.getName() +
                '}';
    }
}
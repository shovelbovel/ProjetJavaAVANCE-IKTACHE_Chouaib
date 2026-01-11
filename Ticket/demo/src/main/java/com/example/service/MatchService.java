package com.example.service;

import com.example.dao.MatchDAO;
import com.example.dao.MatchDAOImpl;
import com.example.entity.Match;
import com.example.entity.Stadium;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class MatchService {

    private final MatchDAO matchDAO = new MatchDAOImpl();

    public void createMatch(String team1, String team2, LocalDateTime matchDate, Stadium stadium) {
        Match match = new Match(team1, team2, matchDate, stadium);
        matchDAO.save(match);
    }

    public void updateMatch(Match match) {
        matchDAO.update(match);
    }

    public void deleteMatch(Match match) {
        matchDAO.delete(match);
    }

    public List<Match> getAllMatches() {
        return matchDAO.findAll();
    }

    public Optional<Match> getMatchById(Long id) {
        return matchDAO.findById(id);
    }
}
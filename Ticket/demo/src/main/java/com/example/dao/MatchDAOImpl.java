package com.example.dao;

import com.example.entity.Match;

public class MatchDAOImpl extends GenericDAOImpl<Match, Long> implements MatchDAO {

    public MatchDAOImpl() {
        super(Match.class);
    }
}
package com.example.dao;

import com.example.entity.Stadium;

public class StadiumDAOImpl extends GenericDAOImpl<Stadium, Long> implements StadiumDAO {

    public StadiumDAOImpl() {
        super(Stadium.class);
    }
}
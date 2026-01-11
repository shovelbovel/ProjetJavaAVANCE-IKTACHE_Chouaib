package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "stadiums")
public class Stadium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @NotBlank
    private String city;

    @Min(1)
    private int capacity;

    // Constructors
    public Stadium() {}

    public Stadium(String name, String city, int capacity) {
        this.name = name;
        this.city = city;
        this.capacity = capacity;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    @Override
    public String toString() {
        return "Stadium{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
package com.boersenparty.v_1_1.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tEvents")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDateTime start_at = LocalDateTime.now().plusMinutes(10);
    private LocalDateTime end_at = start_at.plusMinutes(33);


    @ManyToOne
    @JoinColumn // party_id fehlt
    private Party party;


    private String type = "Börsen Crash"; //Happy Hour etc.


}

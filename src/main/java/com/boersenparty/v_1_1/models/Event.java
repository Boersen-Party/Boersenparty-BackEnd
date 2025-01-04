package com.boersenparty.v_1_1.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tEvents")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type; //Happy Hour etc.

    private Integer duration;  // Duration in minutes

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "party_id", nullable = true)
    @JsonIgnore
    private Party party;

    public Long getId() {
        return id;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", duration=" + duration +
                ", party=" + party +
                '}';
    }
}

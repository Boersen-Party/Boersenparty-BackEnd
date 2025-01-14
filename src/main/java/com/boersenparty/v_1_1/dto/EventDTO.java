package com.boersenparty.v_1_1.dto;


public class EventDTO {
    private Long id;
    private String type;  // Event type can only be 'Happy Hour' or 'Boersencrash'
    private Integer duration;  // Duration in minutes

    public EventDTO() {}

    public EventDTO(Long id, String type, Integer duration) {
        this.id = id;
        this.type = type;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "EventDTO{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", duration=" + duration +
                '}';
    }
}

package com.boersenparty.v_1_1.models;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


// TODO: "n_guests" als Abfrage funktion in PartyService
@Entity
@Table(name="tParties")
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String hosted_by;

    // Constraint: ensure start_date is in the future, and is earlier than end_date etc.
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yy HH:mm")
    private LocalDateTime start_date;


    // TODO: In frontend: block end_date input, before a start_date is inputted
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yy HH:mm")
    private LocalDateTime end_date;



    @OneToMany(mappedBy = "party", cascade = CascadeType.REMOVE, orphanRemoval = false)
    private List<PartyGuest> partyGuests;




    public Party(){
        System.out.println("Default Party Constructor is called");
    }


    public Party(String name, String hosted_by) {
        System.out.println("First Party Constructor is called");
        this.name = name;
        this.hosted_by = hosted_by;
        this.start_date = LocalDateTime.now();
        this.end_date = this.start_date.plusDays(1);
    }
    public Party(String name, LocalDateTime start_date, LocalDateTime end_date, String hosted_by) {
        System.out.println("Second Constructor is called");
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.hosted_by = hosted_by;
    }

    @Override
    public String toString() {
        return "Party{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", partyGuests=" + partyGuests +
                ", hosted_by='" + hosted_by + '\'' +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDateTime start_date) {
        this.start_date = start_date;
    }

    public LocalDateTime getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDateTime end_date) {
        this.end_date = end_date;
    }

    public String getHosted_by() {
        return hosted_by;
    }

    public void setHosted_by(String hosted_by) {
        this.hosted_by = hosted_by;
    }

    public List<PartyGuest> getPartyGuests() {
        return this.partyGuests;
    }

    public void setPartyGuests(List<PartyGuest> partyGuests) {
        this.partyGuests = partyGuests;
    }

}

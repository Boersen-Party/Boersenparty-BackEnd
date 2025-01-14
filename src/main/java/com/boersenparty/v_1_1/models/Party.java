package com.boersenparty.v_1_1.models;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@Entity
@Table(name="tParties")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String accessCode;
    private String hostedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime start_date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime end_date;
    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartyGuest> partyGuests;
    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private List <Product> products;
    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private List <Event> events;
    @OneToOne(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private PartyStats partyStats;

    public PartyStats getPartyStats() {
        return partyStats;
    }

    public void setPartyStats(PartyStats partyStats) {
        this.partyStats = partyStats;
    }

    public Party(){}


    @Override
    public String toString() {
        return "Party{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", accessCode='" + accessCode + '\'' +
                ", hostedBy='" + hostedBy + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", partyGuests=" + "coming soon" +
                ", products=" + "not showing" +
                ", events=" + "not showing" +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
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
        return hostedBy;
    }

    public void setHosted_by(String hosted_by) {
        this.hostedBy = hosted_by;
    }

    public List<PartyGuest> getPartyGuests() {
        return this.partyGuests;
    }

    public void setPartyGuests(List<PartyGuest> partyGuests) {
        this.partyGuests = partyGuests;
    }
    public String getHostedBy() {
        return hostedBy;
    }

    public void setHostedBy(String hostedBy) {
        this.hostedBy = hostedBy;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}

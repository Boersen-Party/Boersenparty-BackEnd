package com.boersenparty.v_1_1.models;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name="tParties")
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String hostedBy;



    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime start_date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime end_date;

    // cascadetype.REMOVE ?
    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartyGuest> partyGuests;

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private List <Product> products;


    public Party(){}

    public Party(List<Product> products){
        this.products = products;
        System.out.println("Default Party Constructor is called");
    }


    public Party(String name, String hosted_by, List<Product> products) {
        this.products = products;
        System.out.println("First Party Constructor is called");
        this.name = name;
        this.hostedBy = hosted_by;
        this.start_date = LocalDateTime.now();
        this.end_date = this.start_date.plusDays(1);
    }
    public Party(String name, LocalDateTime start_date, LocalDateTime end_date, List<Product> products) {
        this.products = products;
        System.out.println("Second Constructor is called");
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.hostedBy = "Partyveranstalter";
    }

    @Override
    public String toString() {
        return "Party{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", partyGuests=" + "NOT IMPLEMENTED YET" +
                ", hosted_by='" + hostedBy + '\'' +
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

}

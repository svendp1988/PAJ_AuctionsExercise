package be.pxl.auctions.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "bids")
public class Bid implements Comparable<Bid> {
    @Id
    @GeneratedValue
    private long id;
    private double amount;
    private LocalDate date;
    @ManyToOne
    private Auction auction;
    @OneToOne
    private User user;

    public Bid() {
    }

    public Bid(User user, LocalDate date, double amount) {
        this.user = user;
        this.date = date;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    @Override
    public int compareTo(Bid o) {
        return Double.compare(o.amount, this.amount);
    }

    public User getUser() {
        return user;
    }
}

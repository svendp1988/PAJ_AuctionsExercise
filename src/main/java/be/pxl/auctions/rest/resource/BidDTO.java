package be.pxl.auctions.rest.resource;

import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.User;

import java.time.LocalDate;

public class BidDTO {
    private long id;
    private double amount;
    private LocalDate date;
    private Auction auction;
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

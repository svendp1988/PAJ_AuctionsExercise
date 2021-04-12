package be.pxl.auctions.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "auction")
@NamedQueries({ @NamedQuery(name = "findAllAuctions", query = "SELECT a FROM Auction a") })
public class Auction {
    @Id
    @GeneratedValue
    private long id;
    private String description;
    private LocalDate endDate;
    @OneToMany(mappedBy = "auction", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Bid> bids = new ArrayList<>();

    public Auction() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void addBid(Bid bid) {
        bids.add(bid);
    }

    public boolean isFinished() {
        return endDate.isBefore(LocalDate.now());
    }

    public Bid findHighestBid() {
        if (bids.isEmpty()) {
            return null;
        }
        return bids.stream()
                .sorted()
                .limit(1)
                .findFirst()
                .get();
    }
}

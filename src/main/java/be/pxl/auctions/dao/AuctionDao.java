package be.pxl.auctions.dao;

import be.pxl.auctions.model.Auction;

import java.util.List;
import java.util.Optional;

public interface AuctionDao {
    Auction saveAction(Auction auction);
    Optional<Auction> findAuctionById(long id);
    List<Auction> findAllAuctions();
}

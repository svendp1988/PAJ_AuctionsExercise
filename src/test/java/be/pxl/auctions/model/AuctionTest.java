package be.pxl.auctions.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AuctionTest {
    @Test
    void isFinishedReturnsTrueIfEndDateBeforeNow() {
        LocalDate endDate = LocalDate.now().minusDays(5);
        Auction auction = new Auction();
        auction.setEndDate(endDate);

        assertTrue(auction.isFinished());
    }

    @Test
    void isFinishedReturnsFalseIfEndDateAfterNow() {
        LocalDate endDate = LocalDate.now().plusDays(5);
        Auction auction = new Auction();
        auction.setEndDate(endDate);

        assertFalse(auction.isFinished());
    }

    @Test
    void findHighestBidReturnsNullIfBidsIsEmpty() {
        Auction auction = new Auction();
        assertTrue(auction.getBids().isEmpty());

        assertNull(auction.findHighestBid());
    }

    @Test
    void findHighestBidReturnsBidWithHighestAmount() {
        Bid bid1 = new Bid(new User(), LocalDate.now(), 500);
        Bid bid2 = new Bid(new User(), LocalDate.now(), 100);
        Auction auction = new Auction();
        auction.addBid(bid1);
        auction.addBid(bid2);

        assertEquals(500, auction.findHighestBid().getAmount());
    }
}
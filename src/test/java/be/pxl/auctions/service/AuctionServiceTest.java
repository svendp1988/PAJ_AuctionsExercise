package be.pxl.auctions.service;

import be.pxl.auctions.dao.AuctionDao;
import be.pxl.auctions.dao.UserDao;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.Bid;
import be.pxl.auctions.model.User;
import be.pxl.auctions.rest.resource.BidCreateResource;
import be.pxl.auctions.util.exception.AuctionNotFoundException;
import be.pxl.auctions.util.exception.InvalidBidException;
import be.pxl.auctions.util.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {
    private static final long AUCTION_ID = 1234L;
    public static final String USER_EMAIL = "test@student.pxl.be";

    @Mock
    private AuctionDao auctionDao;
    @Mock
    private UserDao userDao;
    @InjectMocks
    private AuctionService auctionService;
    @Captor
    private ArgumentCaptor<Auction> auctionArgumentCaptor;
    private BidCreateResource lowBid;
    private BidCreateResource equalBid;
    private BidCreateResource validBid;
    private Auction auction;
    private User user;

    @BeforeEach
    void setUp() {
        lowBid = new BidCreateResource();
        lowBid.setAmount(50L);
        lowBid.setUserEmail(USER_EMAIL);
        equalBid = new BidCreateResource();
        equalBid.setAmount(100L);
        equalBid.setUserEmail(USER_EMAIL);
        validBid = new BidCreateResource();
        validBid.setAmount(200L);
        validBid.setUserEmail(USER_EMAIL);
        user = new User();
        user.setEmail(USER_EMAIL);
        user.setDateOfBirth(LocalDate.of(1988, 6, 7));
        user.setFirstName("test");
        user.setLastName("user");
        Bid bid = new Bid(user, LocalDate.now(), 100L);
        auction = new Auction();
        auction.setDescription("auction");
        auction.setEndDate(LocalDate.now().plusDays(7));
        auction.addBid(bid);
    }

    @Test
    void throwsAuctionNotFoundExceptionIfWrongAuctionId() {
        when(auctionDao.findAuctionById(AUCTION_ID)).thenReturn(Optional.empty());
        AuctionNotFoundException actual = assertThrows(AuctionNotFoundException.class, () -> auctionService.registerBid(AUCTION_ID, lowBid));
        assertEquals("Unable to find Auction with id [1234]", actual.getMessage());
    }

    @Test
    void throwsUserNotFoundExceptionIfUserIdNotFound() {
        when(auctionDao.findAuctionById(AUCTION_ID)).thenReturn(Optional.of(auction));
        UserNotFoundException actual = assertThrows(UserNotFoundException.class, () -> auctionService.registerBid(AUCTION_ID, lowBid));
        assertEquals("Unable to find User with email [test@student.pxl.be]", actual.getMessage());
    }

    @Test
    void throwsInvalidBidExceptionIfBidTooLow() {
        when(auctionDao.findAuctionById(AUCTION_ID)).thenReturn(Optional.of(auction));
        when(userDao.findUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        InvalidBidException actual = assertThrows(InvalidBidException.class, () -> auctionService.registerBid(AUCTION_ID, lowBid));
        assertEquals("Bid cannot be lower than highest bid.", actual.getMessage());
    }

    @Test
    void throwsInvalidBidExceptionIfUserAlreadyHasHighestBid() {
        when(auctionDao.findAuctionById(AUCTION_ID)).thenReturn(Optional.of(auction));
        when(userDao.findUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        InvalidBidException actual = assertThrows(InvalidBidException.class, () -> auctionService.registerBid(AUCTION_ID, equalBid));
        assertEquals("User [test@student.pxl.be] already has the highest bid.", actual.getMessage());
    }

    @Test
    void throwsInvalidBidExceptionIfAuctionIsAlreadyClosed() {
        auction.setEndDate(LocalDate.now().minusDays(1));
        when(auctionDao.findAuctionById(AUCTION_ID)).thenReturn(Optional.of(auction));
        when(userDao.findUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        InvalidBidException actual = assertThrows(InvalidBidException.class, () -> auctionService.registerBid(AUCTION_ID, validBid));
        assertEquals("Cannot make a bid on this auction as it is already closed.", actual.getMessage());
    }

    @Test
    void savesBidIfValidBid() {
        when(auctionDao.findAuctionById(AUCTION_ID)).thenReturn(Optional.of(auction));
        when(userDao.findUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        when(auctionDao.saveAuction(any())).thenAnswer(returnsFirstArg());
        auctionService.registerBid(AUCTION_ID, validBid);

        verify(auctionDao).saveAuction(auctionArgumentCaptor.capture());

        Auction actual = auctionArgumentCaptor.getValue();

        assertEquals(actual.getDescription(), auction.getDescription());
        assertEquals(actual.getEndDate(), auction.getEndDate());
        assertEquals(actual.findHighestBid().getAmount(), validBid.getAmount());
    }
}
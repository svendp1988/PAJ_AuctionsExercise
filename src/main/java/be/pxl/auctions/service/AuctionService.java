package be.pxl.auctions.service;

import be.pxl.auctions.dao.AuctionDao;
import be.pxl.auctions.dao.UserDao;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.Bid;
import be.pxl.auctions.model.User;
import be.pxl.auctions.rest.resource.AuctionCreateResource;
import be.pxl.auctions.rest.resource.AuctionDTO;
import be.pxl.auctions.rest.resource.BidCreateResource;
import be.pxl.auctions.rest.resource.BidDTO;
import be.pxl.auctions.util.exception.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuctionService {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/uuuu");

    @Autowired
    private AuctionDao auctionDao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    public List<AuctionDTO> getAllAuctions() {
        return auctionDao.findAllAuctions().stream().map(this::mapToAuctionResource).collect(Collectors.toList());
    }

    private AuctionDTO mapToAuctionResource(Auction auction) {
        AuctionDTO auctionDTO = new AuctionDTO();
        auctionDTO.setId(auction.getId());
        auctionDTO.setDescription(auction.getDescription());
        auctionDTO.setEndDate(auction.getEndDate());
        auctionDTO.setBids(auction.getBids().stream().map(this::mapToBidResource).collect(Collectors.toList()));
        auctionDTO.setNumberOfBids(auction.getBids().size());
        auctionDTO.setHighestBid(mapToBidResource(auction.findHighestBid()));
        auctionDTO.setHigestBidBy(userService.mapToUserResource(auctionDTO.getHighestBid().getUser()));
        return auctionDTO;
    }

    public AuctionDTO getAuctionById(long auctionId) {
        return auctionDao.findAuctionById(auctionId).map(this::mapToAuctionResource).orElseThrow(() -> new AuctionNotFoundException("Unable to find Auction with id [" + auctionId + "]"));
    }

    public AuctionDTO createAuction(AuctionCreateResource auctionInfo) {
        if (StringUtils.isBlank(auctionInfo.getDescription())) {
            throw new RequiredFieldException("Description");
        }
        if (StringUtils.isBlank(auctionInfo.getEndDate())) {
            throw new RequiredFieldException("EndDate");
        }
        Auction auction = mapToAuction(auctionInfo);
        if (auction.isFinished()) {
            throw new InvalidDateException("EndDate cannot be in the past.");
        }
        return mapToAuctionResource(auctionDao.saveAction(auction));
    }

    private Auction mapToAuction(AuctionCreateResource auctionInfo) {
        Auction auction = new Auction();
        auction.setDescription(auctionInfo.getDescription());
        try {
            auction.setEndDate(LocalDate.parse(auctionInfo.getEndDate(), DATE_FORMAT));
        } catch (DateTimeParseException e) {
            throw new InvalidDateException("[" + auction.getEndDate() + "] is not a valid date. Excepted format: dd/mm/yyyy");
        }
        return auction;
    }

    private BidDTO mapToBidResource(Bid bid) {
        BidDTO bidDTO = new BidDTO();
        bidDTO.setId(bid.getId());
        bidDTO.setAmount(bid.getAmount());
        bidDTO.setDate(bid.getDate());
        bidDTO.setAuction(bid.getAuction());
        bidDTO.setUser(bid.getUser());
        return bidDTO;
    }

    public AuctionDTO registerBid(long auctionId, BidCreateResource bidCreateResource) {
        Auction auction = auctionDao.findAuctionById(auctionId).orElseThrow(() -> new AuctionNotFoundException("Unable to find Auction with id [" + auctionId + "]"));
        User user = userDao.findUserByEmail(bidCreateResource.getUserEmail()).orElseThrow(() -> new UserNotFoundException("Unable to find User with email [" + bidCreateResource.getUserEmail() + "]"));
        Bid higestBid = auction.findHighestBid();
        if (bidCreateResource.getAmount() < higestBid.getAmount()) {
            throw new InvalidBidException("Bid cannot be lower than highest bid.");
        }
        if (bidCreateResource.getAmount() == higestBid.getAmount() && user.equals(higestBid.getUser())) {
            throw new InvalidBidException("User [" + user.getEmail() + "] already has the highest bid.");
        }
        if (auction.isFinished()) {
            throw new InvalidBidException("Cannot make a bid on this auction as it is already closed.");
        }
        Bid bid = new Bid(user, LocalDate.now(), bidCreateResource.getAmount());
        auction.addBid(bid);
        return mapToAuctionResource(auction);
    }
}

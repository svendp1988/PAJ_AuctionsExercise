package be.pxl.auctions.rest.resource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AuctionCreateResource {
    private String description;
    private String endDate;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}

package ru.axothy.airline.model.dto;

import ru.axothy.airline.model.db.Town;

public class CreateTicketRq {
    private TicketType type;
    private Town departureTown;
    private Town arrivalTown;


    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public Town getDepartureTown() {
        return departureTown;
    }

    public void setDepartureTown(Town departureTown) {
        this.departureTown = departureTown;
    }

    public Town getArrivalTown() {
        return arrivalTown;
    }

    public void setArrivalTown(Town arrivalTown) {
        this.arrivalTown = arrivalTown;
    }
}

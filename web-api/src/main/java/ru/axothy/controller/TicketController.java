package ru.axothy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.axothy.model.db.Ticket;
import ru.axothy.model.dto.CreateTicketRq;
import ru.axothy.model.dto.TicketType;
import ru.axothy.service.TicketService;
import ru.axothy.service.TownService;

@RestController
@RequestMapping(value = "/ticket")
public class TicketController {
    private final TicketService ticketService;
    private final TownService townService;

    public TicketController(TicketService ticketService, TownService townService) {
        this.ticketService = ticketService;
        this.townService = townService;
    }

    @GetMapping
    public ResponseEntity<Ticket> getTicketById(@RequestParam ("ticketId") Long ticketId) {
        Ticket result = ticketService.getTicketById(ticketId);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Ticket> createNewTicket(
            @RequestParam ("type") TicketType type,
            @RequestParam ("departureTownId") Long departureTownId,
            @RequestParam ("arrivalTownId") Long arrivalTownId)
    {
        Ticket ticket = new Ticket();
        ticket.setType(type);
        ticket.setDepartureTown(townService.getTownById(departureTownId));
        ticket.setArrivalTown(townService.getTownById(arrivalTownId));

        Ticket result = ticketService.createTicket(ticket);
        return ResponseEntity.ok(result);
    }

    private Ticket toTicketEntity(CreateTicketRq createTicketRq) {
        Ticket ticket = new Ticket();
        ticket.setType(createTicketRq.getType());

        return ticket;
    }

}

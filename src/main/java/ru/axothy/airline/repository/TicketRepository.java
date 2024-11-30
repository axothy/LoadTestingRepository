package ru.axothy.airline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.axothy.airline.model.db.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

}

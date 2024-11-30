package ru.axothy.airline.appender;

import ru.axothy.airline.model.db.Ticket;
import ru.axothy.airline.model.dto.TicketType;

public class TicketsAggregator {
    private static final int HUNDRED = 100;

    private static long numberOfStandardTickets = 0;

    private static long numberOfBusinessTickets = 0;

    private static int businessTicketsPercent = 0;


    /**
     * Обновляет процент билетов бизнес-класса из всех имеющихся билетов
     * @param ticket
     */
    public static void aggregate(Ticket ticket) {
        TicketType type = ticket.getType();

        if (type == TicketType.BUSINESS) {
            numberOfBusinessTickets++;
        } else {
            numberOfStandardTickets++;
        }

        businessTicketsPercent = countPercentOfBusinessTickets();
    }

    private static int countPercentOfBusinessTickets() {
        long totalTickets = numberOfStandardTickets + numberOfBusinessTickets;
        if (totalTickets == 0) {
            return 0;
        }

        return (int) ((numberOfBusinessTickets * HUNDRED) / totalTickets);
    }

    public static long getNumberOfStandardTickets() {
        return numberOfStandardTickets;
    }

    public static long getNumberOfBusinessTickets() {
        return numberOfBusinessTickets;
    }

    public static int getBusinessTicketsPercent() {
        return businessTicketsPercent;
    }
}

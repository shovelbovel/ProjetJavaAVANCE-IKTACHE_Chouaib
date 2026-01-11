package com.example.util;

import com.example.entity.Reservation;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvExporter {

    @SuppressWarnings("deprecation")
    public static void exportReservationsToCsv(List<Reservation> reservations, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.builder()
                 .setHeader("ID", "User", "Ticket ID", "Category", "Price", "Status", "Match", "Reservation Date")
                 .build())) {
            for (Reservation res : reservations) {
                csvPrinter.printRecord(
                    res.getId(),
                    res.getUser().getName(),
                    res.getTicket().getId(),
                    res.getTicket().getCategory(),
                    res.getTicket().getPrice(),
                    res.getTicket().getStatus(),
                    res.getTicket().getMatch().getTeam1() + " vs " + res.getTicket().getMatch().getTeam2(),
                    res.getReservationDate()
                );
            }
        }
    }
}
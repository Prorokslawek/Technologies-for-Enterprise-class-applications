package lab.app;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lab.dto.ComplaintDTO;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Long testId = 152L;

        Client client = ClientBuilder.newClient();
        String baseUrl = "http://localhost:8080/Server-1.0-SNAPSHOT/api/complaints";

        System.out.println("Pobranie statusu");
        String status = client.target(baseUrl + "/" + testId + "/status")
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);
        System.out.println("Status: " + status);

        System.out.println("\nPobranie wszystkich skarg");
        List<ComplaintDTO> all = client.target(baseUrl)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<ComplaintDTO>>() {});
        all.forEach(c -> System.out.println(c.getId() + " - " + c.getComplaintText()));

        System.out.println("\nPobranie jednej otwartej skargi");
        ComplaintDTO complaint = client.target(baseUrl + "/" + testId)
                .request(MediaType.APPLICATION_JSON)
                .get(ComplaintDTO.class);
        System.out.println("Pobrano: " + complaint.getComplaintText() + " | Status: " + complaint.getStatus());

        System.out.println("\nModyfikaca skargi na zamkniętą ===");
        complaint.setStatus("closed");
        Response putResponse = client.target(baseUrl + "/" + testId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(complaint, MediaType.APPLICATION_JSON));
        System.out.println("Kod statusu po aktualizacji :" + putResponse.getStatus());

        System.out.println("\nPobranie tylko otwartych skarg");
        List<ComplaintDTO> openComplaints = client.target(baseUrl)
                .queryParam("status", "open") // Wykorzystuje filtrację z ćw 1
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<ComplaintDTO>>() {});
        openComplaints.forEach(c -> System.out.println(c.getId() + " - " + c.getComplaintText()));

        client.close();
    }
}
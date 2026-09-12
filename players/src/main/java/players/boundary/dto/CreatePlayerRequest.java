package players.boundary.dto;

public record CreatePlayerRequest(String firstname, String lastname, String country, int age) {
}

package se.sofiekl.userorderservice.DTO;

public record AuthResponse(
        String token,
        String type,
        String username
) {}

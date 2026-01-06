package com.julian.razif.kanban.application.user.dto;

public record LoginResponse(String access_token, Long id, String email, String organization) {
}

package pt.isec.mei.das.dto;

import java.time.LocalDateTime;

public record ProjectDTO(Long id, String name, LocalDateTime createdAt) {}

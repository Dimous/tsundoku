package io.github.dimous.tsundoku.data.dto;

public record ConfigDTO(String driver, String dialect, String url, String user, String password, String base_path, String extensions) {}

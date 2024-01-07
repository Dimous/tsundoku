package io.github.dimous.tsundoku.data.dto;

public record BookDTO(String path, String name, String extension, String hash, long size, String content) {}

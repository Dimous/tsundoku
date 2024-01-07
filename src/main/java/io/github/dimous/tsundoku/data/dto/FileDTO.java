package io.github.dimous.tsundoku.data.dto;

import com.google.common.io.ByteSource;

public record FileDTO(String path, String name, String extension, String hash, long size, ByteSource source) {}

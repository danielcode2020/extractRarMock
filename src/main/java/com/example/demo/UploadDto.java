package com.example.demo;

import javax.validation.constraints.NotNull;

public record UploadDto(
        @NotNull
        String name,
        @NotNull
        String data) {
}

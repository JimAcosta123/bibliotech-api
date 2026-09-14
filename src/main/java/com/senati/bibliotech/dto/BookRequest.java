package com.senati.bibliotech.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookRequest {

    @NotBlank(message = "El titulo es obligatorio")
    private String title;

    @NotBlank(message = "El autor es obligatorio")
    private String author;

    private String isbn;
    private String genre;

    @Min(value = 1, message = "Debe haber al menos 1 copia")
    private Integer totalCopies;
}

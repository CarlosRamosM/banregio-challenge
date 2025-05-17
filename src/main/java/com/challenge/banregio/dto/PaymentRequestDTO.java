package com.challenge.banregio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(
    name = "PaymentRequest",
    description = "Datos para realizar un pago"
)
public record PaymentRequestDTO(
    @Schema(
        name = "interestRate",
        description = "Tasa de interés",
        example = "0.05"
    )
    @NotNull(message = "La tasa de interés no puede ser nula")
    @Positive(message = "La tasa de interés debe ser mayor que cero")
    float interestRate,

    @Schema(
        name = "fee",
        description = "Tarifa",
        example = "10.0"
    )
    @NotNull(message = "La tarifa no puede ser nula")
    @Min(value = 0, message = "La tarifa debe ser mayor o igual a cero")
    float fee,

    @Schema(
        name = "businessYear",
        description = "Días del año fiscal.",
        example = "360"
    )
    @NotNull(message = "El año fiscal no puede ser nulo")
    @Positive(message = "El año fiscal debe ser mayor que cero")
    int businessYear
) {}

package com.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
@Data
public class CreateUserDTO {
    @NotNull
    private Integer id;

    @NotBlank
    @JsonProperty("firstname")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @Min(value = 0, message = "Age must be a positive number")
    private Integer age;

    private Boolean subscriptionStatus;

    private String postalCode;

    private LocalDateTime createdAt;
}

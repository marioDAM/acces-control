package com.example.controller;

import com.example.dto.CreateUserDTO;
import com.example.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
public interface UserApi {
    @Operation(summary = "Crear un nuevo usuario", description = "Crea un nuevo usuario en el sistema.", operationId = "createUser", tags = {"Users"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    ResponseEntity<User> createUser(@RequestBody CreateUserDTO user);

    @Operation(summary = "Obtener un usuario por ID", description = "Devuelve los detalles de un usuario específico por ID.", operationId = "getUserById", tags = {"Users"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    ResponseEntity<User> getUserById(@PathVariable Long id);

    @Operation(
            summary = "Filtrar usuarios",
            description = "Obtiene una lista paginada de usuarios filtrados por nombre y/o edad. "
                    + "Incluye parámetros de paginación como número de página (`page`) y tamaño de página (`size`).",
            operationId = "filterUsers",
            tags = {"Users"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios filtrados encontrados"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (falta de parámetros)"),
            @ApiResponse(responseCode = "404", description = "No se encontraron usuarios con los filtros proporcionados")
    })
    @GetMapping("/filter")
    ResponseEntity<Page<User>> filterUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer age,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );
}

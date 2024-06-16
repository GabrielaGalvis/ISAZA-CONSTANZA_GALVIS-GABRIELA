package com.backend.clinica_odontologica.dto.entrada;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class OdontologoEntradaDto {

    @Pattern(regexp ="^[A-Z]{3}-\\d{3}$",message = "Debe tener el formato : ABC-123")
    @NotBlank(message = "Debe especificarse el número de matrícula del odontologo")
    @Size(max = 7,min = 7, message = "La matricula debe tener 7 caracteres")
    private String numeroDeMatricula;

    @NotBlank(message = "Debe especificarse el nombre del odontologo")
    @Size(max = 50, message = "El nombre puede tener hasta 50 caracteres")
    private String nombre;

    @NotBlank(message = "Debe especificarse el apellido del odontologo")
    @Size(max = 50, message = "El apellido puede tener hasta 50 caracteres")
    private String apellido;

    public OdontologoEntradaDto() {
    }

    public OdontologoEntradaDto(String numeroDeMatricula, String nombre, String apellido) {
        this.numeroDeMatricula = numeroDeMatricula;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getNumeroDeMatricula() {
        return numeroDeMatricula;
    }

    public void setNumeroDeMatricula(String numeroDeMatricula) {
        this.numeroDeMatricula = numeroDeMatricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}

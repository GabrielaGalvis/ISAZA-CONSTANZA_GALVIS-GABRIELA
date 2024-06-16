package com.backend.clinica_odontologica.dto.entrada;


import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class TurnoEntradaDto {

    @Positive(message = "El id del paciente no puede ser nulo o menor a cero")
    private Long idPacienteEntradaDto;

    @Positive(message = "El id del odontologo no puede ser nulo o menor a cero")
    private Long idOdontologoEntradaDto;

    @FutureOrPresent(message = "La fecha no puede ser anterior al día de hoy")
    @NotNull(message = "Debe especificarse la fecha y hora del turno")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaYHora;

    public TurnoEntradaDto() {
    }

    public TurnoEntradaDto(Long idPacienteEntradaDto, Long idOdontologoEntradaDto, LocalDateTime fechaYHora) {
        this.idPacienteEntradaDto = idPacienteEntradaDto;
        this.idOdontologoEntradaDto = idOdontologoEntradaDto;
        this.fechaYHora = fechaYHora;
    }

    public @Positive(message = "El id del paciente no puede ser nulo o menor a cero") Long getIdPacienteEntradaDto() {
        return idPacienteEntradaDto;
    }

    public void setIdPacienteEntradaDto(@Positive(message = "El id del paciente no puede ser nulo o menor a cero") Long idPacienteEntradaDto) {
        this.idPacienteEntradaDto = idPacienteEntradaDto;
    }

    public @Positive(message = "El id del odontologo no puede ser nulo o menor a cero") Long getIdOdontologoEntradaDto() {
        return idOdontologoEntradaDto;
    }

    public void setIdOdontologoEntradaDto(@Positive(message = "El id del odontologo no puede ser nulo o menor a cero") Long idOdontologoEntradaDto) {
        this.idOdontologoEntradaDto = idOdontologoEntradaDto;
    }

    public @FutureOrPresent(message = "La fecha no puede ser anterior al día de hoy") @NotNull(message = "Debe especificarse la fecha y hora del turno") LocalDateTime getFechaYHora() {
        return fechaYHora;
    }

    public void setFechaYHora(@FutureOrPresent(message = "La fecha no puede ser anterior al día de hoy") @NotNull(message = "Debe especificarse la fecha y hora del turno") LocalDateTime fechaYHora) {
        this.fechaYHora = fechaYHora;
    }
}

package com.backend.clinica_odontologica.dto.salida;

import com.backend.clinica_odontologica.entity.Odontologo;
import com.backend.clinica_odontologica.entity.Paciente;

import java.time.LocalDateTime;

public class TurnoSalidaDto {

    private Long id;
    private Long idPaciente;
    private String nombreCompletoPaciente;
    private Long idOdontologo;
    private String nombreCompletoOdontologo;
    private LocalDateTime fechaYHora;

    public TurnoSalidaDto() {
    }

    public TurnoSalidaDto(Long id, Long idPaciente, String nombreCompletoPaciente, Long idOdontologo, String nombreCompletoOdontologo, LocalDateTime fechaYHora) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.nombreCompletoPaciente = nombreCompletoPaciente;
        this.idOdontologo = idOdontologo;
        this.nombreCompletoOdontologo = nombreCompletoOdontologo;
        this.fechaYHora = fechaYHora;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getNombreCompletoPaciente() {
        return nombreCompletoPaciente;
    }

    public void setNombreCompletoPaciente(String nombreCompletoPaciente) {
        this.nombreCompletoPaciente = nombreCompletoPaciente;
    }

    public Long getIdOdontologo() {
        return idOdontologo;
    }

    public void setIdOdontologo(Long idOdontologo) {
        this.idOdontologo = idOdontologo;
    }

    public String getNombreCompletoOdontologo() {
        return nombreCompletoOdontologo;
    }

    public void setNombreCompletoOdontologo(String nombreCompletoOdontologo) {
        this.nombreCompletoOdontologo = nombreCompletoOdontologo;
    }

    public LocalDateTime getFechaYHora() {
        return fechaYHora;
    }

    public void setFechaYHora(LocalDateTime fechaYHora) {
        this.fechaYHora = fechaYHora;
    }
}

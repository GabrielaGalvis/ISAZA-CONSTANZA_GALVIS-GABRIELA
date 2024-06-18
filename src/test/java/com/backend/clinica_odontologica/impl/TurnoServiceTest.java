package com.backend.clinica_odontologica.impl;

import com.backend.clinica_odontologica.dto.entrada.DomicilioEntradaDto;
import com.backend.clinica_odontologica.dto.entrada.OdontologoEntradaDto;
import com.backend.clinica_odontologica.dto.entrada.PacienteEntradaDto;
import com.backend.clinica_odontologica.dto.entrada.TurnoEntradaDto;
import com.backend.clinica_odontologica.dto.salida.OdontologoSalidaDto;
import com.backend.clinica_odontologica.dto.salida.PacienteSalidaDto;
import com.backend.clinica_odontologica.dto.salida.TurnoSalidaDto;
import com.backend.clinica_odontologica.exceptions.BadRequestException;
import com.backend.clinica_odontologica.service.impl.OdontologoService;
import com.backend.clinica_odontologica.service.impl.PacienteService;
import com.backend.clinica_odontologica.service.impl.TurnoService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class TurnoServiceTest {

    @Autowired
    private TurnoService turnoService;
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private OdontologoService odontologoService;


    @Test
    @Order(1)
    void deberiaRegistrarseUnTurnoSiExisteElOdontologoYElPaciente_yRetornarSuId() throws BadRequestException {

        PacienteEntradaDto pacienteEntradaDto = new PacienteEntradaDto("Juan", "Perez", 123456, LocalDate.of(2024, 6, 22), new DomicilioEntradaDto("Calle", 123, "Localidad", "Provincia"));
        PacienteSalidaDto pacienteSalidaDto = pacienteService.registrarPaciente(pacienteEntradaDto);

        OdontologoEntradaDto odontologoEntradaDto = new OdontologoEntradaDto("ABC-123","Daniel","Torres");
        OdontologoSalidaDto odontologoSalidaDto = odontologoService.registrarOdontologo(odontologoEntradaDto);

        TurnoEntradaDto turnoEntradaDto = new TurnoEntradaDto(pacienteSalidaDto.getId(),odontologoSalidaDto.getId(), LocalDateTime.of(2024, 7, 21, 15, 30));

        TurnoSalidaDto turnoSalidaDto = turnoService.registrarTurno(turnoEntradaDto);

        //assert
        assertNotNull(turnoSalidaDto);
        assertNotNull(turnoSalidaDto.getId());
        assertEquals("Juan Perez", turnoSalidaDto.getNombreCompletoPaciente());
    }


    @Test
    @Order(2)
    void deberiaDevolverUnaListaNoVaciaDeTurnos(){
        List<TurnoSalidaDto> listadoDeTurnos= turnoService.listarTurnos();
        assertFalse(listadoDeTurnos.isEmpty());
    }

    @Test
    @Order(3)
    void deberiaEliminarseElTurnoConId1(){

        assertDoesNotThrow(() -> turnoService.eliminarTurno(1L));
    }

    @Test
    @Order(4)
    void deberiaDevolverUnaListaVaciaDeTurnos(){
        List<TurnoSalidaDto> listadoDeTurnos= turnoService.listarTurnos();
        assertTrue(listadoDeTurnos.isEmpty());
    }
}
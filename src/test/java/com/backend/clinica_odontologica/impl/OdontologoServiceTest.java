package com.backend.clinica_odontologica.impl;

import com.backend.clinica_odontologica.dto.entrada.OdontologoEntradaDto;
import com.backend.clinica_odontologica.dto.salida.OdontologoSalidaDto;
import com.backend.clinica_odontologica.service.impl.OdontologoService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class OdontologoServiceTest {

    @Autowired
    private OdontologoService odontologoService;


    @Test
    @Order(1)
    void deberiaRegistrarseUnOdontologoDeNombreDaniel_yRetornarSuId(){

        OdontologoEntradaDto odontologoEntradaDto = new OdontologoEntradaDto("ABC-123","Daniel","Torres");

        OdontologoSalidaDto odontologoSalidaDto = odontologoService.registrarOdontologo(odontologoEntradaDto);


        assertNotNull(odontologoSalidaDto);
        assertNotNull(odontologoSalidaDto.getId());
        assertEquals("Daniel", odontologoSalidaDto.getNombre());
    }


    @Test
    @Order(2)
    void deberiaDevolverUnaListaNoVaciaDeOdontologos(){
        List<OdontologoSalidaDto> listadoDeOdontologos= odontologoService.listarOdontologos();
        assertFalse(listadoDeOdontologos.isEmpty());
    }

    @Test
    @Order(3)
    void deberiaEliminarseElOdontologoConId1(){

        assertDoesNotThrow(() -> odontologoService.eliminarOdontologo(1L));
    }

    @Test
    @Order(4)
    void deberiaDevolverUnaListaVaciaDeOdontologos(){
        List<OdontologoSalidaDto> listadoDeOdontologos= odontologoService.listarOdontologos();
        assertTrue(listadoDeOdontologos.isEmpty());
    }

}
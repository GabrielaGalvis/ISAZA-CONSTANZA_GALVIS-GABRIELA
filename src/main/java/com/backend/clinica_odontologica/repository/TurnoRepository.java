package com.backend.clinica_odontologica.repository;

import com.backend.clinica_odontologica.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurnoRepository extends JpaRepository<Turno, Long> {

        //@Query("SELECT p FROM Paciente p WHERE p.dni = ?1") HQL
        //@Query(value = "SELECT * FROM PACIENTES WHERE DNI = ?1", nativeQuery = true) SQL
        //Paciente findByDni(int dni);
        //Paciente findByDniAndNombre(int dni, String nombre);

}

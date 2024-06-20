package com.backend.clinica_odontologica.service.impl;

import com.backend.clinica_odontologica.dto.entrada.OdontologoEntradaDto;
import com.backend.clinica_odontologica.dto.entrada.PacienteEntradaDto;
import com.backend.clinica_odontologica.dto.entrada.TurnoEntradaDto;
import com.backend.clinica_odontologica.dto.salida.OdontologoSalidaDto;
import com.backend.clinica_odontologica.dto.salida.PacienteSalidaDto;
import com.backend.clinica_odontologica.dto.salida.TurnoSalidaDto;
import com.backend.clinica_odontologica.entity.Odontologo;
import com.backend.clinica_odontologica.entity.Paciente;
import com.backend.clinica_odontologica.entity.Turno;
import com.backend.clinica_odontologica.exceptions.BadRequestException;
import com.backend.clinica_odontologica.exceptions.ResourceNotFoundException;
import com.backend.clinica_odontologica.repository.TurnoRepository;
import com.backend.clinica_odontologica.service.IOdontologoService;
import com.backend.clinica_odontologica.service.IPacienteService;
import com.backend.clinica_odontologica.service.ITurnoService;
import com.backend.clinica_odontologica.utils.JsonPrinter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class TurnoService implements ITurnoService {

    private final Logger LOGGER = LoggerFactory.getLogger(TurnoService.class);
    private TurnoRepository turnoRepository;
    private ModelMapper modelMapper;
    private IPacienteService pacienteService;
    private IOdontologoService odontologoService;
    @PersistenceContext
    private EntityManager entityManager;

    public TurnoService(TurnoRepository turnoRepository, ModelMapper modelMapper, IPacienteService pacienteService, IOdontologoService odontologoService) {
        this.turnoRepository = turnoRepository;
        this.modelMapper = modelMapper;
        this.pacienteService = pacienteService;
        this.odontologoService = odontologoService;
        configureMapping();
    }

    @Override
    @Transactional
    public TurnoSalidaDto registrarTurno(TurnoEntradaDto turnoEntradaDto) throws BadRequestException {

        TurnoSalidaDto turnoSalidaDto= null;
        LOGGER.info("Turno entrada: {}", JsonPrinter.toString(turnoEntradaDto));

        PacienteSalidaDto pacienteEncontrado = pacienteService.buscarPacientePorId(turnoEntradaDto.getIdPacienteEntradaDto());
        OdontologoSalidaDto odontologoEncontrado = odontologoService.buscarOdontologoPorId(turnoEntradaDto.getIdOdontologoEntradaDto());

        if(pacienteEncontrado!=null && odontologoEncontrado!=null){
            LOGGER.info("PacienteSalidaDto encontrado: {}", JsonPrinter.toString(pacienteEncontrado));
            LOGGER.info("OdontologoSalidaDto encontrado: {}", JsonPrinter.toString(odontologoEncontrado));


            Turno turnoARegistrar = deDtoAEntidad(pacienteEncontrado,odontologoEncontrado,turnoEntradaDto);

            Turno turnoRegistrado = turnoRepository.save(turnoARegistrar);
            LOGGER.info("Turno registrado: {}", JsonPrinter.toString(turnoRegistrado));


            turnoSalidaDto= deEntidadADto(turnoRegistrado);
            LOGGER.info("TurnoSalidaDto: {}", JsonPrinter.toString(turnoSalidaDto));
        } else if (pacienteEncontrado==null) {
            LOGGER.error("No fue posible registrar el turno ya que no existe el paciente con id {}",turnoEntradaDto.getIdPacienteEntradaDto());
            throw new BadRequestException("No existe registro de paciente con id "+ turnoEntradaDto.getIdPacienteEntradaDto());

        }else
        {
            LOGGER.error("No fue posible registrar el turno ya que no existe el odontologo con id {}",turnoEntradaDto.getIdOdontologoEntradaDto());
            throw new BadRequestException("No existe registro de odontologo con id "+ turnoEntradaDto.getIdOdontologoEntradaDto());
        }

        return turnoSalidaDto;
    }

    @Override
    public TurnoSalidaDto buscarTurnoPorId(Long id) {

        Turno turnoBuscado = turnoRepository.findById(id).orElse(null);
        TurnoSalidaDto turnoEncontrado = null;

        if (turnoBuscado != null){
            turnoEncontrado = deEntidadADto(turnoBuscado);
            LOGGER.info("Turno encontrado: {}", JsonPrinter.toString(turnoEncontrado));
        } else LOGGER.error("No se ha encontrado el turno con id {}", id);

        return turnoEncontrado;
    }

    @Override
    public List<TurnoSalidaDto> listarTurnos(){

        List<TurnoSalidaDto> turnos = turnoRepository.findAll()
                .stream()
                .map(this::deEntidadADto)
                .toList();

        LOGGER.info("Listado de todos los turnos: {}", JsonPrinter.toString(turnos));
        return turnos;
    }

    @Override
    public void eliminarTurno(Long id) throws ResourceNotFoundException {
        if(buscarTurnoPorId(id) != null){
            turnoRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el turno con id {}", id);
        }  else {
            LOGGER.error("No fue posible eliminar el turno porque no existe registro con id {}", id);
            throw new ResourceNotFoundException("No existe registro de turno con id " + id);
        }
    }

    @Override
    @Transactional
    public TurnoSalidaDto actualizarTurno(TurnoEntradaDto turnoEntradaDto, Long id) throws ResourceNotFoundException{
        PacienteSalidaDto pacienteRecibido = pacienteService.buscarPacientePorId(turnoEntradaDto.getIdPacienteEntradaDto());
        LOGGER.info("Paciente recibido: {}", JsonPrinter.toString(pacienteRecibido));
        OdontologoSalidaDto odontologoRecibido = odontologoService.buscarOdontologoPorId(turnoEntradaDto.getIdOdontologoEntradaDto());
        LOGGER.info("Odontologo recibido: {}", JsonPrinter.toString(odontologoRecibido));
        Turno turnoAActualizar = turnoRepository.findById(id).orElse(null);
        TurnoSalidaDto turnoSalidaDto = null;

        if(turnoAActualizar != null){
            Turno turnoRecibido = deDtoAEntidad(pacienteRecibido,odontologoRecibido,turnoEntradaDto);
            turnoRecibido.setId(turnoAActualizar.getId());

            turnoAActualizar = turnoRecibido;
            LOGGER.info("Turno a actualizar: {}", JsonPrinter.toString(turnoAActualizar));

            turnoRepository.save(turnoAActualizar);
            turnoSalidaDto = deEntidadADto(turnoAActualizar);
            LOGGER.warn("Turno actualizado: {}", JsonPrinter.toString(turnoSalidaDto));

        } else {
            LOGGER.error("No fue posible actualizar el turno porque no existe registro con id {}", id);
            throw new ResourceNotFoundException("No existe registro de turno con id " + id);
        }

        return turnoSalidaDto;
    }

    private void configureMapping(){

        modelMapper.typeMap(PacienteSalidaDto.class, Paciente.class)
                .addMappings(mapper -> mapper.map(PacienteSalidaDto::getDomicilioSalidaDto, Paciente::setDomicilio));

        modelMapper.typeMap(OdontologoSalidaDto.class, Odontologo.class);

        modelMapper.typeMap(PacienteEntradaDto.class, Paciente.class)
                .addMappings(mapper -> mapper.map(PacienteEntradaDto::getDomicilioEntradaDto, Paciente::setDomicilio));

        modelMapper.typeMap(OdontologoEntradaDto.class, Odontologo.class);
    }

    private TurnoSalidaDto deEntidadADto(Turno turnoEntidad) {
        TurnoSalidaDto turnoSalidaDto = new TurnoSalidaDto();
        turnoSalidaDto.setId(turnoEntidad.getId());
        turnoSalidaDto.setIdPaciente(turnoEntidad.getPaciente().getId());
        turnoSalidaDto.setNombreCompletoPaciente(turnoEntidad.getPaciente().getNombre()+" "+turnoEntidad.getPaciente().getApellido());
        turnoSalidaDto.setIdOdontologo(turnoEntidad.getOdontologo().getId());
        turnoSalidaDto.setNombreCompletoOdontologo(turnoEntidad.getOdontologo().getNombre()+" "+turnoEntidad.getOdontologo().getApellido());
        turnoSalidaDto.setFechaYHora(turnoEntidad.getFechaYHora());
        return turnoSalidaDto;
    }

    private Turno deDtoAEntidad(PacienteSalidaDto pacienteEncontrado, OdontologoSalidaDto odontologoEncontrado, TurnoEntradaDto turnoEntradaDto){

        Turno turnoARegistrar= new Turno();

        Paciente paciente = modelMapper.map(pacienteEncontrado,Paciente.class);
        LOGGER.info("Paciente entidad: {}", JsonPrinter.toString(paciente));

        paciente = entityManager.merge(paciente);

        turnoARegistrar.setPaciente(paciente);

        Odontologo odontologo = modelMapper.map(odontologoEncontrado,Odontologo.class);
        LOGGER.info("Odontologo entidad: {}", JsonPrinter.toString(odontologoEncontrado));

        odontologo = entityManager.merge(odontologo);

        turnoARegistrar.setOdontologo(odontologo);

        turnoARegistrar.setFechaYHora(turnoEntradaDto.getFechaYHora());
        LOGGER.info("Turno a registrar entidad: {}", JsonPrinter.toString(turnoARegistrar));
        return turnoARegistrar;
    }
}

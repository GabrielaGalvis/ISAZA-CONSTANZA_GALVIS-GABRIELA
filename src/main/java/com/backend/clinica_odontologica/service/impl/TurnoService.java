package com.backend.clinica_odontologica.service.impl;

import com.backend.clinica_odontologica.dto.entrada.PacienteEntradaDto;
import com.backend.clinica_odontologica.dto.entrada.TurnoEntradaDto;
import com.backend.clinica_odontologica.dto.salida.OdontologoSalidaDto;
import com.backend.clinica_odontologica.dto.salida.PacienteSalidaDto;
import com.backend.clinica_odontologica.dto.salida.TurnoSalidaDto;
import com.backend.clinica_odontologica.entity.Odontologo;
import com.backend.clinica_odontologica.entity.Paciente;
import com.backend.clinica_odontologica.entity.Turno;
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
    public TurnoSalidaDto registrarTurno(TurnoEntradaDto turnoEntradaDto) {
        //logica de negocio
        TurnoSalidaDto turnoSalidaDto= null;
        LOGGER.info("Turno entrada: {}", JsonPrinter.toString(turnoEntradaDto));
        //buscamos si el paciente existe
        PacienteSalidaDto pacienteEncontrado = pacienteService.buscarPacientePorId(turnoEntradaDto.getIdPacienteEntradaDto());
        OdontologoSalidaDto odontologoEncontrado = odontologoService.buscarOdontologoPorId(turnoEntradaDto.getIdOdontologoEntradaDto());

        if(pacienteEncontrado!=null && odontologoEncontrado!=null){
            LOGGER.info("PacienteSalidaDto encontrado: {}", JsonPrinter.toString(pacienteEncontrado));
            LOGGER.info("OdontologoSalidaDto encontrado: {}", JsonPrinter.toString(odontologoEncontrado));

            //creamos la entidad turno
            Turno turnoARegistrar = new Turno();
            //mapeamos de dto a entidad
            //de pacienteSalidaDto a Paciente
            Paciente paciente = modelMapper.map(pacienteEncontrado,Paciente.class);
            LOGGER.info("Paciente entidad: {}", JsonPrinter.toString(paciente));
            //reanudamos la persistencia
            paciente = entityManager.merge(paciente);
            //seteamos
            turnoARegistrar.setPaciente(paciente);
            //de odontologoSalidaDto a Odontologo
            Odontologo odontologo = modelMapper.map(odontologoEncontrado,Odontologo.class);
            LOGGER.info("Odontologo entidad: {}", JsonPrinter.toString(odontologoEncontrado));
            //reanudamos la persistencia
            odontologo = entityManager.merge(odontologo);
            //seteamos
            turnoARegistrar.setOdontologo(odontologo);
            //fecha y hora
            turnoARegistrar.setFechaYHora(turnoEntradaDto.getFechaYHora());
            LOGGER.info("Turno a registrar entidad: {}", JsonPrinter.toString(turnoARegistrar));

            //registramos el turno
            Turno turnoRegistrado = turnoRepository.save(turnoARegistrar);
            LOGGER.info("Turno registrado: {}", JsonPrinter.toString(turnoRegistrado));

            //mapeo de entidad a dto
            turnoSalidaDto= modelMapper.map(turnoRegistrado,TurnoSalidaDto.class);
            LOGGER.info("TurnoSalidaDto: {}", JsonPrinter.toString(turnoSalidaDto));
        } else if (pacienteEncontrado==null) {
            LOGGER.error("No fue posible registrar el turno ya que no existe el paciente");
        }else
        {
            LOGGER.error("No fue posible registrar el turno ya que no existe el odontologo");
        }

        return turnoSalidaDto;
    }

    @Override
    public TurnoSalidaDto buscarTurnoPorId(Long id) {

        Turno turnoBuscado = turnoRepository.findById(id).orElse(null);
        TurnoSalidaDto turnoEncontrado = null;

        if (turnoBuscado != null){
            turnoEncontrado = modelMapper.map(turnoBuscado, TurnoSalidaDto.class);
            LOGGER.info("Turno encontrado: {}", JsonPrinter.toString(turnoEncontrado));
        } else LOGGER.error("No se ha encontrado el turno con id {}", id);

        return turnoEncontrado;
    }

    @Override
    public List<TurnoSalidaDto> listarTurnos(){
        //mapeo de lista de entidades a lista de dtos
        List<TurnoSalidaDto> turnos = turnoRepository.findAll()
                .stream()
                .map(turno -> modelMapper.map(turno,TurnoSalidaDto.class))
                .toList();

        LOGGER.info("Listado de todos los turnos: {}",turnos);
        return turnos;
    }

    @Override
    public void eliminarTurno(Long id) throws ResourceNotFoundException {
        if(buscarTurnoPorId(id) != null){
            turnoRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el turno con id {}", id);
        }  else {
            throw new ResourceNotFoundException("No existe registro de turno con id " + id);
        }
    }

    @Override
    public TurnoSalidaDto actualizarTurno(TurnoEntradaDto turnoEntradaDto, Long id) {

        Turno turnoRecibido = modelMapper.map(turnoEntradaDto, Turno.class);
        Turno turnoAActualizar = turnoRepository.findById(id).orElse(null);
        TurnoSalidaDto turnoSalidaDto = null;

        if(turnoAActualizar != null){

            turnoRecibido.setId(turnoAActualizar.getId());
            turnoRecibido.getPaciente().setId(turnoAActualizar.getPaciente().getId());
            turnoRecibido.getOdontologo().setId(turnoAActualizar.getOdontologo().getId());

            turnoAActualizar = turnoRecibido;

            turnoRepository.save(turnoAActualizar);
            turnoSalidaDto = modelMapper.map(turnoAActualizar, TurnoSalidaDto.class);
            LOGGER.warn("Turno actualizado: {}", JsonPrinter.toString(turnoSalidaDto));

        } else {
            LOGGER.error("No fue posible actualizar el turno porque no se encuentra en nuestra base de datos");
            //lanzar excepcion
        }

        return turnoSalidaDto;
    }

    private void configureMapping(){
        modelMapper.typeMap(Turno.class, TurnoSalidaDto.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getPaciente().getId(), TurnoSalidaDto::setIdPaciente);
                    mapper.map(src -> src.getPaciente().getNombre()+" "+src.getPaciente().getApellido(),TurnoSalidaDto::setNombreCompletoPaciente);
                    mapper.map(src -> src.getOdontologo().getId(),TurnoSalidaDto::setIdOdontologo);
                    mapper.map(src -> src.getOdontologo().getNombre()+" "+src.getOdontologo().getApellido(),TurnoSalidaDto::setNombreCompletoOdontologo);
                });

        modelMapper.typeMap(PacienteSalidaDto.class, Paciente.class)
                .addMappings(mapper -> mapper.map(PacienteSalidaDto::getDomicilioSalidaDto, Paciente::setDomicilio));

        modelMapper.typeMap(OdontologoSalidaDto.class, Odontologo.class);
    }
}
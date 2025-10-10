package br.edu.imepac.comum.services;

import br.edu.imepac.comum.dtos.consulta.ConsultaDto;
import br.edu.imepac.comum.dtos.consulta.ConsultaRequest;
import br.edu.imepac.comum.exceptions.ResourceNotFoundException; // Import corrigido
import br.edu.imepac.comum.models.Consulta;
import br.edu.imepac.comum.models.Funcionario;
import br.edu.imepac.comum.models.Paciente;
import br.edu.imepac.comum.observability.ConsultaMetricsPublisher;
import br.edu.imepac.comum.repositories.ConsultaRepository;
import br.edu.imepac.comum.repositories.FuncionarioRepository;
import br.edu.imepac.comum.repositories.PacienteRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ModelMapper modelMapper;
    private final ObservationRegistry observationRegistry;
    private final ConsultaMetricsPublisher metricsPublisher;

    public ConsultaService(ConsultaRepository consultaRepository,
                           PacienteRepository pacienteRepository,
                           FuncionarioRepository funcionarioRepository,
                           ModelMapper modelMapper,
                           ObservationRegistry observationRegistry,
                           ConsultaMetricsPublisher metricsPublisher) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.modelMapper = modelMapper;
        this.observationRegistry = observationRegistry;
        this.metricsPublisher = metricsPublisher;
    }

    // Método auxiliar para conversão, usado internamente
    private ConsultaDto convertToDto(Consulta consulta) {
        ConsultaDto dto = modelMapper.map(consulta, ConsultaDto.class);
        if (consulta.getPaciente() != null) {
            dto.setNomePaciente(consulta.getPaciente().getNome());
        }
        if (consulta.getMedico() != null) {
            dto.setNomeMedico(consulta.getMedico().getNome());
        }
        return dto;
    }

    public ConsultaDto save(ConsultaRequest consultaRequest) {
        return Observation.createNotStarted("consultas.criar", observationRegistry)
                .lowCardinalityKeyValue("retorno", String.valueOf(consultaRequest.isERetorno()))
                .observe(() -> {
                    Paciente paciente = pacienteRepository.findById(consultaRequest.getPacienteId())
                            .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

                    Funcionario medico = funcionarioRepository.findById(consultaRequest.getMedicoId())
                            .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado"));

                    Consulta consulta = new Consulta();
                    consulta.setPaciente(paciente);
                    consulta.setMedico(medico);
                    consulta.setDataHorario(consultaRequest.getDataHorario());
                    consulta.setSintomas(consultaRequest.getSintomas());
                    consulta.setERetorno(consultaRequest.isERetorno());
                    consulta.setEstaAtiva(true);

                    Consulta savedConsulta = consultaRepository.save(consulta);
                    metricsPublisher.markCriada();
                    return convertToDto(savedConsulta);
                });
    }

    public List<ConsultaDto> findAll() {
        return Observation.createNotStarted("consultas.listar", observationRegistry)
                .observe(() -> consultaRepository.findAll().stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()));
    }

    public ConsultaDto findById(Long id) {
        return Observation.createNotStarted("consultas.buscar", observationRegistry)
                .lowCardinalityKeyValue("consulta.id", String.valueOf(id))
                .observe(() -> {
                    Consulta consulta = consultaRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada"));
                    return convertToDto(consulta);
                });
    }

    public ConsultaDto update(Long id, ConsultaRequest request) {
        return Observation.createNotStarted("consultas.atualizar", observationRegistry)
                .lowCardinalityKeyValue("consulta.id", String.valueOf(id))
                .observe(() -> {
                    Consulta consultaExistente = consultaRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada"));

                    if (request.getPacienteId() != null) {
                        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado para atualização"));
                        consultaExistente.setPaciente(paciente);
                    }

                    if (request.getMedicoId() != null) {
                        Funcionario medico = funcionarioRepository.findById(request.getMedicoId())
                                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado para atualização"));
                        consultaExistente.setMedico(medico);
                    }

                    if (request.getDataHorario() != null) {
                        consultaExistente.setDataHorario(request.getDataHorario());
                    }
                    if (request.getSintomas() != null) {
                        consultaExistente.setSintomas(request.getSintomas());
                    }

                    Consulta updatedConsulta = consultaRepository.save(consultaExistente);
                    metricsPublisher.markAtualizada();
                    return convertToDto(updatedConsulta);
                });
    }

    public void delete(Long id) {
        Observation.createNotStarted("consultas.cancelar", observationRegistry)
                .lowCardinalityKeyValue("consulta.id", String.valueOf(id))
                .observe(() -> {
                    Consulta consulta = consultaRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada"));

                    consulta.setEstaAtiva(false);
                    consultaRepository.save(consulta);
                    metricsPublisher.markCancelada();
                    return null;
                });
    }
}

package br.edu.imepac.comum.services;

import br.edu.imepac.comum.dtos.paciente.PacienteDto;
import br.edu.imepac.comum.dtos.paciente.PacienteRequest;
import br.edu.imepac.comum.exceptions.ResourceNotFoundException;
import br.edu.imepac.comum.models.Convenio;
import br.edu.imepac.comum.models.Paciente;
import br.edu.imepac.comum.observability.PacienteMetricsPublisher;
import br.edu.imepac.comum.repositories.ConvenioRepository;
import br.edu.imepac.comum.repositories.PacienteRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final ConvenioRepository convenioRepository;
    private final ModelMapper modelMapper;
    private final ObservationRegistry observationRegistry;
    private final PacienteMetricsPublisher metricsPublisher;

    public PacienteService(PacienteRepository pacienteRepository,
                           ConvenioRepository convenioRepository,
                           ModelMapper modelMapper,
                           ObservationRegistry observationRegistry,
                           PacienteMetricsPublisher metricsPublisher) {
        this.pacienteRepository = pacienteRepository;
        this.convenioRepository = convenioRepository;
        this.modelMapper = modelMapper;
        this.observationRegistry = observationRegistry;
        this.metricsPublisher = metricsPublisher;
    }

    private PacienteDto convertToDto(Paciente paciente) {
        return modelMapper.map(paciente, PacienteDto.class);
    }

    public PacienteDto save(PacienteRequest pacienteRequest) {
        return Observation.createNotStarted("pacientes.criar", observationRegistry)
                .observe(() -> {
                    Paciente paciente = modelMapper.map(pacienteRequest, Paciente.class);

                    if (pacienteRequest.isPossuiConvenio() && pacienteRequest.getConvenioId() != null) {
                        Convenio convenio = convenioRepository.findById(pacienteRequest.getConvenioId())
                                .orElseThrow(() -> new ResourceNotFoundException("Convênio não encontrado"));
                        paciente.setConvenio(convenio);
                    }

                    Paciente savedPaciente = pacienteRepository.save(paciente);
                    metricsPublisher.markCriado();
                    return convertToDto(savedPaciente);
                });
    }

    public List<PacienteDto> findAll() {
        return Observation.createNotStarted("pacientes.listar", observationRegistry)
                .observe(() -> pacienteRepository.findAll().stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()));
    }

    public PacienteDto findById(Long id) {
        return Observation.createNotStarted("pacientes.buscar", observationRegistry)
                .lowCardinalityKeyValue("paciente.id", String.valueOf(id))
                .observe(() -> {
                    Paciente paciente = pacienteRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));
                    return convertToDto(paciente);
                });
    }

    public PacienteDto update(Long id, PacienteRequest request) {
        return Observation.createNotStarted("pacientes.atualizar", observationRegistry)
                .lowCardinalityKeyValue("paciente.id", String.valueOf(id))
                .observe(() -> {
                    Paciente pacienteExistente = pacienteRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

                    modelMapper.map(request, pacienteExistente);

                    if (request.getConvenioId() != null) {
                        Convenio convenio = convenioRepository.findById(request.getConvenioId())
                                .orElseThrow(() -> new ResourceNotFoundException("Convênio não encontrado para atualização"));
                        pacienteExistente.setConvenio(convenio);
                    }

                    Paciente updatedPaciente = pacienteRepository.save(pacienteExistente);
                    metricsPublisher.markAtualizado();
                    return convertToDto(updatedPaciente);
                });
    }

    public void delete(Long id) {
        Observation.createNotStarted("pacientes.remover", observationRegistry)
                .lowCardinalityKeyValue("paciente.id", String.valueOf(id))
                .observe(() -> {
                    if (!pacienteRepository.existsById(id)) {
                        throw new ResourceNotFoundException("Paciente não encontrado");
                    }
                    pacienteRepository.deleteById(id);
                    return null;
                });
    }
}
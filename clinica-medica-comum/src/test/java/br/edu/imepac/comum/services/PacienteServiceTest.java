package br.edu.imepac.comum.services;

import br.edu.imepac.comum.dtos.paciente.PacienteDto;
import br.edu.imepac.comum.dtos.paciente.PacienteRequest;
import br.edu.imepac.comum.exceptions.ResourceNotFoundException;
import br.edu.imepac.comum.models.Convenio;
import br.edu.imepac.comum.models.Paciente;
import br.edu.imepac.comum.repositories.ConvenioRepository;
import br.edu.imepac.comum.repositories.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import br.edu.imepac.comum.observability.PacienteMetricsPublisher;
import io.micrometer.observation.ObservationRegistry;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private ConvenioRepository convenioRepository;
    @Spy
    private ModelMapper modelMapper;
    @Mock
    private PacienteMetricsPublisher metricsPublisher;

    private ObservationRegistry observationRegistry;
    private PacienteService pacienteService;

    @BeforeEach
    void setUp() {
        // CORREÇÃO: Adicionamos a mesma regra de mapeamento que temos na aplicação principal.
        // Isto "ensina" ao ModelMapper do teste como resolver a ambiguidade.
        modelMapper.addMappings(new PropertyMap<Paciente, PacienteDto>() {
            @Override
            protected void configure() {
                map().setNomeConvenio(source.getConvenio().getNomeEmpresa());
            }
        });

        observationRegistry = ObservationRegistry.create();
        pacienteService = new PacienteService(pacienteRepository, convenioRepository, modelMapper, observationRegistry, metricsPublisher);
    }

    @Test
    void testSave_ConvenioNotFound() {
        PacienteRequest request = new PacienteRequest();
        request.setPossuiConvenio(true);
        request.setConvenioId(99L);
        when(convenioRepository.findById(99L)).thenReturn(Optional.empty());
        when(modelMapper.map(request, Paciente.class)).thenReturn(new Paciente());

        assertThrows(ResourceNotFoundException.class, () -> pacienteService.save(request));
    }

    @Test
    void testUpdate() {
        PacienteRequest request = new PacienteRequest();
        request.setNome("Nome Atualizado");

        Paciente pacienteExistente = new Paciente();
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteExistente));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteExistente);

        pacienteService.update(1L, request);

        assertEquals("Nome Atualizado", pacienteExistente.getNome());
        verify(pacienteRepository).save(pacienteExistente);
    }

    @Test
    void testUpdate_PacienteNotFound() {
        PacienteRequest request = new PacienteRequest();
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> pacienteService.update(99L, request));
    }
}

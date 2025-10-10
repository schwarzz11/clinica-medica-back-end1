package br.edu.imepac.comum.services;

import br.edu.imepac.comum.dtos.consulta.ConsultaRequest;
import br.edu.imepac.comum.exceptions.ResourceNotFoundException;
import br.edu.imepac.comum.models.Consulta;
import br.edu.imepac.comum.observability.ConsultaMetricsPublisher;
import br.edu.imepac.comum.repositories.ConsultaRepository;
import br.edu.imepac.comum.repositories.FuncionarioRepository;
import br.edu.imepac.comum.repositories.PacienteRepository;
import io.micrometer.observation.ObservationRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.junit.jupiter.api.BeforeEach;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceTest {

    @Mock private ConsultaRepository consultaRepository;
    @Mock private FuncionarioRepository funcionarioRepository;
    @Mock private PacienteRepository pacienteRepository;
    @Mock private ConsultaMetricsPublisher metricsPublisher;
    @Spy private ModelMapper modelMapper;

    private ObservationRegistry observationRegistry;
    private ConsultaService consultaService;

    @BeforeEach
    void setUp() {
        observationRegistry = ObservationRegistry.create();
        consultaService = new ConsultaService(consultaRepository, pacienteRepository, funcionarioRepository, modelMapper, observationRegistry, metricsPublisher);
    }

    @Test
    void testUpdate_MedicoNotFound() {
        ConsultaRequest request = new ConsultaRequest();
        request.setMedicoId(99L);
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(new Consulta()));
        when(funcionarioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> consultaService.update(1L, request));
    }

    @Test
    void testDelete() {
        Consulta consulta = new Consulta();
        consulta.setEstaAtiva(true);

        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consulta);

        consultaService.delete(1L);

        assertFalse(consulta.isEstaAtiva());
        verify(consultaRepository).save(consulta);
    }
}
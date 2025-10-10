package br.edu.imepac.comum.services;

import br.edu.imepac.comum.dtos.prontuario.ProntuarioRequest;
import br.edu.imepac.comum.exceptions.ResourceNotFoundException;
import br.edu.imepac.comum.models.Consulta;
import br.edu.imepac.comum.models.Funcionario;
import br.edu.imepac.comum.models.Paciente;
import br.edu.imepac.comum.models.Prontuario;
import br.edu.imepac.comum.repositories.ConsultaRepository;
import br.edu.imepac.comum.repositories.ProntuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProntuarioServiceTest {

    @Mock
    private ProntuarioRepository prontuarioRepository;

    @Mock
    private ConsultaRepository consultaRepository;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private ProntuarioService prontuarioService;

    @Test
    void deveSalvarProntuarioAssociandoConsulta() {
        Consulta consulta = new Consulta();
        consulta.setId(3L);
        Paciente paciente = new Paciente();
        paciente.setNome("João");
        consulta.setPaciente(paciente);
        Funcionario medico = new Funcionario();
        medico.setNome("Dra. Ana");
        consulta.setMedico(medico);
        consulta.setDataHorario(LocalDateTime.now());

        when(consultaRepository.findById(3L)).thenReturn(Optional.of(consulta));
        when(prontuarioRepository.save(any(Prontuario.class)))
                .thenAnswer(invocation -> {
                    Prontuario salvo = invocation.getArgument(0);
                    salvo.setId(15L);
                    return salvo;
                });

        ProntuarioRequest request = new ProntuarioRequest();
        request.setConsultaId(3L);
        request.setReceituario("Repouso e hidratação");

        var dto = prontuarioService.save(request);

        assertEquals(3L, dto.getConsultaId());
        assertEquals("João", dto.getNomePaciente());
        assertEquals("Dra. Ana", dto.getNomeMedico());

        ArgumentCaptor<Prontuario> captor = ArgumentCaptor.forClass(Prontuario.class);
        verify(prontuarioRepository).save(captor.capture());
        assertEquals(consulta, captor.getValue().getConsulta());
    }

    @Test
    void deveLancarExcecaoAoDeletarProntuarioInexistente() {
        when(prontuarioRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> prontuarioService.delete(99L));
    }
}

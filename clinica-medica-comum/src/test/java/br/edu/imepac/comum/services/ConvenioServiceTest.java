package br.edu.imepac.comum.services;

import br.edu.imepac.comum.dtos.convenio.ConvenioRequest;
import br.edu.imepac.comum.exceptions.ResourceNotFoundException;
import br.edu.imepac.comum.models.Convenio;
import br.edu.imepac.comum.repositories.ConvenioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConvenioServiceTest {

    @Mock
    private ConvenioRepository convenioRepository;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private ConvenioService convenioService;

    @Test
    void deveAtualizarCamposNaoNulos() {
        Convenio existente = new Convenio();
        existente.setId(7L);
        existente.setNomeEmpresa("Saude Total");
        existente.setCnpj("00011122233344");
        when(convenioRepository.findById(7L)).thenReturn(Optional.of(existente));
        when(convenioRepository.save(any(Convenio.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ConvenioRequest request = new ConvenioRequest();
        request.setNomeEmpresa("Rede Vida");
        request.setTelefone("34999999999");

        convenioService.update(7L, request);

        ArgumentCaptor<Convenio> captor = ArgumentCaptor.forClass(Convenio.class);
        verify(convenioRepository).save(captor.capture());
        Convenio atualizado = captor.getValue();

        assertEquals("Rede Vida", atualizado.getNomeEmpresa());
        assertEquals("34999999999", atualizado.getTelefone());
        assertEquals("00011122233344", atualizado.getCnpj());
    }

    @Test
    void deveLancarExcecaoQuandoConvenioNaoExistirParaAtualizacao() {
        when(convenioRepository.findById(99L)).thenReturn(Optional.empty());

        ConvenioRequest request = new ConvenioRequest();
        request.setNomeEmpresa("Rede Vida");

        assertThrows(ResourceNotFoundException.class,
                () -> convenioService.update(99L, request));
    }
}

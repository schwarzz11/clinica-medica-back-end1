package br.edu.imepac.comum.services;

import br.edu.imepac.comum.domain.EnumTipoFuncionario;
import br.edu.imepac.comum.dtos.funcionario.FuncionarioRequest;
import br.edu.imepac.comum.exceptions.ResourceNotFoundException;
import br.edu.imepac.comum.models.Funcionario;
import br.edu.imepac.comum.models.Perfil;
import br.edu.imepac.comum.repositories.FuncionarioRepository;
import br.edu.imepac.comum.repositories.PerfilRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FuncionarioServiceTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private PerfilRepository perfilRepository;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private FuncionarioService funcionarioService;

    @Test
    void deveAdicionarFuncionarioComSenhaCriptografadaEPerfil() {
        FuncionarioRequest request = criarRequestBasico();
        Perfil perfil = new Perfil();
        perfil.setId(1L);

        when(perfilRepository.findById(1L)).thenReturn(Optional.of(perfil));
        when(passwordEncoder.encode("SenhaForte!"))
                .thenReturn("hash-seguro");
        when(funcionarioRepository.save(any(Funcionario.class)))
                .thenAnswer(invocation -> {
                    Funcionario salvo = invocation.getArgument(0);
                    salvo.setId(10L);
                    return salvo;
                });

        funcionarioService.adicionarFuncionario(request);

        ArgumentCaptor<Funcionario> captor = ArgumentCaptor.forClass(Funcionario.class);
        verify(funcionarioRepository).save(captor.capture());
        Funcionario persistido = captor.getValue();

        assertEquals(perfil, persistido.getPerfil());
        assertEquals("hash-seguro", persistido.getSenha());
    }

    @Test
    void deveLancarExcecaoAoAtualizarPerfilInexistente() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(5L);
        when(funcionarioRepository.findById(5L)).thenReturn(Optional.of(funcionario));

        FuncionarioRequest request = new FuncionarioRequest();
        request.setPerfilId(99L);

        when(perfilRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> funcionarioService.atualizarFuncionario(5L, request));
    }

    private FuncionarioRequest criarRequestBasico() {
        FuncionarioRequest request = new FuncionarioRequest();
        request.setNome("Maria Souza");
        request.setCpf("12345678901");
        request.setEmail("maria@exemplo.com");
        request.setUsuario("maria");
        request.setSenha("SenhaForte!");
        request.setSexo('F');
        request.setDataNascimento(LocalDate.of(1990, 5, 10));
        request.setEstado("MG");
        request.setTipoFuncionario(EnumTipoFuncionario.ATENDENTE);
        request.setPerfilId(1L);
        return request;
    }
}

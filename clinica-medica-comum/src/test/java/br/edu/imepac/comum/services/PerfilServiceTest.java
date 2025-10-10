package br.edu.imepac.comum.services;

import br.edu.imepac.comum.exceptions.AuthenticationClinicaMedicaException;
import br.edu.imepac.comum.models.Funcionario;
import br.edu.imepac.comum.models.Perfil;
import br.edu.imepac.comum.repositories.FuncionarioRepository;
import br.edu.imepac.comum.repositories.PerfilRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerfilServiceTest {

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private PerfilService perfilService;

    @Test
    void deveAutorizarAcaoQuandoPerfilPossuiPermissao() {
        Perfil perfil = new Perfil();
        perfil.setCadastrarPaciente(true);

        Funcionario funcionario = new Funcionario();
        funcionario.setUsuario("usuario");
        funcionario.setSenha("segredo");
        funcionario.setPerfil(perfil);

        when(funcionarioRepository.findByUsuario("usuario"))
                .thenReturn(Optional.of(funcionario));

        assertTrue(perfilService.verificarAutorizacao("usuario", "segredo", "cadastrarPaciente"));
    }

    @Test
    void deveLancarExcecaoQuandoSenhaEstiverIncorreta() {
        Perfil perfil = new Perfil();
        Funcionario funcionario = new Funcionario();
        funcionario.setSenha("correta");
        funcionario.setPerfil(perfil);

        when(funcionarioRepository.findByUsuario("usuario"))
                .thenReturn(Optional.of(funcionario));

        assertThrows(AuthenticationClinicaMedicaException.class,
                () -> perfilService.verificarAutorizacao("usuario", "errada", "cadastrarPaciente"));
    }

    @Test
    void deveNegarQuandoAcaoNaoForReconhecida() {
        Perfil perfil = new Perfil();
        perfil.setCadastrarPaciente(true);

        Funcionario funcionario = new Funcionario();
        funcionario.setSenha("senha");
        funcionario.setPerfil(perfil);

        when(funcionarioRepository.findByUsuario("usuario"))
                .thenReturn(Optional.of(funcionario));

        assertFalse(perfilService.verificarAutorizacao("usuario", "senha", "acao-desconhecida"));
    }
}

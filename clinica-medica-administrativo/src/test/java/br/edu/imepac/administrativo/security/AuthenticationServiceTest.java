package br.edu.imepac.administrativo.security;

import br.edu.imepac.comum.dtos.auth.AuthenticationRequest;
import br.edu.imepac.comum.dtos.auth.AuthenticationResponse;
import br.edu.imepac.comum.models.Funcionario;
import br.edu.imepac.comum.repositories.FuncionarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void authenticateShouldReturnJwtTokenWhenCredentialsAreValid() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsuario("roger");
        request.setSenha("123456");

        Funcionario funcionario = new Funcionario();
        funcionario.setUsuario("roger");

        when(funcionarioRepository.findByUsuario("roger"))
                .thenReturn(Optional.of(funcionario));
        when(jwtService.generateToken(funcionario)).thenReturn("jwt-token");

        AuthenticationResponse response = authenticationService.authenticate(request);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(funcionario);
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("jwt-token");
    }
}

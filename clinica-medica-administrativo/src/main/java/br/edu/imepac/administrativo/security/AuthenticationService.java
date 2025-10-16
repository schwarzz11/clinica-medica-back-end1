package br.edu.imepac.administrativo.security;

import br.edu.imepac.comum.dtos.auth.AuthenticatedUserDto;
import br.edu.imepac.comum.dtos.auth.AuthenticationRequest;
import br.edu.imepac.comum.dtos.auth.AuthenticationResponse;
import br.edu.imepac.comum.models.Perfil;
import br.edu.imepac.comum.repositories.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final FuncionarioRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsuario(),
                        request.getSenha()
                )
        );

        var user = repository.findByUsuario(request.getUsuario())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        var perfil = user.getPerfil();
        var userDto = new AuthenticatedUserDto(
                user.getId(),
                user.getNome(),
                user.getUsuario(),
                user.getEmail(),
                user.getTipoFuncionario(),
                perfil != null ? perfil.getId() : null,
                perfil != null ? perfil.getNome() : null
        );

        Map<String, Boolean> permissions = perfil != null ? buildPermissions(perfil) : Map.of();

        return new AuthenticationResponse(jwtToken, userDto, permissions);
    }

    private Map<String, Boolean> buildPermissions(Perfil perfil) {
        Map<String, Boolean> permissions = new LinkedHashMap<>();
        permissions.put("cadastrarFuncionario", perfil.isCadastrarFuncionario());
        permissions.put("lerFuncionario", perfil.isLerFuncionario());
        permissions.put("atualizarFuncionario", perfil.isAtualizarFuncionario());
        permissions.put("deletarFuncionario", perfil.isDeletarFuncionario());
        permissions.put("listarFuncionario", perfil.isListarFuncionario());

        permissions.put("cadastrarPaciente", perfil.isCadastrarPaciente());
        permissions.put("lerPaciente", perfil.isLerPaciente());
        permissions.put("atualizarPaciente", perfil.isAtualizarPaciente());
        permissions.put("deletarPaciente", perfil.isDeletarPaciente());
        permissions.put("listarPaciente", perfil.isListarPaciente());

        permissions.put("cadastrarConsulta", perfil.isCadastrarConsulta());
        permissions.put("lerConsulta", perfil.isLerConsulta());
        permissions.put("atualizarConsulta", perfil.isAtualizarConsulta());
        permissions.put("deletarConsulta", perfil.isDeletarConsulta());
        permissions.put("listarConsulta", perfil.isListarConsulta());

        permissions.put("cadastrarEspecialidade", perfil.isCadastrarEspecialidade());
        permissions.put("lerEspecialidade", perfil.isLerEspecialidade());
        permissions.put("atualizarEspecialidade", perfil.isAtualizarEspecialidade());
        permissions.put("deletarEspecialidade", perfil.isDeletarEspecialidade());
        permissions.put("listarEspecialidade", perfil.isListarEspecialidade());

        permissions.put("cadastrarConvenio", perfil.isCadastrarConvenio());
        permissions.put("lerConvenio", perfil.isLerConvenio());
        permissions.put("atualizarConvenio", perfil.isAtualizarConvenio());
        permissions.put("deletarConvenio", perfil.isDeletarConvenio());
        permissions.put("listarConvenio", perfil.isListarConvenio());

        permissions.put("cadastrarProntuario", perfil.isCadastrarProntuario());
        permissions.put("lerProntuario", perfil.isLerProntuario());
        permissions.put("atualizarProntuario", perfil.isAtualizarProntuario());
        permissions.put("deletarProntuario", perfil.isDeletarProntuario());
        permissions.put("listarProntuario", perfil.isListarProntuario());
        return permissions;
    }
}

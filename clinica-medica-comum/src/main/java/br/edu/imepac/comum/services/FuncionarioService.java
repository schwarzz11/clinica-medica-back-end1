package br.edu.imepac.comum.services;

import br.edu.imepac.comum.dtos.funcionario.FuncionarioDto;
import br.edu.imepac.comum.dtos.funcionario.FuncionarioRequest;
import br.edu.imepac.comum.exceptions.ResourceNotFoundException;
import br.edu.imepac.comum.models.Funcionario;
import br.edu.imepac.comum.models.Perfil;
import br.edu.imepac.comum.repositories.FuncionarioRepository;
import br.edu.imepac.comum.repositories.PerfilRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public FuncionarioDto adicionarFuncionario(FuncionarioRequest request) {
        Perfil perfil = perfilRepository.findById(request.getPerfilId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado"));

        Funcionario funcionario = modelMapper.map(request, Funcionario.class);
        funcionario.setPerfil(perfil);
        funcionario.setSenha(passwordEncoder.encode(request.getSenha()));

        Funcionario savedFuncionario = funcionarioRepository.save(funcionario);
        return modelMapper.map(savedFuncionario, FuncionarioDto.class);
    }

    public List<FuncionarioDto> listarFuncionarios() {
        return funcionarioRepository.findAll().stream()
                .map(funcionario -> modelMapper.map(funcionario, FuncionarioDto.class))
                .collect(Collectors.toList());
    }

    public FuncionarioDto buscarFuncionarioPorId(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
        return modelMapper.map(funcionario, FuncionarioDto.class);
    }

    public FuncionarioDto atualizarFuncionario(Long id, FuncionarioRequest request) {
        Funcionario funcionarioExistente = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));

        if (request.getNome() != null) {
            funcionarioExistente.setNome(request.getNome());
        }
        if (request.getCpf() != null) {
            funcionarioExistente.setCpf(request.getCpf());
        }
        if (request.getEmail() != null) {
            funcionarioExistente.setEmail(request.getEmail());
        }
        if (request.getUsuario() != null) {
            funcionarioExistente.setUsuario(request.getUsuario());
        }
        if (request.getSexo() != '\0') {
            funcionarioExistente.setSexo(request.getSexo());
        }
        if (request.getDataNascimento() != null) {
            funcionarioExistente.setDataNascimento(request.getDataNascimento());
        }
        if (request.getRua() != null) {
            funcionarioExistente.setRua(request.getRua());
        }
        if (request.getNumero() != null) {
            funcionarioExistente.setNumero(request.getNumero());
        }
        if (request.getComplemento() != null) {
            funcionarioExistente.setComplemento(request.getComplemento());
        }
        if (request.getBairro() != null) {
            funcionarioExistente.setBairro(request.getBairro());
        }
        if (request.getCidade() != null) {
            funcionarioExistente.setCidade(request.getCidade());
        }
        if (request.getEstado() != null) {
            funcionarioExistente.setEstado(request.getEstado());
        }
        if (request.getTipoFuncionario() != null) {
            funcionarioExistente.setTipoFuncionario(request.getTipoFuncionario());
        }
        if (request.getPerfilId() != null) {
            Perfil perfil = perfilRepository.findById(request.getPerfilId())
                    .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado para atualização"));
            funcionarioExistente.setPerfil(perfil);
        }
        if (request.getSenha() != null && !request.getSenha().isBlank()) {
            funcionarioExistente.setSenha(passwordEncoder.encode(request.getSenha()));
        }

        Funcionario updatedFuncionario = funcionarioRepository.save(funcionarioExistente);
        return modelMapper.map(updatedFuncionario, FuncionarioDto.class);
    }

    public void removerFuncionario(Long id) {
        if (!funcionarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Funcionário não encontrado");
        }
        funcionarioRepository.deleteById(id);
    }
}

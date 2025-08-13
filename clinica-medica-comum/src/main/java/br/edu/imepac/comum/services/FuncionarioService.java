package br.edu.imepac.comum.services;

import br.edu.imepac.comum.dtos.funcionario.FuncionarioDto;
import br.edu.imepac.comum.dtos.funcionario.FuncionarioRequest;
import br.edu.imepac.comum.exceptions.ResourceNotFoundException; // Usando a exceção padronizada
import br.edu.imepac.comum.models.Funcionario;
import br.edu.imepac.comum.models.Perfil;
import br.edu.imepac.comum.repositories.FuncionarioRepository;
import br.edu.imepac.comum.repositories.PerfilRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

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


        String senhaCriptografada = passwordEncoder.encode(request.getSenha());
        funcionario.setSenha(senhaCriptografada);


        Funcionario savedFuncionario = funcionarioRepository.save(funcionario);
        return modelMapper.map(savedFuncionario, FuncionarioDto.class);
    }

    public List<FuncionarioDto> listarFuncionarios() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        return funcionarios.stream()
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

        if (request.getEmail() != null) {
            funcionarioExistente.setEmail(request.getEmail());
        }
        // Adicione outros ifs para os campos que podem ser atualizados

        if (request.getPerfilId() != null) {
            Perfil perfil = perfilRepository.findById(request.getPerfilId())
                    .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado para atualização"));
            funcionarioExistente.setPerfil(perfil);
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
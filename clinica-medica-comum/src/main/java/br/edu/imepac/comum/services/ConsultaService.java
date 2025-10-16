package br.edu.imepac.comum.services;

import br.edu.imepac.comum.dtos.consulta.ConsultaDto;
import br.edu.imepac.comum.dtos.consulta.ConsultaRequest;
import br.edu.imepac.comum.exceptions.ResourceNotFoundException;
import br.edu.imepac.comum.models.Consulta;
import br.edu.imepac.comum.models.Funcionario;
import br.edu.imepac.comum.models.Paciente;
import br.edu.imepac.comum.repositories.ConsultaRepository;
import br.edu.imepac.comum.repositories.FuncionarioRepository;
import br.edu.imepac.comum.repositories.PacienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    private ConsultaDto convertToDto(Consulta consulta) {
        ConsultaDto dto = modelMapper.map(consulta, ConsultaDto.class);
        if (consulta.getPaciente() != null) {
            dto.setPacienteId(consulta.getPaciente().getId());
            dto.setNomePaciente(consulta.getPaciente().getNome());
        }
        if (consulta.getMedico() != null) {
            dto.setMedicoId(consulta.getMedico().getId());
            dto.setNomeMedico(consulta.getMedico().getNome());
        }
        dto.setSintomas(consulta.getSintomas());
        return dto;
    }

    public ConsultaDto save(ConsultaRequest consultaRequest) {
        Paciente paciente = pacienteRepository.findById(consultaRequest.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

        Funcionario medico = funcionarioRepository.findById(consultaRequest.getMedicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado"));

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setDataHorario(consultaRequest.getDataHorario());
        consulta.setSintomas(consultaRequest.getSintomas());
        consulta.setERetorno(consultaRequest.isERetorno());
        consulta.setEstaAtiva(true);

        Consulta savedConsulta = consultaRepository.save(consulta);
        return convertToDto(savedConsulta);
    }

    public List<ConsultaDto> findAll() {
        return consultaRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ConsultaDto findById(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada"));
        return convertToDto(consulta);
    }

    public ConsultaDto update(Long id, ConsultaRequest request) {
        Consulta consultaExistente = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada"));

        if (request.getPacienteId() != null) {
            Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado para atualização"));
            consultaExistente.setPaciente(paciente);
        }

        if (request.getMedicoId() != null) {
            Funcionario medico = funcionarioRepository.findById(request.getMedicoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado para atualização"));
            consultaExistente.setMedico(medico);
        }

        if (request.getDataHorario() != null) {
            consultaExistente.setDataHorario(request.getDataHorario());
        }
        if (request.getSintomas() != null) {
            consultaExistente.setSintomas(request.getSintomas());
        }
        consultaExistente.setERetorno(request.isERetorno());

        Consulta updatedConsulta = consultaRepository.save(consultaExistente);
        return convertToDto(updatedConsulta);
    }

    public void delete(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada"));

        consulta.setEstaAtiva(false);
        consultaRepository.save(consulta);
    }
}

package br.edu.imepac.administrativo.config;

import br.edu.imepac.comum.dtos.funcionario.FuncionarioDto;
import br.edu.imepac.comum.dtos.paciente.PacienteDto;
import br.edu.imepac.comum.models.Funcionario;
import br.edu.imepac.comum.models.Paciente;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigModelMapper {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        modelMapper.typeMap(Paciente.class, PacienteDto.class).setPostConverter(context -> {
            Paciente source = context.getSource();
            PacienteDto destination = context.getDestination();
            if (source.getConvenio() != null) {
                destination.setConvenioId(source.getConvenio().getId());
                destination.setNomeConvenio(source.getConvenio().getNomeEmpresa());
            } else {
                destination.setConvenioId(null);
                destination.setNomeConvenio(null);
            }
            return destination;
        });

        modelMapper.typeMap(Funcionario.class, FuncionarioDto.class).setPostConverter(context -> {
            Funcionario source = context.getSource();
            FuncionarioDto destination = context.getDestination();
            if (source.getPerfil() != null) {
                destination.setPerfilId(source.getPerfil().getId());
                destination.setPerfilNome(source.getPerfil().getNome());
            } else {
                destination.setPerfilId(null);
                destination.setPerfilNome(null);
            }
            return destination;
        });

        return modelMapper;
    }
}

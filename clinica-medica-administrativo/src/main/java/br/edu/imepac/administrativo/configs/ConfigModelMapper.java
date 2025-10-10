package br.edu.imepac.administrativo.configs;

import br.edu.imepac.comum.dtos.paciente.PacienteDto;
import br.edu.imepac.comum.models.Paciente;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigModelMapper {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();


        modelMapper.getConfiguration().setSkipNullEnabled(true);

        modelMapper.addMappings(new PropertyMap<Paciente, PacienteDto>() {
            @Override
            protected void configure() {
                map().setNomeConvenio(source.getConvenio().getNomeEmpresa());
            }
        });

        return modelMapper;
    }
}

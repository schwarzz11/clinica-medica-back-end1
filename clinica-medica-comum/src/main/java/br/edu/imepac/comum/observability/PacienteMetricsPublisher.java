package br.edu.imepac.comum.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class PacienteMetricsPublisher {

    private final Counter pacientesCriados;
    private final Counter pacientesAtualizados;

    public PacienteMetricsPublisher(MeterRegistry meterRegistry) {
        this.pacientesCriados = Counter.builder("pacientes_criados_total")
                .description("Total de pacientes cadastrados")
                .register(meterRegistry);
        this.pacientesAtualizados = Counter.builder("pacientes_atualizados_total")
                .description("Total de pacientes atualizados")
                .register(meterRegistry);
    }

    public void markCriado() {
        pacientesCriados.increment();
    }

    public void markAtualizado() {
        pacientesAtualizados.increment();
    }
}

package br.edu.imepac.comum.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class ConsultaMetricsPublisher {

    private final Counter consultasCriadas;
    private final Counter consultasAtualizadas;
    private final Counter consultasCanceladas;

    public ConsultaMetricsPublisher(MeterRegistry meterRegistry) {
        this.consultasCriadas = Counter.builder("consultas_criadas_total")
                .description("Quantidade de consultas criadas")
                .register(meterRegistry);
        this.consultasAtualizadas = Counter.builder("consultas_atualizadas_total")
                .description("Quantidade de consultas atualizadas")
                .register(meterRegistry);
        this.consultasCanceladas = Counter.builder("consultas_canceladas_total")
                .description("Quantidade de consultas canceladas")
                .register(meterRegistry);
    }

    public void markCriada() {
        consultasCriadas.increment();
    }

    public void markAtualizada() {
        consultasAtualizadas.increment();
    }

    public void markCancelada() {
        consultasCanceladas.increment();
    }
}

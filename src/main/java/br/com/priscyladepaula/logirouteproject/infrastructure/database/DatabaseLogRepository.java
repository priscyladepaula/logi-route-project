package br.com.priscyladepaula.logirouteproject.infrastructure.database;

import br.com.priscyladepaula.logirouteproject.domain.ConsultaLog;
import br.com.priscyladepaula.logirouteproject.domain.LogGateway;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseLogRepository implements LogGateway {

    private final DataLogRepository repository;

    public DatabaseLogRepository(DataLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public void salvar(ConsultaLog log) {
        repository.save(log);
    }

    @Override
    public List<ConsultaLog> buscarTodos(){
        return repository.findAll();
    }
}
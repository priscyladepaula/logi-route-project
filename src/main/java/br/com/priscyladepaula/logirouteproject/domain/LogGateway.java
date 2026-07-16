package br.com.priscyladepaula.logirouteproject.domain;

import java.util.List;

public interface LogGateway  {
    void salvar(ConsultaLog log);

    List<ConsultaLog> buscarTodos();

}

package br.com.priscyladepaula.logirouteproject.gateway;

import br.com.priscyladepaula.logirouteproject.dto.CepResponse;

public interface CepGateway {
    CepResponse buscarCep(String cep);
}

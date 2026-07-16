package br.com.priscyladepaula.logirouteproject.infrastructure.viacep;

import br.com.priscyladepaula.logirouteproject.dto.CepResponse;
import br.com.priscyladepaula.logirouteproject.gateway.CepGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ViaCepClient implements CepGateway {

    private final RestTemplate restTemplate;
    private final String url;

    public ViaCepClient(RestTemplate restTemplate, @Value("${api.viacep.url}") String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    @Override
    public CepResponse buscarCep(String cep) {
        String urlFormatada = String.format("%s/ws/%s/json", url, cep);
        try {
            return restTemplate.getForObject(urlFormatada, CepResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar a API de CEP externa", e);
        }
    }
}

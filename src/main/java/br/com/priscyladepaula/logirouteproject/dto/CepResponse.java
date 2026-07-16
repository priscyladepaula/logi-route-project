package br.com.priscyladepaula.logirouteproject.dto;

public record CepResponse(
        String cep,
        String logradouro,
        String localidade,
        String uf
) {}
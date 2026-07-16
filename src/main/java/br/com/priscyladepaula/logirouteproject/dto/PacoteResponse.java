package br.com.priscyladepaula.logirouteproject.dto;

import java.math.BigDecimal;

public record PacoteResponse(
        String cep,
        String uf,
        String localidade,
        String zona,
        String transportadora,
        int prazoDiasUteis,
        BigDecimal valorFrete
) {}

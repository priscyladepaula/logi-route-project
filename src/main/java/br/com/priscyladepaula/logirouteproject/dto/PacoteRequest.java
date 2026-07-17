package br.com.priscyladepaula.logirouteproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PacoteRequest {

    @NotBlank(message = "O CEP é obrigatório")
    private String cep;

    @NotNull(message = "O peso é obrigatório")
    @Positive(message = "O peso do pacote deve ser maior que zero")
    private Double peso;

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public PacoteRequest() {}

    public PacoteRequest(String cep, Double peso) {
        this.cep = cep;
        this.peso = peso;
    }
}
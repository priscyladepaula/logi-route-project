package br.com.priscyladepaula.logirouteproject.infrastructure.exception;

public class CepNaoEncontradoException extends RuntimeException {
    public CepNaoEncontradoException(String cep) {
        super("O CEP " + cep + " não foi encontrado na base de dados.");
    }
}

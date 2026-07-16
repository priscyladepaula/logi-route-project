package br.com.priscyladepaula.logirouteproject.infrastructure.exception;

import br.com.priscyladepaula.logirouteproject.dto.ErroFormularioDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(CepNaoEncontradoException.class)
    public ResponseEntity<ErroFormularioDto> tratarErro404(CepNaoEncontradoException ex) {

        ErroFormularioDto erro = new ErroFormularioDto("cep", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }
}

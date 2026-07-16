package br.com.priscyladepaula.logirouteproject.controller;

import br.com.priscyladepaula.logirouteproject.domain.ConsultaLog;
import br.com.priscyladepaula.logirouteproject.dto.PacoteRequest;
import br.com.priscyladepaula.logirouteproject.dto.PacoteResponse;
import br.com.priscyladepaula.logirouteproject.service.TriagemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacotes")
public class PacoteController {

    private final TriagemService triagemService;

    public PacoteController(TriagemService triagemService) {
        this.triagemService = triagemService;
    }

    @PostMapping
    public ResponseEntity<PacoteResponse> triarPacote(@Valid @RequestBody PacoteRequest request) {
        PacoteResponse response = triagemService.executarTriagem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/logs")
    public ResponseEntity<List<ConsultaLog>> obterHistorico() {
        List<ConsultaLog> logs = triagemService.listarHistorico();
        return ResponseEntity.ok(logs);
    }
}
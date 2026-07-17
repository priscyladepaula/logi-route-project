package br.com.priscyladepaula.logirouteproject.service;

import br.com.priscyladepaula.logirouteproject.domain.ConsultaLog;
import br.com.priscyladepaula.logirouteproject.domain.LogGateway;
import br.com.priscyladepaula.logirouteproject.domain.Regiao;
import br.com.priscyladepaula.logirouteproject.dto.CepResponse;
import br.com.priscyladepaula.logirouteproject.dto.PacoteRequest;
import br.com.priscyladepaula.logirouteproject.dto.PacoteResponse;
import br.com.priscyladepaula.logirouteproject.gateway.CepGateway;
import br.com.priscyladepaula.logirouteproject.infrastructure.exception.CepNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TriagemService {

    private final CepGateway cepGateway;
    private final LogGateway logGateway;

    public TriagemService(CepGateway cepGateway, LogGateway logGateway) {
        this.cepGateway = cepGateway;
        this.logGateway = logGateway;
    }

    public PacoteResponse executarTriagem(PacoteRequest request) {

        String cepLimpo = request.getCep().replaceAll("\\D", "");

        if (cepLimpo.length() != 8) {
            throw new CepNaoEncontradoException(request.getCep());
        }

        CepResponse cepDados = cepGateway.buscarCep(cepLimpo);

        if (cepDados == null || cepDados.uf() == null) {
            throw new CepNaoEncontradoException(cepLimpo);
        }

        String uf = cepDados.uf().trim().toUpperCase();

        String zona = determinarZona(uf);
        String transportadora = determinarTransportadora(uf);
        int prazo = Regiao.obterPorUf(uf).getPrazoDiasUteis();

        BigDecimal valorFrete = calcularFrete(uf, BigDecimal.valueOf(request.getPeso()));

        salvarConsultaLog(cepDados, valorFrete, transportadora, zona);

        return new PacoteResponse(
                cepDados.cep(),
                uf,
                cepDados.localidade(),
                zona,
                transportadora,
                prazo,
                valorFrete);
    }

    private BigDecimal calcularFrete(String uf, BigDecimal peso) {
        Regiao regiao = Regiao.obterPorUf(uf);

        BigDecimal custoPeso = peso.multiply(regiao.getValorPorKg());

        return custoPeso.add(regiao.getTaxaFixa());
    }

    private String determinarZona(String uf) {
        Regiao regiao = Regiao.obterPorUf(uf);
        return regiao != null ? regiao.getNome() : null;
    }

    private String determinarTransportadora(String uf) {
        return Regiao.obterPorUf(uf).getTransportadora();
    }

    public List<ConsultaLog> listarHistorico() {
        List<ConsultaLog> historico = logGateway.buscarTodos();

        return historico;
    }

    private void salvarConsultaLog(CepResponse cepDados, BigDecimal valorFrete, String transportadora, String zona) {
        ConsultaLog log = new ConsultaLog();
        log.setCep(cepDados.cep());
        log.setUf(cepDados.uf());
        log.setLocalidade(cepDados.localidade());
        log.setValorFrete(valorFrete);
        log.setTransportadora(transportadora);
        log.setZona(zona);
        log.setDataHora(LocalDateTime.now());

        logGateway.salvar(log);
    }
}
package br.com.priscyladepaula.logirouteproject.service;

import br.com.priscyladepaula.logirouteproject.domain.ConsultaLog;
import br.com.priscyladepaula.logirouteproject.domain.LogGateway;
import br.com.priscyladepaula.logirouteproject.dto.CepResponse;
import br.com.priscyladepaula.logirouteproject.dto.PacoteRequest;
import br.com.priscyladepaula.logirouteproject.dto.PacoteResponse;
import br.com.priscyladepaula.logirouteproject.gateway.CepGateway;
import br.com.priscyladepaula.logirouteproject.infrastructure.exception.CepNaoEncontradoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TriagemServiceTest {

    @Mock
    private CepGateway cepGateway;

    @Mock
    private LogGateway logGateway;

    @InjectMocks
    private TriagemService triagemService;

    @Test
    @DisplayName("Deve calcular o frete corretamente para a região SUDESTE e salvar o log")
    void deveTriarPacoteParaSudesteComSucesso() {

        String cep = "01001000";
        PacoteRequest request = new PacoteRequest(cep, 2.5); // Peso: 2.5kg

        CepResponse respostaMockada = new CepResponse(cep, "Praça da Sé", "São Paulo", "SP");

        when(cepGateway.buscarCep(cep)).thenReturn(respostaMockada);

        PacoteResponse response = triagemService.executarTriagem(request);

        assertNotNull(response);
        assertEquals("SP", response.uf());
        assertEquals("Sudeste", response.zona());
        assertEquals("LogiExpress Sudeste", response.transportadora());

        assertEquals(new BigDecimal("23.75"), response.valorFrete());

        verify(logGateway, times(1)).salvar(any(ConsultaLog.class));
    }

    @Test
    @DisplayName("Deve calcular o frete corretamente para a região SUL")
    void deveCalvarFreteParaSulComSucesso() {

        String cep = "80010010";
        PacoteRequest request = new PacoteRequest(cep, 4.0); // Peso: 4.0kg

        CepResponse respostaMockada = new CepResponse(cep, "Rua XV de Novembro", "Curitiba", "PR");

        when(cepGateway.buscarCep(cep)).thenReturn(respostaMockada);

        PacoteResponse response = triagemService.executarTriagem(request);

        assertNotNull(response);
        assertEquals("Sul", response.zona());
        assertEquals("SulTrans Transportes", response.transportadora());

        assertEquals(new BigDecimal("40.80"), response.valorFrete());
    }

    @Test
    @DisplayName("Deve lançar CepNaoEncontradoException quando o CEP não existir")
    void deveLancarexcecaoQuandoCepNaoForEncontrado() {

        String cepInexistente = "99999999";
        PacoteRequest request = new PacoteRequest(cepInexistente, 2.0);

        when(cepGateway.buscarCep(cepInexistente)).thenReturn(null);

        assertThrows(CepNaoEncontradoException.class, () -> {
            triagemService.executarTriagem(request);
        });

        verify(logGateway, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve salvar o log com a transportadora e frete corretos ao executar triagem")
    void deveSalvarLogAoExecutarTriagemComSucesso() {
        String cep = "01001000";
        PacoteRequest request = new PacoteRequest(cep, 10.0);

        CepResponse responseMock = new CepResponse(cep, "Praça da Sé", "São Paulo", "SP");

        when(cepGateway.buscarCep(cep)).thenReturn(responseMock);

        PacoteResponse response = triagemService.executarTriagem(request);

        assertNotNull(response);
        assertEquals("LogiExpress Sudeste", response.transportadora());
        assertEquals("Sudeste", response.zona());

        ArgumentCaptor<ConsultaLog> logCaptor = ArgumentCaptor.forClass(ConsultaLog.class);
        verify(logGateway).salvar(logCaptor.capture());

        ConsultaLog logSalvo = logCaptor.getValue();

        assertEquals("LogiExpress Sudeste", logSalvo.getTransportadora());
        assertEquals(cep, logSalvo.getCep());

        assertEquals(new BigDecimal("65.00"), logSalvo.getValorFrete());
    }

    @Test
    @DisplayName("Deve executar triagem com sucesso mesmo se o CEP for enviado com hífen")
    void deveExecutarTriagemComSucessoQuandoCepContiverHifen() {
        String cepComHifen = "01001-000";
        String cepLimpo = "01001000";
        PacoteRequest request = new PacoteRequest(cepComHifen, 2.5);

        CepResponse respostaMockada = new CepResponse(cepLimpo, "Praça da Sé", "São Paulo", "SP");

        when(cepGateway.buscarCep(cepLimpo)).thenReturn(respostaMockada);

        PacoteResponse response = triagemService.executarTriagem(request);

        assertNotNull(response);
        assertEquals("SP", response.uf());
        assertEquals("01001000", response.cep());
    }
}
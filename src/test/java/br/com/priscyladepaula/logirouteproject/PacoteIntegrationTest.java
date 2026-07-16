package br.com.priscyladepaula.logirouteproject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class PacoteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve triar um pacote com sucesso para a região sudeste a partir do CEP")
    void deveTriarPacoteComSucesso() throws Exception {
        String pacotePayload = """
                {
                    "cep": "01001000",
                    "peso": 2.5
                }
                """;

        mockMvc.perform(post("/pacotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pacotePayload))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve retornar Bad Request com corpo detalhado se o peso for inválido")
    void deveRetornarBadRequestParaPesoInvalido() throws Exception {
        String payloadInvalido = """
            {
                "cep": "01001000",
                "peso": -5.0
            }
            """;

        mockMvc.perform(post("/pacotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].campo").value("peso"))
                .andExpect(jsonPath("$[0].mensagem").value("O peso do pacote deve ser maior que zero"));
    }

    @Test
    @DisplayName("Deve registrar a consulta no banco de dados e recuperá-la através do histórico")
    void deveSalvarERecuperarLogDeConsulta() throws Exception {
        String payloadValido = """
            {
                "cep": "01001000",
                "peso": 3.0
            }
            """;

        mockMvc.perform(post("/pacotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadValido))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/pacotes/logs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].cep").value("01001000"));
    }
}

# 🗺️ LogiRoute Project

> Sistema de triagem, cálculo de frete dinâmico e rastreabilidade de rotas de entrega baseado no ViaCEP.

O **LogiRoute** é uma API REST desenvolvida em Java com Spring Boot, estruturada sob os princípios de **Clean Architecture** (Arquitetura Limpa). O objetivo principal é receber dados de pacotes, consultar informações de endereço através da API do ViaCEP, determinar a região logística (Zona/Região), calcular o frete dinamicamente com base em regras de peso/região, indicar a transportadora parceira ideal e registrar um histórico completo de consultas para fins de auditoria.

---

## 🛠️ Tecnologias Utilizadas

![](https://img.shields.io/badge/Java-21-7B62A3?style=for-the-badge)
![](https://img.shields.io/badge/SpringBoot-3.x-7B62A3?style=for-the-badge)
![](https://img.shields.io/badge/Spring%20Data%20JPA-PostgreSQL-7B62A3?style=for-the-badge)
![](https://img.shields.io/badge/Mockito-JUnit-7B62A3?style=for-the-badge)
![](https://img.shields.io/badge/Jackson-7B62A3?style=for-the-badge)
![](https://img.shields.io/badge/Docker-Docker%20Compose-7B62A3?style=for-the-badge)

---

## 📐 Desenho da Solução (Arquitetura)

A aplicação foi desenhada seguindo os conceitos de **Clean Architecture** e isolamento de domínios. Isso garante que as regras de negócio fiquem completamente independentes de frameworks externos (como o Spring), bancos de dados ou APIs de terceiros.

### Diagrama de Fluxo de Dados:
```mermaid
graph TD
    %% Texto em preto (color:#000)
    classDef client fill:#ececff,stroke:#9370db,stroke-width:2px,rx:10px,color:#000;
    classDef controller fill:#e1f5fe,stroke:#03a9f4,stroke-width:2px,rx:8px,color:#000;
    classDef service fill:#fff9c4,stroke:#fbc02d,stroke-width:2px,color:#000;
    classDef gateway fill:#e8f5e9,stroke:#4caf50,stroke-width:2px,color:#000;
    classDef db fill:#ffebee,stroke:#f44336,stroke-width:2px,color:#000;

    %% Elementos do Gráfico
    Client[📱 Cliente / Apidog]:::client -->|POST /pacotes| Controller[⚙️ TriagemController]:::controller
    
    Controller -->|Sanitização e Validação do CEP| Service{🧠 TriagemService}:::service
    
    Service -->|1. Consulta CEP| Gateway[🔌 CepGateway]:::gateway
    Gateway -->|2. Request| ViaCEP([☁️ API ViaCEP]):::gateway
    ViaCEP -->|3. Retorna Endereço| Gateway
    
    Service -->|4. Obtém Região/Regras| Enum[📋 Regiao Enum]:::service
    Service -->|5. Calcula Frete| Frete(💵 Peso * TaxaKg + TaxaFixa):::service
    
    Service -->|6. Salva Log| LogGateway[💾 LogGateway]:::gateway
    LogGateway -->|Persiste| DB[(🗄️ PostgreSQL Docker)]:::db
    
    Service -->|7. Retorna JSON| Response[📦 PacoteResponse]:::controller
```

### Componentes Principais:
1. **Domain (`domain`)**: Contém as regras puras de negócio. O Enum `Regiao` centraliza as taxas, prazos e transportadoras de cada região brasileira, evitando regras espalhadas no código. A entidade `ConsultaLog` modela o registro histórico.
2. **Gateways (`gateway` / `domain`)**: Interfaces que definem as fronteiras do sistema para comunicação com o mundo externo (ViaCEP e Banco de Dados).
3. **Infrastructure (`infrastructure`)**: Configurações de banco de dados, exceptions

## 🌐 Demonstração da API em Produção
* **🚀 Link da API em Produção:** `https://logi-route-project.onrender.com`
> ⚠️ Como a aplicação está hospedada na camada gratuita do Render, a primeira requisição após minutos de inatividade, pode levar cerca de 50 segundos ou mais para responder.

### 🧪 Testando a API em Produção
#### 1. Executar Triagem
* **Endpoint:** `POST https://logi-route-project.onrender.com/pacotes`
* **Payload de entrada:**
```JSON
{
  "cep": "06040100",
  "peso": 20
}
```
* **Resposta esperada - 201 Created:**
```JSON
{
    "cep": "06040-100",
    "uf": "SP",
    "localidade": "Osasco",
    "zona": "Sudeste",
    "transportadora": "LogiExpress Sudeste",
    "prazoDiasUteis": 2,
    "valorFrete": 120.00
}
```
#### 2. Consultar histórico
* **Endpoint:** `GET https://logi-route-project.onrender.com/pacotes/logs`
* **Resposta esperada - 200 OK:**
```JSON
[
    {
        "uf": "SP",
        "localidade": "Osasco",
        "dataHora": "16/07/2026 19:15:21",
        "valorFrete": 120.00,
        "zona": "Sudeste",
        "transportadora": "LogiExpress Sudeste",
        "cep": "06040-100",
        "id": 1
    }
]
```
---
## 👩‍💻 Autor
- Desenvolvido por [@priscyladepaula](https://www.linkedin.com/in/priscyladepaula/)
package br.com.priscyladepaula.logirouteproject.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public enum Regiao {
    SUDESTE(
            "Sudeste",
            "LogiExpress Sudeste",
            2,
            BigDecimal.valueOf(5.50),
            BigDecimal.valueOf(10.00),
            "SP", "RJ", "MG", "ES"
    ),
    SUL(
            "Sul",
            "SulTrans Transportes",
            3,
            BigDecimal.valueOf(7.20),
            BigDecimal.valueOf(12.00),
            "PR", "SC", "RS"
    ),
    CENTRO_OESTE(
            "Centro-Oeste",
            "Cerrado Logística",
            4,
            BigDecimal.valueOf(8.50),
            BigDecimal.valueOf(15.00),
            "DF", "GO", "MS", "MT"
    ),
    NORDESTE(
            "Nordeste",
            "Asa Branca Express",
            5,
            BigDecimal.valueOf(10.00),
            BigDecimal.valueOf(18.00),
            "AL", "BA", "CE", "MA", "PB", "PE", "PI", "RN", "SE"
    ),
    NORTE(
            "Norte",
            "NorteVias Log",
            7,
            BigDecimal.valueOf(12.50),
            BigDecimal.valueOf(22.00),
            "AC", "AM", "AP", "PA", "RO", "RR", "TO"
    );

    private final String nome;
    private final String transportadora;
    private final int prazoDiasUteis;
    private final BigDecimal valorPorKg;
    private final BigDecimal taxaFixa;
    private final List<String> ufs;

    Regiao(String nome, String transportadora, int prazoDiasUteis, BigDecimal valorPorKg, BigDecimal taxaFixa, String... ufs) {
        this.nome = nome;
        this.transportadora = transportadora;
        this.prazoDiasUteis = prazoDiasUteis;
        this.valorPorKg = valorPorKg;
        this.taxaFixa = taxaFixa;
        this.ufs = Arrays.asList(ufs);
    }

    public String getNome() { return nome; }
    public String getTransportadora() { return transportadora; }
    public int getPrazoDiasUteis() { return prazoDiasUteis; }
    public BigDecimal getValorPorKg() { return valorPorKg; }
    public BigDecimal getTaxaFixa() { return taxaFixa; }

    public static Regiao obterPorUf(String uf) {
        if (uf == null) {
            return SUDESTE;
        }
        return Arrays.stream(values())
                .filter(regiao -> regiao.ufs.contains(uf.toUpperCase()))
                .findFirst()
                .orElse(SUDESTE);
    }
}

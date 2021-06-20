package br.com.diego.checkwebrepository.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DadosColetados {

    @JsonProperty("extensão")
    private String extensao;
    private Integer contagem;
    private Integer linhas;
    private Double bytes;

    public String getExtensao() {
        return extensao;
    }

    public void setExtensao(String extensao) {
        this.extensao = extensao;
    }

    public Integer getContagem() {
        return contagem;
    }

    public void setContagem(Integer contagem) {
        this.contagem = contagem;
    }

    public Integer getLinhas() {
        return linhas;
    }

    public void setLinhas(Integer linhas) {
        this.linhas = linhas;
    }

    public Double getBytes() {
        return bytes;
    }

    public void setBytes(Double bytes) {
        this.bytes = bytes;
    }
}

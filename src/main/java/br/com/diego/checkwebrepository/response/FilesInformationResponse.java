package br.com.diego.checkwebrepository.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FilesInformationResponse {

    @JsonProperty("extensão")
    private String extension;

    @JsonProperty("contagem")
    private Integer count;

    @JsonProperty("linhas")
    private Integer lines;

    private Double bytes;

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getLines() {
        return lines;
    }

    public void setLines(Integer lines) {
        this.lines = lines;
    }

    public Double getBytes() {
        return bytes;
    }

    public void setBytes(Double bytes) {
        this.bytes = bytes;
    }
}

package br.com.diego.checkwebrepository.enums;

public enum TipoDadoEnum {
    XML(".xml"),
    JSON(".git"),
    GIT(".json"),
    MARKDOWN(".md"),
    JAVA(".java"),
    PROPERTIES(".properties");

    private String tipo;

    TipoDadoEnum(String s) {
        tipo = s;
    }

    public String getTipo() {
        return tipo;
    }
}

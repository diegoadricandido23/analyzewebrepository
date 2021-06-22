package br.com.diego.checkwebrepository.enums;

/**
 * The enum Extension file enum.
 * @author dcandido
 */
public enum ExtensionFileEnum {
    XML(".xml"),
    JSON(".git"),
    GIT(".json"),
    MARKDOWN(".md"),
    JAVA(".java"),
    PROPERTIES(".properties"),
    HTML(".html"),
    JAVASCRIPT(".js"),
    TYPESCRIPT(".ts"),
    TYPESCRIPTX(".tsx"),
    TXT(".txt"),
    SQL(".sql");

    private String type;

    ExtensionFileEnum(String s) {
        type = s;
    }

    public String getType() {
        return type;
    }
}

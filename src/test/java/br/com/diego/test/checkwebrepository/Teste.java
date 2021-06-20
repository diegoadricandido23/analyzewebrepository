package br.com.diego.test.checkwebrepository;

import br.com.diego.checkwebrepository.enums.TipoDadoEnum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Teste {

    private HashMap<String, List<String>> dados = new HashMap<>();

    @Test
    public void teste() {

        for (TipoDadoEnum tipoDadoEnum : TipoDadoEnum.values()) {
            dados.put(tipoDadoEnum.name(), new ArrayList<>());
        }

        List<String> valores = List.of("testando.xml", "achei.xml");
        valores.forEach(href -> {
            if(href.contains(".")) {
                if (href.contains(TipoDadoEnum.XML.getTipo())) {
                    dados.get(TipoDadoEnum.XML.name()).add(href);
                }
            }
        });
        dados.keySet().stream().forEach(chave -> {
                System.out.println(chave);
                dados.get(chave).forEach(System.out::println);
            }
        );
    }

    @Test
    public void testeString() {
        String valor = "42 lines (35 sloc) 1.68 KB".toLowerCase();
        String linha = valor.substring(0, valor.lastIndexOf("lines")).trim();

        String busca = valor.contains("bytes") ? "bytes" : "kb";
        String auxTamanho = valor.substring(valor.indexOf(")"), valor.lastIndexOf(busca));

        String tamanho = "";
        for (int i = 0; i < auxTamanho.length(); i++) {
            if (Character.isDigit(auxTamanho.charAt(i)) || auxTamanho.charAt(i) == '.') {
                tamanho += auxTamanho.charAt(i);
            }
        }
        System.out.println(Double.parseDouble(tamanho) * 1000);
    }
}

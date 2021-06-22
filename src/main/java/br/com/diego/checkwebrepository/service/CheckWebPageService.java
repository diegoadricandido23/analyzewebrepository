package br.com.diego.checkwebrepository.service;

import br.com.diego.checkwebrepository.enums.ExtensionFileEnum;
import br.com.diego.checkwebrepository.response.FilesInformationResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CheckWebPageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckWebPageService.class);
    private static final String URL_BASE = "https://github.com/";
    private static final String LINK_INFO = "js-navigation-open Link--primary";
    private static final String CODE_INFO = "text-mono f6 flex-auto pr-3 flex-order-2 flex-md-order-1";

    private List<String> directoryList = new ArrayList<>();
    private List<String> auxDirectory = new ArrayList<>();
    private HashMap<String, List<String>> dados = new HashMap<>();
    private List<FilesInformationResponse> filesInformationResponseList = new ArrayList<>();

    @PostConstruct
    public void init() {
        for (ExtensionFileEnum extensionFileEnum : ExtensionFileEnum.values()) {
            dados.put(extensionFileEnum.name(), new ArrayList<>());
        }

    }

    public List<FilesInformationResponse> checkPage(final String user, final String repository) {
        try {
            checarPageInicial(user.concat("/").concat(repository));
            popularArquivos(user.concat("/").concat(repository));

            int tamanho = 0;
            do {
                tamanho = directoryList.size();
                checarExistenciaDiretorio();
                auxDirectory.forEach(dir -> {
                    if (!directoryList.contains(dir)) {
                        directoryList.add(dir);
                    }
                });
                auxDirectory.clear();
            } while (tamanho < directoryList.size());

            checarExistenciaArquivos();
            checarCodigo();
            imprimir();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return filesInformationResponseList;
    }

    private void checarPageInicial(final String pathInicial) throws IOException {
        LOGGER.info("CHECANDO PAGINA PRINCIPAL");
        popularDiretorio(pathInicial);
    }

    private void checarExistenciaDiretorio() {
        LOGGER.info("CHECANDO NOVOS DIRETORIOS");
        directoryList.forEach(diretorio -> {
            try {
                popularDiretorio(diretorio);
            } catch (IOException e) {
                LOGGER.error("ERROR ", e);
            }
        });
    }

    private void checarExistenciaArquivos() {
        LOGGER.info("CHECANDO ARQUIVOS");
        directoryList.forEach(diretorio -> {
            try {
                popularArquivos(diretorio);
            } catch (IOException e) {
                LOGGER.error("ERROR ", e);
            }
        });
    }

    private void popularDiretorio(final String path) throws IOException {
        LOGGER.info("POPULANDO DIRETORIO");
        Document doc = carregarDocumento(path);
        doc.getElementsByClass(LINK_INFO).forEach(x -> {
            String href = x.attr("href");
            if (!href.contains(".")) {
                if (directoryList.isEmpty()) {
                    directoryList.add(href);
                } else {
                    auxDirectory.add(href);
                }
            }
        });
    }

    private void popularArquivos(final String path) throws IOException {
        LOGGER.info("POPULANDO ARQUIVOS");
        Document doc = carregarDocumento(path);
        doc.getElementsByClass(LINK_INFO).forEach(x -> {
            String href = x.attr("href");
            if (href.contains(".")) {
                if (href.contains(ExtensionFileEnum.XML.getType())) {
                    dados.get(ExtensionFileEnum.XML.name()).add(href);
                } else if (href.contains(ExtensionFileEnum.GIT.getType())) {
                    dados.get(ExtensionFileEnum.GIT.name()).add(href);
                } else if (href.contains(ExtensionFileEnum.JSON.getType())) {
                    dados.get(ExtensionFileEnum.JSON.name()).add(href);
                } else if (href.contains(ExtensionFileEnum.MARKDOWN.getType())) {
                    dados.get(ExtensionFileEnum.MARKDOWN.name()).add(href);
                } else if (href.contains(ExtensionFileEnum.JAVA.getType())) {
                    dados.get(ExtensionFileEnum.JAVA.name()).add(href);
                } else if (href.contains(ExtensionFileEnum.PROPERTIES.getType())) {
                    dados.get(ExtensionFileEnum.PROPERTIES.name()).add(href);
                }
            }
        });
    }

    private Document carregarDocumento(final String path) throws IOException {
        return Jsoup.connect(URL_BASE.concat(path)).get();
    }

    private void checarCodigo() {
        LOGGER.info("CARREGAR CODIGO");
        AtomicInteger totalLinhas = new AtomicInteger();
        AtomicReference<Double> totalBytes = new AtomicReference<>((double) 0);

        dados.keySet().stream().forEach(tipoDado -> {
            dados.get(tipoDado).forEach(cod -> {
                try {
                    final Document doc = carregarDocumento(cod);
                    doc.getElementsByClass(CODE_INFO).forEach(x -> {
                        String valor = x.text().toLowerCase();
                        String linha = valor.substring(0, valor.lastIndexOf("lines")).trim();

                        String busca = valor.contains("bytes") ? "bytes" : "kb";
                        String auxTamanho = valor.substring(valor.indexOf(")"), valor.lastIndexOf(busca));

                        String tamanho = "";
                        for (int i = 0; i < auxTamanho.length(); i++) {
                            if (Character.isDigit(auxTamanho.charAt(i)) || auxTamanho.charAt(i) == '.') {
                                tamanho += auxTamanho.charAt(i);
                            }
                        }
                        totalLinhas.addAndGet(Integer.parseInt(linha));
                        String finalTamanho = tamanho;
                        totalBytes.updateAndGet(v -> v + (Double.parseDouble(finalTamanho) * 1000));
                    });
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
            if(totalLinhas.get() > 0) {
                FilesInformationResponse dc = new FilesInformationResponse();
                dc.setExtension(tipoDado);
                dc.setCount(dados.get(tipoDado).size());
                dc.setLines(totalLinhas.get());
                dc.setBytes(totalBytes.get());

                totalLinhas.set(0);
                totalBytes.set(0.0);
                filesInformationResponseList.add(dc);
            }
        });
    }

    private void imprimir() {
        dados.keySet().stream().forEach(chave -> {
            LOGGER.info(chave);
            dados.get(chave).forEach(x -> LOGGER.info(x));
        });
    }
}

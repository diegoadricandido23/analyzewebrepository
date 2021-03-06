package br.com.diego.checkwebrepository.service;

import br.com.diego.checkwebrepository.enums.ExtensionFileEnum;
import br.com.diego.checkwebrepository.enums.TypeSearchEnum;
import br.com.diego.checkwebrepository.exceptions.CheckPageCustomException;
import br.com.diego.checkwebrepository.model.FilesInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The type Check web page custom service.
 * @author dcandido
 */
@Service
public class CheckWebPageCustomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckWebPageCustomService.class);
    private static final String URL_BASE = "https://github.com";
    private static final String LINK_INFO = "js-navigation-open Link--primary";
    private static final String CODE_INFO = "text-mono f6 flex-auto pr-3 flex-order-2 flex-md-order-1";
    private static final String H_REF = "href=";

    private final List<String> directoryList = new ArrayList<>();
    private final List<String> auxDirectory = new ArrayList<>();
    private final HashMap<String, List<String>> mapWithInformation = new HashMap<>();
    private final List<FilesInformation> filesInformationResponseList = new ArrayList<>();

    @Autowired
    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        for (ExtensionFileEnum extensionFileEnum : ExtensionFileEnum.values()) {
            mapWithInformation.put(extensionFileEnum.name(), new ArrayList<>());
        }

    }

    /**
     * Start process to analise Page.
     *
     * @param user
     *         the user {@link String}
     * @param repository
     *         the repository {@link String}
     * @return the list {@link List<FilesInformation>}
     */
    @Cacheable(cacheNames = "resource", key = "#user + #repository")
    public List<FilesInformation> startProcess(final String user, final String repository) {
        final String path = "/".concat(user).concat("/").concat(repository);
        clear();
        checkInitialPage(path);
        extractInfoPage(path, TypeSearchEnum.ALL);

        var sizeList = 0;
        do {
            sizeList = directoryList.size();
            checkDirectory();
            auxDirectory.forEach(dir -> {
                if (!directoryList.contains(dir)) {
                    directoryList.add(dir);
                }
            });
            auxDirectory.clear();
        } while (sizeList < directoryList.size());

        checkFiles();
        checkCodes();

        return filesInformationResponseList;
    }

    /**
     * Start checking the initial Page
     * @param pathInitial {@link String}
     */
    private void checkInitialPage(final String pathInitial) {
        LOGGER.debug("CHECKING INITIAL PAGE");
        extractInfoPage(pathInitial, TypeSearchEnum.DIRECTORIES);
    }

    /**
     * Start process to check a Directory
     */
    private void checkDirectory(){
        LOGGER.debug("CHECKING NEW DIRECTORIES");
        directoryList.forEach(directory -> extractInfoPage(directory, TypeSearchEnum.DIRECTORIES));
    }

    /**
     * Start process to check a Files
     */
    private void checkFiles(){
        LOGGER.debug("CHECKING NEW FILES");
        directoryList.forEach(directory -> extractInfoPage(directory, TypeSearchEnum.FILES));
    }

    /**
     *  Start process to extract info of pages
     *
     * @param path {@link String}
     * @param typeSearchEnum {@link TypeSearchEnum}
     */
    private void extractInfoPage(final String path, final TypeSearchEnum typeSearchEnum) {
        var item = new StringBuilder();
        var itemAux = new StringBuilder();
        var page = searchPage(path);

        int pos = page.indexOf(LINK_INFO);
        while (pos > -1) {
            page = page.substring(pos);
            if (page.contains(H_REF)) {
                pos = 0;
                while (pos < page.length()) {
                    if (page.startsWith(H_REF, pos)) {
                        while (!String.valueOf(page.charAt(pos)).equals(">")) {
                            item.append(page.charAt(pos));
                            pos++;
                        }
                    }
                    if (item.length() > 0) {
                        for (var i = 0; i < item.length(); i++) {
                            if(String.valueOf(item.charAt(i)).equals("=")){
                                itemAux.append(item, i +2, item.length() -1);
                            }
                        }

                        if(typeSearchEnum == TypeSearchEnum.ALL) {
                            extractDirectories(itemAux);
                            extractFiles(itemAux);

                        } else if(typeSearchEnum == TypeSearchEnum.DIRECTORIES) {
                            extractDirectories(itemAux);

                        } else if (typeSearchEnum == TypeSearchEnum.FILES) {
                            extractFiles(itemAux);

                        }

                        item.setLength(0);
                        itemAux.setLength(0);
                        break;
                    }
                    pos++;
                }

            }
            page = page.substring(pos);
            pos = page.indexOf(LINK_INFO);
        }
    }

    /**
     * Start process to check a Codes Page
     *
     */
    private void checkCodes() {
        LOGGER.debug("CHECKING CODES");

        var totalLines = new AtomicInteger();
        AtomicReference<Double> totalBytes = new AtomicReference<>(0.0);
        var line = new StringBuilder();
        var bytes = new StringBuilder();

        mapWithInformation.keySet().stream().forEach(typeInformation -> {
            mapWithInformation.get(typeInformation).forEach(cod -> {
                var page = searchPage(cod);
                int pos = page.indexOf(CODE_INFO);
                while (pos < page.length()) {
                    page = page.substring(pos);
                    for (var i = 0; i < page.length(); i++) {
                        if(page.startsWith("1\">", i)){
                            pos = i;
                            break;
                        }
                    }
                    if (page.startsWith("1\">", pos)) {
                        pos +=3;
                        while (!String.valueOf(page.charAt(pos)).equals("<")) {
                            line.append(page.charAt(pos));
                            pos++;
                        }
                        if (line.toString().contains("(")) {
                            var aux = line.substring(0, line.indexOf("("));
                            line.setLength(0);
                            line.append(aux);
                        }
                    }
                    page = page.substring(pos);
                    for (var i = 0; i < page.length(); i++) {
                        if(page.startsWith("n>", i)){
                            pos = i;
                            break;
                        }
                    }
                    if (page.startsWith("n>", pos)) {
                        pos +=2;
                        while (!String.valueOf(page.charAt(pos)).equals("<")) {
                            bytes.append(page.charAt(pos));
                            pos++;
                        }
                    }
                    if (line.length() > 0) {
                        String linha = line.toString().replaceAll("[\\D]", "");
                        totalLines.addAndGet(Integer.parseInt(linha));
                    }
                    if(bytes.length() > 0) {
                        var bytesString = bytes.toString().toLowerCase().trim();
                        var typeExtension = bytesString.contains("bytes") ? "bytes" : "kb";

                        String auxSize;
                        if(bytesString.contains(")")) {
                            auxSize = bytesString.substring(bytesString.indexOf(")"), bytesString.lastIndexOf(typeExtension));
                        } else {
                            auxSize = bytesString;
                        }

                        var size = new StringBuilder();
                        for (var i = 0; i < auxSize.length(); i++) {
                            if (Character.isDigit(auxSize.charAt(i)) || auxSize.charAt(i) == '.') {
                                size.append(auxSize.charAt(i));
                            }
                        }
                        totalBytes.updateAndGet(v -> (v + (Double.parseDouble(size.toString()) * 1000)));
                    }
                    if(line.length() > 0 || bytes.length() > 0) {
                        line.setLength(0);
                        bytes.setLength(0);
                        break;
                    }
                    pos++;
                    page = page.substring(pos);
                    pos = page.indexOf(LINK_INFO);
                }
            });
            if(totalLines.get() > 0) {
                var dc = new FilesInformation();
                dc.setExtension(typeInformation);
                dc.setCount(mapWithInformation.get(typeInformation).size());
                dc.setLines(totalLines.get());
                dc.setBytes(totalBytes.get());

                totalLines.set(0);
                totalBytes.set(0.0);
                filesInformationResponseList.add(dc);
            }
        });
    }

    /**
     * The method search a page informed if dont't found
     * generate an Exception {@link CheckPageCustomException}
     *
     * @param path {@link String}
     * @return {@link String}
     */
    private String searchPage(final String path) {
        try {
            return restTemplate.getForObject(URL_BASE.concat(path), String.class);

        } catch (Exception e) {
            throw new CheckPageCustomException("PAGE NOT FOUND - " + URL_BASE.concat(path));
        }
    }

    /**
     * The method extract new Directories
     * @param itemAux {@link StringBuilder}
     */
    private void extractDirectories(final StringBuilder itemAux) {
        if (!itemAux.toString().contains(".")) {
            if (directoryList.isEmpty()) {
                directoryList.add(itemAux.toString());
            } else {
                auxDirectory.add(itemAux.toString());
            }
        }
    }

    /**
     * The method to do a split for type extension archives
     *
     * @param itemAux {@link StringBuilder}
     */
    private void extractFiles(final StringBuilder itemAux) {
        if (itemAux.toString().contains(".")) {
            if (itemAux.toString().contains(ExtensionFileEnum.XML.getType())) {
                mapWithInformation.get(ExtensionFileEnum.XML.name()).add(itemAux.toString());
            } else if (itemAux.toString().contains(ExtensionFileEnum.GITIGNORE.getType())) {
                mapWithInformation.get(ExtensionFileEnum.GITIGNORE.name()).add(itemAux.toString());
            } else if (itemAux.toString().contains(ExtensionFileEnum.JSON.getType())) {
                mapWithInformation.get(ExtensionFileEnum.JSON.name()).add(itemAux.toString());
            } else if (itemAux.toString().contains(ExtensionFileEnum.MARKDOWN.getType())) {
                mapWithInformation.get(ExtensionFileEnum.MARKDOWN.name()).add(itemAux.toString());
            } else if (itemAux.toString().contains(ExtensionFileEnum.JAVA.getType())) {
                mapWithInformation.get(ExtensionFileEnum.JAVA.name()).add(itemAux.toString());
            } else if (itemAux.toString().contains(ExtensionFileEnum.PROPERTIES.getType())) {
                mapWithInformation.get(ExtensionFileEnum.PROPERTIES.name()).add(itemAux.toString());
            } else if (itemAux.toString().contains(ExtensionFileEnum.HTML.getType())) {
                mapWithInformation.get(ExtensionFileEnum.HTML.name()).add(itemAux.toString());
            } else if (itemAux.toString().contains(ExtensionFileEnum.JAVASCRIPT.getType())) {
                mapWithInformation.get(ExtensionFileEnum.JAVASCRIPT.name()).add(itemAux.toString());
            } else if (itemAux.toString().contains(ExtensionFileEnum.TYPESCRIPT.getType())) {
                mapWithInformation.get(ExtensionFileEnum.TYPESCRIPT.name()).add(itemAux.toString());
            } else if (itemAux.toString().contains(ExtensionFileEnum.TYPESCRIPTX.getType())) {
                mapWithInformation.get(ExtensionFileEnum.TYPESCRIPTX.name()).add(itemAux.toString());
            } else if (itemAux.toString().contains(ExtensionFileEnum.TXT.getType())) {
                mapWithInformation.get(ExtensionFileEnum.TXT.name()).add(itemAux.toString());
            } else if (itemAux.toString().contains(ExtensionFileEnum.SQL.getType())) {
                mapWithInformation.get(ExtensionFileEnum.SQL.name()).add(itemAux.toString());
            }
        }
    }

    private void clear() {
        directoryList.clear();
        auxDirectory.clear();
        filesInformationResponseList.clear();
    }
}

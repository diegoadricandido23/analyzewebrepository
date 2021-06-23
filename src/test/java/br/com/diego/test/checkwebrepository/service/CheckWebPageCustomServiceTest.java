package br.com.diego.test.checkwebrepository.service;

import br.com.diego.checkwebrepository.exceptions.CheckPageCustomException;
import br.com.diego.checkwebrepository.model.FilesInformation;
import br.com.diego.checkwebrepository.service.CheckWebPageCustomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.when;

public class CheckWebPageCustomServiceTest {

    private static final String CLASS_PATH_PAGE = "classpath:paginaInicial.html";
    private static final String CLASS_PATH_GIT = "classpath:gitignore.html";
    private static final String CLASS_PATH_POM = "classpath:pom.html";
    private static final String CLASS_PATH_README = "classpath:readme.html";
    private static final String CLASS_PATH_SRC = "classpath:src.html";
    private static final String CLASS_PATH_MAIN = "classpath:main.html";
    private static final String CLASS_PATH_CLIMATE = "classpath:climate.html";
    private static final String CLASS_PATH_CODE = "classpath:code.html";
    private static final String CLASS_PATH_RESPONSE = "classpath:response.json";

    private static final String URL_BASE = "https://github.com/";
    private static final String URL_GIT = "https://github.com/diegoadricandido23/clima/blob/main/.gitignore";
    private static final String URL_README = "https://github.com/diegoadricandido23/clima/blob/main/README.md";
    private static final String URL_POM = "https://github.com/diegoadricandido23/clima/blob/main/pom.xml";
    private static final String URL_SRC = "https://github.com/diegoadricandido23/clima/tree/main/src";
    private static final String URL_MAIN = "https://github.com/diegoadricandido23/clima/tree/main/src/main";
    private static final String URL_CLIMATE = "https://github.com/diegoadricandido23/clima/tree/main/src/main/java/com/diego/clima";
    private static final String URL_CODE = "https://github.com/diegoadricandido23/clima/blob/main/src/main/java/com/diego/clima/ClimaApplication.java";

    @InjectMocks
    private CheckWebPageCustomService service;
    @Mock
    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;

    @Before
    public void init() throws IOException {
        MockitoAnnotations.openMocks(this);
        service.init();
        objectMapper = new ObjectMapper();

        when(restTemplate.getForObject(URL_BASE.concat("usuario/repositorio"), String.class))
                .thenReturn(FileUtils.readFileToString(ResourceUtils.getFile(CLASS_PATH_PAGE), "UTF-8"));
        when(restTemplate.getForObject(URL_GIT, String.class))
                .thenReturn(FileUtils.readFileToString(ResourceUtils.getFile(CLASS_PATH_GIT), "UTF-8"));
        when(restTemplate.getForObject(URL_README, String.class))
                .thenReturn(FileUtils.readFileToString(ResourceUtils.getFile(CLASS_PATH_README), "UTF-8"));
        when(restTemplate.getForObject(URL_POM, String.class))
                .thenReturn(FileUtils.readFileToString(ResourceUtils.getFile(CLASS_PATH_POM), "UTF-8"));
        when(restTemplate.getForObject(URL_SRC, String.class))
                .thenReturn(FileUtils.readFileToString(ResourceUtils.getFile(CLASS_PATH_SRC), "UTF-8"));
        when(restTemplate.getForObject(URL_MAIN, String.class))
                .thenReturn(FileUtils.readFileToString(ResourceUtils.getFile(CLASS_PATH_MAIN), "UTF-8"));
        when(restTemplate.getForObject(URL_CLIMATE, String.class))
                .thenReturn(FileUtils.readFileToString(ResourceUtils.getFile(CLASS_PATH_CLIMATE), "UTF-8"));
        when(restTemplate.getForObject(URL_CODE, String.class))
                .thenReturn(FileUtils.readFileToString(ResourceUtils.getFile(CLASS_PATH_CODE), "UTF-8"));
    }

    @Test
    public void shouldCheckPageSuccess() throws IOException {

        List<FilesInformation> files = service.startProcess("usuario", "repositorio");
        Assert.assertEquals(getResponse(),  objectMapper.writeValueAsString(files));
    }

    @Test(expected = CheckPageCustomException.class)
    public void shouldCheckPageSuccessException() {
        service.startProcess("usuario", "repositorio/");
    }

    private String getResponse() throws IOException {
        return FileUtils.readFileToString(ResourceUtils.getFile(CLASS_PATH_RESPONSE), "UTF-8");
    }
}
package br.com.diego.test.checkwebrepository.controller;

import br.com.diego.checkwebrepository.controller.CheckWebPageController;
import br.com.diego.checkwebrepository.exceptions.ApiErrorHandler;
import br.com.diego.checkwebrepository.service.CheckWebPageCustomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(MockitoJUnitRunner.class)
public class CheckWebPageServiceTest {

    private static final String BASE_URI = "/check";

    private MockMvc mvc;
    @InjectMocks
    private CheckWebPageController controller;

    @Mock
    private CheckWebPageCustomService service;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ApiErrorHandler())
                .build();
    }

    @Test
    public void shouldVerifyPageWithSuccess() throws Exception {
        MockHttpServletRequestBuilder get = get(BASE_URI + "/usuario/repositorio")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mvc.perform(get).andReturn();

        final int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    public void shouldVerifyPageNotFound() throws Exception {
        MockHttpServletRequestBuilder get = get(BASE_URI + "/usuario/repositorio/teste")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mvc.perform(get).andReturn();

        final int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }
}
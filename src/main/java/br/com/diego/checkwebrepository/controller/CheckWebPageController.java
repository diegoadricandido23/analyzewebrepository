package br.com.diego.checkwebrepository.controller;

import br.com.diego.checkwebrepository.response.DadosColetados;
import br.com.diego.checkwebrepository.service.CheckWebPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/check")
public class CheckWebPageController {

    @Autowired
    private CheckWebPageService checkWebService;

    @GetMapping("/{user}/{repository}")
    public ResponseEntity<List<DadosColetados>> checaRepositorio(@PathVariable String user, @PathVariable String repository) {
        final List<DadosColetados> dadosColetados = checkWebService.checkPage(user, repository);
        return ResponseEntity.ok(dadosColetados);
    }
}

package br.com.diego.checkwebrepository.controller;

import br.com.diego.checkwebrepository.model.FilesInformation;
import br.com.diego.checkwebrepository.response.FilesInformationResponse;
import br.com.diego.checkwebrepository.service.CheckWebPageCustomService;
import br.com.diego.checkwebrepository.service.CheckWebPageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Check web page controller.
 * @author dcandido
 */
@RestController
@RequestMapping("/check")
public class CheckWebPageController {

    @Autowired
    private CheckWebPageService checkWebService;

    @Autowired
    private CheckWebPageCustomService checkWebPageCustomService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Checa repositorio response entity.
     *
     * @param user the user
     * @param repository the repository
     * @return the response entity
     */
    @GetMapping("/{user}/{repository}")
    public ResponseEntity<List<FilesInformationResponse>> checaRepositorio(@PathVariable String user, @PathVariable String repository) {
        final List<FilesInformationResponse> dadosColetados = checkWebService.checkPage(user, repository);
        return ResponseEntity.ok(dadosColetados);
    }

    /**
     * Checa repositorio v 1 response entity.
     *
     * @param user the user
     * @param repository the repository
     * @return the response entity
     */
    @GetMapping("/v1/{user}/{repository}")
    public ResponseEntity<List<FilesInformationResponse>> checaRepositorioV1(@PathVariable String user, @PathVariable String repository) {
        final List<FilesInformation> filesInformations = checkWebPageCustomService.startProcess(user, repository);
        return ResponseEntity.ok(filesInformations.stream().map(this::modelToResponse).collect(Collectors.toList()));
    }

    /**
     * The method receive a object model and convert to object response (dto)
     * @param files {@link FilesInformation}
     * @return {@link FilesInformationResponse}
     */
    private FilesInformationResponse modelToResponse(final FilesInformation files) {
        return modelMapper.map(files, FilesInformationResponse.class);
    }
}

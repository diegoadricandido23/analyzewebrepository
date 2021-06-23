package br.com.diego.checkwebrepository.controller;

import br.com.diego.checkwebrepository.model.FilesInformation;
import br.com.diego.checkwebrepository.response.FilesInformationResponse;
import br.com.diego.checkwebrepository.service.CheckWebPageCustomService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckWebPageController.class);

    @Autowired
    private CheckWebPageCustomService checkWebPageCustomService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Check informed repository.
     *
     * @param user the user
     * @param repository the repository
     * @return the response entity
     */
    @GetMapping("/{user}/{repository}")
    public ResponseEntity<List<FilesInformationResponse>> checkRepository(@PathVariable String user, @PathVariable String repository) {

        LOGGER.info("INITIAL PROCESS");
        final List<FilesInformation> filesInformation = checkWebPageCustomService.startProcess(user, repository);

        LOGGER.info("FINALLY PROCESS");
        return ResponseEntity.ok(filesInformation.stream().map(this::modelToResponse).collect(Collectors.toList()));
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

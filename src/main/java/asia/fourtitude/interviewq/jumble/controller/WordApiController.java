package asia.fourtitude.interviewq.jumble.controller;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import asia.fourtitude.interviewq.jumble.core.JumbleEngine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Word API", description = "Word REST API endpoint.")
@RequestMapping(path = "/api/word")
public class WordApiController {

    private static final Logger LOG = LoggerFactory.getLogger(WordApiController.class);

    private final JumbleEngine jumbleEngine;

    @Autowired(required = true)
    public WordApiController(JumbleEngine jumbleEngine) {
        this.jumbleEngine = jumbleEngine;
    }

    @Operation(
            summary = "Auto complete based on prefix",
            description = "Returns a list of words matching the input `prefix` (of at least 3 letters).")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    name = "Success",
                                                    description = "The list of words matching the `prefix`.",
                                                    value = "[\n" +
                                                            "  \"awe\",\n" +
                                                            "  \"awed\",\n" +
                                                            "  \"awes\",\n" +
                                                            "  \"awesome\",\n" +
                                                            "  \"awesomely\",\n" +
                                                            "  \"awesomeness\",\n" +
                                                            "  \"awestruck\"\n" +
                                                            "]") })) })
    @GetMapping(value = "/{prefix}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<String>> autoComplete(
            @Parameter(
                    description = "The prefix.",
                    required = true,
                    example = "awe")
            @PathVariable String prefix) {
        prefix = StringUtils.trimToEmpty(prefix);
        if (prefix.length() < 3) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
        Collection<String> words = this.jumbleEngine.wordsMatchingPrefix(prefix);
        return new ResponseEntity<>(words, HttpStatus.OK);
    }

}

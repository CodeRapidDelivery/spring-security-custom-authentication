package com.marbor.customauthentication.resources;

import com.marbor.customauthentication.resources.dto.HelloResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Hello", description = "Hello world operations")
@ExposesResourceFor(String.class)
public interface HelloWorldResourceDefinition {

    String UNAUTHORIZED_EXAMPLE = "{\n" +
            "  \"timestamp\": \"2021-02-18T19:29:46.687+0000\",\n" +
            "  \"status\": 403,\n" +
            "  \"error\": \"Forbidden\",\n" +
            "  \"message\": \"Access Denied\",\n" +
            "  \"path\": \"/v1/api/hello\"\n" +
            "}";

    @Operation(
            security = {@SecurityRequirement(name = "JWT")},
            summary = "Get hello world message.",
            description = "Get hello world message.",
            method = "GET",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved last data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = String.class),
                                    examples = {
                                            @ExampleObject(name = "200", value = "{ \"message\" : \"Hello world!\" }")
                                    })),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(name = "401", value = UNAUTHORIZED_EXAMPLE)
                                    })),
                    @ApiResponse(
                            responseCode = "403",
                            description = "This operation is forbidden for this user"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Unexpected Internal Error")
            })
    ResponseEntity<HelloResponse> hello();
}

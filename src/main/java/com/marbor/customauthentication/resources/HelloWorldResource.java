package com.marbor.customauthentication.resources;

import com.marbor.customauthentication.resources.dto.HelloResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldResource implements HelloWorldResourceDefinition {

    @GetMapping(Routes.HELLO_WORLD_ROUTE)
    @Override
    public ResponseEntity<HelloResponse> hello() {
        return new ResponseEntity<>(new HelloResponse("Hello world!"), HttpStatus.OK);
    }
}

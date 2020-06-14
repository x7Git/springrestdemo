package com.example.springrestdemo.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LiveCheck {

    @GetMapping("/"+CtxPath.SYSTEM_ALIVE)
    public ResponseEntity<String> systemAlive(){
        return ResponseEntity.ok().build();
    }
}

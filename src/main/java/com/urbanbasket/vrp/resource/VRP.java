package com.urbanbasket.vrp.resource;

import org.springframework.http.ResponseEntity;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/VRP")
public class VRP {

    @PostMapping
    public @ResponseBody
    ResponseEntity<List<String>> post() {

    }

}

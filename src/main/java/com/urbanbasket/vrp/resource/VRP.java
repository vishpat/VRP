package com.urbanbasket.vrp.resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import com.urbanbasket.vrp.Location;

@RestController
@RequestMapping("v1/VRP")
public class VRP {

    private static final Logger logger = LogManager.getLogger(VRP.class);

    @GetMapping("healthz")
    public @ResponseBody
    ResponseEntity<String> healthz() {
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @PutMapping("addresses")
    public @ResponseBody
    ResponseEntity<List<Location>> sortAddresses(@RequestBody List<Location> customerLocations) {
        customerLocations.forEach(location -> logger.debug(location));
        return new ResponseEntity<>(customerLocations,HttpStatus.CREATED);
    }

}

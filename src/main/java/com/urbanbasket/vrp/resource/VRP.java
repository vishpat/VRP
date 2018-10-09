package com.urbanbasket.vrp.resource;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;
import org.optaplanner.examples.vehiclerouting.domain.location.RoadLocation;
import org.optaplanner.examples.vehiclerouting.domain.Customer;
import org.optaplanner.examples.vehiclerouting.domain.Depot;
import org.optaplanner.examples.vehiclerouting.domain.Vehicle;

import com.urbanbasket.vrp.Location;

import java.util.ArrayList;
import java.util.Arrays;


@RestController
@RequestMapping("v1/VRP")
public class VRP {

    private static final Logger logger = LogManager.getLogger(VRP.class);

    @GetMapping("healthz")
    public @ResponseBody
    ResponseEntity<String> healthz() {
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }


    private static ArrayList<Location> solve(ArrayList<Location> unsortedLocations) {
        ArrayList<Location>sortedLocations = new ArrayList();
        VehicleRoutingSolution vrpSolution = new VehicleRoutingSolution();

        return sortedLocations;
    }

    @PutMapping("addresses")
    public @ResponseBody
    ResponseEntity<String[]> sortAddresses(@RequestBody String customerLocations[]) {

        ArrayList<String> locations = new ArrayList<>();

        Arrays.stream(customerLocations).forEach(customerLocation -> {
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(System.getenv("GOOGLE_MAP_API_KEY"))
                    .build();
            try {
                GeocodingResult[] results = GeocodingApi.geocode(context,
                        customerLocation).await();

                Location location = Location.getInstance(customerLocation);
                location.setLatitude(results[0].geometry.location.lat);
                location.setLongitude(results[0].geometry.location.lng);
                locations.add(location.toString());
            } catch (Exception exp) {
                logger.error("Hit exception " + exp.toString());
            }
        });

        return new ResponseEntity<>(locations.toArray(new String[0]), HttpStatus.CREATED);
    }

}

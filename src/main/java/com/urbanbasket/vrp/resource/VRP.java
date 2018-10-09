package com.urbanbasket.vrp.resource;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.optaplanner.examples.vehiclerouting.domain.location.DistanceType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;
import org.optaplanner.examples.vehiclerouting.domain.location.RoadLocation;
import org.optaplanner.examples.vehiclerouting.domain.location.Location;
import org.optaplanner.examples.vehiclerouting.domain.Customer;
import org.optaplanner.examples.vehiclerouting.domain.Depot;
import org.optaplanner.examples.vehiclerouting.domain.Vehicle;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("v1/VRP")
public class VRP {

    private static final Logger logger = LogManager.getLogger(VRP.class);

    @GetMapping("healthz")
    public @ResponseBody
    ResponseEntity<String> healthz() {
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }


    private double getDistance(double lat1, double lon1, double lat2, double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }

    private static ArrayList<Location> solve(ArrayList<Location> unsortedLocations) {
        ArrayList<Location>sortedLocations = new ArrayList();
        VehicleRoutingSolution vrpSolution = new VehicleRoutingSolution();

        return sortedLocations;
    }

    private void calculateDistanceMatrix(List<Location> roadLocations) {

        roadLocations.stream().forEach(roadLocationObj -> {
            RoadLocation roadLocation = (RoadLocation)roadLocationObj;

            roadLocations.stream().forEach(neighboringRoadLocationObj -> {
                RoadLocation neighboringRoadLocation = (RoadLocation)neighboringRoadLocationObj;
                if (roadLocation.getId() != neighboringRoadLocation.getId()) {
                    double distance = this.getDistance(roadLocation.getLatitude(), roadLocation.getLongitude(),
                            neighboringRoadLocation.getLatitude(), neighboringRoadLocation.getLongitude());
                    roadLocation.getTravelDistanceMap().put(neighboringRoadLocation, distance);
                }
            });
        });
    }

    @PutMapping("addresses")
    public @ResponseBody
    ResponseEntity<String[]> sortAddresses(@RequestBody VrpParameters parameters) {

        VehicleRoutingSolution vrpSolution = new VehicleRoutingSolution();
        List<Location> roadLocations = new ArrayList<>();
        int id = 0;

        for (String customerLocation : parameters.getCustomerLocations()) {
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(System.getenv("GOOGLE_MAPS_API_KEY"))
                    .build();
            try {
                GeocodingResult[] results = GeocodingApi.geocode(context,
                        customerLocation).await();

                RoadLocation roadLocation = new RoadLocation();
                roadLocation.setName(customerLocation);
                roadLocation.setLatitude(results[0].geometry.location.lat);
                roadLocation.setLongitude(results[0].geometry.location.lng);
                roadLocation.setId(Long.valueOf(id));
                roadLocations.add(roadLocation);

                id += 1;
            } catch (Exception exp) {
                logger.error("Hit exception " + exp.toString());
            }
        }

        this.calculateDistanceMatrix(roadLocations);

        vrpSolution.setLocationList(roadLocations);
        vrpSolution.setDistanceType(DistanceType.ROAD_DISTANCE);

        return new ResponseEntity<>(roadLocations.toArray(new String[0]), HttpStatus.CREATED);
    }

}

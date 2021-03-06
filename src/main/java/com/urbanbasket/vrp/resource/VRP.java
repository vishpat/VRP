package com.urbanbasket.vrp.resource;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.localsearch.LocalSearchPhaseConfig;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.optaplanner.examples.vehiclerouting.domain.Customer;
import org.optaplanner.examples.vehiclerouting.domain.Depot;
import org.optaplanner.examples.vehiclerouting.domain.Vehicle;
import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;
import org.optaplanner.examples.vehiclerouting.domain.location.DistanceType;
import org.optaplanner.examples.vehiclerouting.domain.location.Location;
import org.optaplanner.examples.vehiclerouting.domain.location.RoadLocation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

class VRPSolution {

    private static final Logger logger = LogManager.getLogger(VRPSolution.class);
    private static final Integer CUSTOMER_DEMAND = 1;
    private static final Integer VEHICLE_COUNT = 1;
    private static final Integer VEHICLE_CAPACITY = 10000;
    private static final Integer STEP_LIMIT = 5000;
    private static final String MOVE_THREAD_COUNT = "4";
    private final VrpParameters parameters;
    private final VehicleRoutingSolution vrpSolution;
    private final List<Location> roadLocations = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();
    private final List<Vehicle> vehicles = new ArrayList<>();
    private final List<Depot> depots = new ArrayList<>();
    private Long locationId = new Long(0);
    private Long depotID = null;
    private Long vehicleID = new Long(1);
    private GeoApiContext context = new GeoApiContext.Builder()
            .apiKey(System.getenv("GOOGLE_MAPS_API_KEY"))
            .build();


    VRPSolution(VrpParameters parameters) {
        this.parameters = parameters;
        this.vrpSolution = new VehicleRoutingSolution();
        logger.isEnabled(Level.DEBUG);
    }

    private Long getNextRoadID() {
        locationId += 1;
        return locationId;
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

    private void calculateDistanceMatrix(List<Location> roadLocations) {

        roadLocations.stream().forEach(roadLocationObj -> {
            RoadLocation roadLocation = (RoadLocation) roadLocationObj;
            roadLocation.setTravelDistanceMap(new HashMap<>());

            roadLocations.stream().forEach(neighboringRoadLocationObj -> {
                RoadLocation neighboringRoadLocation = (RoadLocation) neighboringRoadLocationObj;
                if (roadLocation.getId() != neighboringRoadLocation.getId()) {
                    double distance = this.getDistance(roadLocation.getLatitude(), roadLocation.getLongitude(),
                            neighboringRoadLocation.getLatitude(), neighboringRoadLocation.getLongitude());
                    roadLocation.getTravelDistanceMap().put(neighboringRoadLocation, distance);
                }
            });
        });
    }

    private void setupCustomers() {
        this.roadLocations.stream().forEach(roadLocation -> {
            if (roadLocation.getId() != this.depotID) {
                Customer customer = new Customer();
                customer.setId(roadLocation.getId());
                customer.setDemand(CUSTOMER_DEMAND);
                customer.setLocation(roadLocation);
                customers.add(customer);
            }
        });

        this.vrpSolution.setCustomerList(this.customers);
    }

    private void setupVehicles() {
        for (int i = 0; i < VEHICLE_COUNT; i++) {
            Vehicle vehicle = new Vehicle();
            vehicle.setId(this.vehicleID);
            vehicle.setCapacity(VEHICLE_CAPACITY);
            vehicle.setDepot(this.depots.get(0));
            vehicles.add(vehicle);
        }
        this.vrpSolution.setVehicleList(vehicles);
    }

    private void setupDepots() {
        try {
            GeocodingResult[] results = GeocodingApi.geocode(context,
                    parameters.getDepot()).await();

            RoadLocation roadLocation = new RoadLocation();
            roadLocation.setName(parameters.getDepot());
            roadLocation.setLatitude(results[0].geometry.location.lat);
            roadLocation.setLongitude(results[0].geometry.location.lng);
            roadLocation.setId(this.getNextRoadID());
            this.depotID = roadLocation.getId();

            Depot depot = new Depot();
            depot.setLocation(roadLocation);
            depot.setId(this.depotID);
            this.depots.add(depot);
            vrpSolution.setDepotList(this.depots);
        } catch (Exception exp) {
            logger.error("Hit exception " + exp.toString());
        }
    }

    private void setupRoadLocations() {
        for (String customerLocation : parameters.getCustomerLocations()) {
            try {
                GeocodingResult[] results = GeocodingApi.geocode(context,
                        customerLocation).await();

                RoadLocation roadLocation = new RoadLocation();
                roadLocation.setName(customerLocation);
                roadLocation.setLatitude(results[0].geometry.location.lat);
                roadLocation.setLongitude(results[0].geometry.location.lng);
                roadLocation.setId(this.getNextRoadID());
                this.roadLocations.add(roadLocation);
            } catch (Exception exp) {
                logger.error("Hit exception " + exp.toString());
            }
        }

        this.depots.forEach(depot -> {
            this.roadLocations.add(depot.getLocation());
        });

        this.calculateDistanceMatrix(this.roadLocations);
        vrpSolution.setLocationList(this.roadLocations);
        vrpSolution.setDistanceType(DistanceType.ROAD_DISTANCE);
    }

    public List<Customer> solve() {
        this.setupDepots();
        this.setupRoadLocations();
        this.setupCustomers();
        this.setupVehicles();

        SolverFactory<VehicleRoutingSolution> solverFactory = SolverFactory.createFromXmlResource(
                "org/optaplanner/examples/vehiclerouting/solver/vehicleRoutingSolverConfig.xml");
        SolverConfig solverConfig = solverFactory.getSolverConfig();
        solverConfig.getPhaseConfigList().forEach(phaseConfig -> {
            if (LocalSearchPhaseConfig.class.isAssignableFrom(phaseConfig.getClass())) {
                phaseConfig.setTerminationConfig(new TerminationConfig().withStepCountLimit(STEP_LIMIT));
            }
        });
        solverConfig.setMoveThreadCount(MOVE_THREAD_COUNT);

        Solver<VehicleRoutingSolution> solver = solverFactory.buildSolver();
        VehicleRoutingSolution bestSolution = solver.solve(this.vrpSolution);

        final List<Customer> customerRoutes = new ArrayList<>();
        bestSolution.getVehicleList().stream().forEach(vehicle -> {
            Customer customer = vehicle.getNextCustomer();
            while (customer != null) {
                customerRoutes.add(customer);
                customer = customer.getNextCustomer();
            }
        });

        return customerRoutes;
    }
}


@RestController
@RequestMapping("v1/VRP")
public class VRP {

    private static ArrayList<Location> solve(ArrayList<Location> unsortedLocations) {
        ArrayList<Location> sortedLocations = new ArrayList();
        VehicleRoutingSolution vrpSolution = new VehicleRoutingSolution();
        return sortedLocations;
    }

    @GetMapping("healthz")
    public @ResponseBody
    ResponseEntity<String> healthz() {
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @PutMapping("addresses")
    public @ResponseBody
    ResponseEntity<String[]> sortAddresses(@RequestBody VrpParameters parameters) {
        VRPSolution vrpSolution = new VRPSolution(parameters);
        List<Customer> customers = vrpSolution.solve();
        List<String> locations = customers.stream().map(customer -> customer.getLocation().getName()).collect(Collectors.toList());
        return new ResponseEntity<>(locations.toArray(new String[0]), HttpStatus.CREATED);
    }

}

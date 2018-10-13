VRP
===
Java springboot REST service for Vehicle Routing Problem. The service finds an optimal solution of the Vehicle routing problem using Optaplanner_ and Google Maps API_

..  _OptaPlanner : https://www.optaplanner.org/
..  _API : https://developers.google.com/maps/documentation/geolocation/intro


Build
-----
::

      git clone https://github.com/vishpat/VRP
      mvn package


Run
---
::
    
    export GOOGLE_MAPS_API_KEY=<Google Geolocation API key>
    java -jar target/vrp-0.0.1-SNAPSHOT.jar


Usage
-----

Sample Json Input

::

    {  
   "customerLocations":[  
      "Customer Address 1",
      "Customer Address 2",
      "Customer Address 3",
      "Customer Address 4",
      "Customer Address 5",
   ],
   "depot":"Depot Address",
  }


The output is Json array of sorted addresses.

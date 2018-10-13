VRP
===
Java springboot REST service for Vehicle Routing Problem 

Build
-----
::

      git clone https://github.com/vishpat/VRP
      mvn package


Run
---
::
    
    export GOOGLE_MAPS_API_KEY=<Google Map API key>
    java -jar target/vrp-0.0.1-SNAPSHOT.jar


Usage
-----

Sample Input

::

    {  
   "customerLocations":[  
      "300 constitution av apt 133, Bayonne, NJ 7002",
      "17 new street, FL-1,    Bayonne,    NJ,    7002",
      "3 Pamrapo CT Apt 3A,    Bayonne,    NJ,    7002",
      "300 constitution ave apt 418,    Bayonne,    NJ,    7002",
      "183 West 49th street Apt 3A,    Bayonne,    NJ,    7002",
      "3 pamrapo ct Apt 3A,    Bayonne,    NJ,    7002"
   ],
   "depot":"180 Talmadge rd, Edison, Nj",
   "carCount":1
  }


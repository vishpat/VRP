Description
===========

A simple Java REST service for solving Vehicle Routing Problem. The service finds an optimal path to be taken by vehicle delivering goods to different customer locations starting for the depot. The REST serivce is built using SpringBoot and utilizes Optaplanner_ and Google Geolocation API_. The Google GeoLocation API is used to determine the latitude and longitude of the customer location while Optaplanner is use to determine the optiomal delivery path.


..  _OptaPlanner : https://www.optaplanner.org/
..  _API : https://developers.google.com/maps/documentation/geolocation/intro


Requirements
------------

JDK 10


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


The output is Json array of sorted addresses. sample_client.py_ provides an example of how to use the REST api.

.. _sample_client.py : https://github.com/vishpat/VRP/blob/master/sample_client.py

#!/usr/bin/env python
u 

import requests
import json

URL = "http://localhost:8080/v1/VRP/addresses"

customer_locations = ["addr1", "addr2", "addr3", "addr4", "addr5"]

depot_address = "180 Talmadge rd, Edison, Nj"

r = requests.put(URL, json={'customerLocations': customer_locations, 'depot': depot_address})
print(r.status_code)
print(json.dumps(r.json(), indent=4))

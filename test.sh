locations="[\"Address 1\",
        \"Address 2\",
        \"Address 3\",
        \"Address 4\",
        \"Address 5\",
         \"Address 6\"]"

depot="\"UrbanBasket Address\""

payload="{\"customerLocations\": $locations, \"depot\":$depot}"
echo $payload | python -m json.tool
curl -H 'Content-Type: application/json' -H 'Accept: application/json'   -X PUT -d "$payload" http://localhost:8080/v1/VRP/addresses

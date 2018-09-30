data="[\"300 constitution av apt 133, Bayonne, NJ 7002\",
        \"17 new street, FL-1,    Bayonne,    NJ,    7002\",
        \"3 Pamrapo CT Apt 3A,    Bayonne,    NJ,    7002\",
        \"300 constitution ave apt 418,    Bayonne,    NJ,    7002\",
        \"183 West 49th street Apt 3A,    Bayonne,    NJ,    7002\",
         \"3 pamrapo ct Apt 3A,    Bayonne,    NJ,    7002\"]"

curl -H 'Content-Type: application/json' -H 'Accept: application/json'   -X PUT -d "$data" http://localhost:8080/v1/VRP/addresses

#!/bin/bash

set -exu

## prepare data
if [[ ! -d tmp ]] ; then
    mkdir tmp
fi

curl http://localhost:8080/json/string > tmp/string.json
curl http://localhost:8080/json/int > tmp/int.json
curl http://localhost:8080/json/list > tmp/list.json
curl http://localhost:8080/json/map > tmp/map.json
curl http://localhost:8080/json/user > tmp/user.json

curl http://localhost:8080/cbor/string > tmp/string.cbor
curl http://localhost:8080/cbor/int > tmp/int.cbor
curl http://localhost:8080/cbor/list > tmp/list.cbor
curl http://localhost:8080/cbor/map > tmp/map.cbor
curl http://localhost:8080/cbor/user > tmp/user.cbor

hyperfine --warmup 10 --export-json tmp/benchJsonString.json 'curl -f -H "Content-Type: application/json" --data-binary @tmp/string.json http://localhost:8080/json/string'
hyperfine --warmup 10 --export-json tmp/benchJsonInt.json    'curl -f -H "Content-Type: application/json" --data-binary @tmp/int.json    http://localhost:8080/json/int'
hyperfine --warmup 10 --export-json tmp/benchJsonList.json   'curl -f -H "Content-Type: application/json" --data-binary @tmp/list.json   http://localhost:8080/json/list'
hyperfine --warmup 10 --export-json tmp/benchJsonMap.json    'curl -f -H "Content-Type: application/json" --data-binary @tmp/map.json    http://localhost:8080/json/map'
hyperfine --warmup 10 --export-json tmp/benchJsonUser.json   'curl -f -H "Content-Type: application/json" --data-binary @tmp/user.json   http://localhost:8080/json/user'

hyperfine --warmup 10 --export-json tmp/benchCborString.json 'curl -f -H "Content-Type: application/cbor" --data-binary @tmp/string.cbor http://localhost:8080/cbor/string'
hyperfine --warmup 10 --export-json tmp/benchCborInt.json    'curl -f -H "Content-Type: application/cbor" --data-binary @tmp/int.cbor    http://localhost:8080/cbor/int'
hyperfine --warmup 10 --export-json tmp/benchCborList.json   'curl -f -H "Content-Type: application/cbor" --data-binary @tmp/list.cbor   http://localhost:8080/cbor/list'
hyperfine --warmup 10 --export-json tmp/benchCborMap.json    'curl -f -H "Content-Type: application/cbor" --data-binary @tmp/map.cbor    http://localhost:8080/cbor/map'
hyperfine --warmup 10 --export-json tmp/benchCborUser.json   'curl -f -H "Content-Type: application/cbor" --data-binary @tmp/user.cbor   http://localhost:8080/cbor/user'

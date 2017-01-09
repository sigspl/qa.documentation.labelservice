#!/bin/bash

port=9009
url="http://localhost:$port"


api_call="$url/qa/documentation"
response=$(curl --write-out %{http_code} --silent --output /dev/null $api_call)

if [ $response != "200" ]; then
	echo "ERROR: http code $response. Expected 200 for URL=$url"
	response=$(curl --write-out %{http_code} --silent --output /dev/null servername)
	exit 1
else
	echo "SUCCESS! (http code 200)";
fi

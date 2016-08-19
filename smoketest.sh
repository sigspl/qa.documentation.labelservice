#!/bin/bash

url="http://$1:$2"

response=$(curl --write-out %{http_code} --silent --output /dev/null $url)

if [ $response != "200" ]; then
	echo "ERROR: http code $response. Expected 200 for URL=$url"
	response=$(curl --write-out %{http_code} --silent --output /dev/null servername)
	exit 1
else
	echo "SUCCESS! (http code 200)";
fi

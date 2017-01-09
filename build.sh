DATE=$(date +"%Y%m%d")
ORG="iais.nm"
PRODUCT="org.fiware.qa.measurements"

TAG="$ORG/$PRODUCT:$DATE"
TAG_LATEST="$ORG/$PRODUCT:latest"
#PORT_MAPPING="-p 4711:8080"
PORT_MAPPING="-p 9009:9009"

echo "building image: $TAG"
docker build -f Dockerfile -t $TAG -t $TAG_LATEST .

docker rm -f $PRODUCT || true


docker run -d $PORT_MAPPING --name=$PRODUCT -v ~/.m2-docker:/root/.m2 $TAG_LATEST tail -f /dev/null
docker exec $PRODUCT bash -c "mvn exec:java " &

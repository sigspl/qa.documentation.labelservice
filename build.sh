DATE=$(date +"%Y%m%d")
ORG="iais.nm"
PRODUCT="org.fiware.qa.measurements"

TAG="$ORG/$PRODUCT:$DATE"
TAG_LATEST="$ORG/$PRODUCT:latest"
#PORT_MAPPING="-p 4711:8080"
PORT_MAPPING=" "

echo "building image: $TAG"
docker build -f Dockerfile.ubuntu16 -t $TAG .
echo "tagging image: $TAG_LATEST"
docker tag $TAG $TAG_LATEST

docker rm -f $PRODUCT || true

docker run -d --name=$PRODUCT $TAG tail -f /dev/null




FROM jeanblanchard/java:8

MAINTAINER Vibbo Dev Team <vibbo.dev@scmspain.com>

COPY ./build/libs/cache-jsr107-with-redis-1.0-SNAPSHOT.jar /app/api.jar
WORKDIR /app

EXPOSE 8000

CMD java -jar -Dspring.profiles.active=$ENVIRONMENT api.jar

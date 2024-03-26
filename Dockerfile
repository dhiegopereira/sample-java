FROM ubuntu:latest

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk maven && \
    apt-get clean

WORKDIR /app

COPY ./app /app

EXPOSE 3002

CMD ["mvn", "spring-boot:run"]
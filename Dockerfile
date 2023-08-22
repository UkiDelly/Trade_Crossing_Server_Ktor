FROM openjdk:17-slim
RUN mkdir /app
COPY ./build/libs/trade_crossing_server-all.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
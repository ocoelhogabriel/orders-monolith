FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/orders-monolith-1.0.0.jar app.jar
ENV JAVA_OPTS="-Xms256m -Xmx512m"
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
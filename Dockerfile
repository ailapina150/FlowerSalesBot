FROM openjdk:16.0-slim
WORKDIR /app
COPY target/classes/tulip ./tulip
COPY target/classes/peony ./peony
COPY target/classes/instruction ./instruction
COPY target/saleChatBot-1.0-SNAPSHOT.jar ./bot.jar
CMD ["java", "-jar", "bot.jar"]

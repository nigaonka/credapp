FROM docker.tivo.com/openjdk-docker:11
CMD [ "java", "-jar", "/home/tivo/creditapp.jar" ]
ENV SERVICE_NAME=creditapp \
    PATH=/home/tivo/bin:$PATH
COPY build/libs/creditapp.jar /home/tivo/
COPY build/buildinfo /TivoData/etc/buildinfo

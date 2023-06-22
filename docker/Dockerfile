FROM adoptopenjdk/openjdk11:jdk-11.0.8_10-ubuntu
USER root
RUN apt-get update -y && apt install -y openjdk-11-jre ca-certificates
RUN mkdir -p /opt/chaos-monkey-pod
COPY chaos-monkey-pod-1.0.0.jar /opt/chaos-monkey-pod
COPY start.sh /opt/chaos-monkey-pod
CMD ["/bin/bash", "/opt/chaos-monkey-pod/start.sh"]

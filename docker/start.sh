cp /var/run/secrets/kubernetes.io/serviceaccount/ca.crt /usr/local/share/ca-certificates
update-ca-certificates
java -jar -Djavax.net.ssl.trustStore=/etc/ssl/certs/java/cacerts /opt/chaos-monkey-pod/chaos-monkey-pod-1.0.0.jar

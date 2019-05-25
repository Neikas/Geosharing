# Geo Location Server
TODO:

## Setup
As server relies on the `protocolserve` library it is required to build it at first.
Goto `protocolserde` project directory and execute
```console
mvn clean install
```

After the previous step is finished, simply build the project by executing
```console
mvn clean install
```

## Configuration
It is possible to pass additional configuration to the server application through environment variables.
Please find the list of supported variables below.
| Variable | Description | Default |
| -------- | ----------- | ------------- |
| PORT | Bind server to the specified port | 5432 |
| SSL_CERT | Location of SSL certificate in PEM format | SSL disabled |
| SSL_KEY | Location of SSL ket in PEM format | SSL disabled | 

### Generate self-signed SSL certificate for testing purposes
Please install OpenSSL tools at first. Below guide is for Linux/MacOs only.
- Generate a private key
```console
openssl genpkey -out server.key -algorithm RSA -pkeyopt rsa_keygen_bits:2048
```
- Generate the certificate
```console
openssl req -new -key server.key -out server.csr
```
**NOTE** provide hostname as "Common Name" which next will be put in '/etc/hosts', "geoserver.com" for instance

- Generating a self-signed certificate
```console
openssl x509 -req -days 365 -in server.csr -signkey server.key -out server.crt
```

As a result of these action you will get `server.crt` and `server.key` files.

- Start application with SSL enabled
Goto application folder and execute:
```
mvn exec:java -DSSL_CERT=<path to server.crt> -DSSL_KEY=<path to server.key>
```

As an example of simple SslClient please refer to `src/examples/SslTestClient.java'

## Starting the server
```console
mvn exec:java
```

## Generate JAR with dependencies
```console
mvn clean compile assembly:single
```

## Testing

### Unit tests
The general intention of unit tests is to have an ability to fast verification of functionality of each unit (class in this case). Therefore, the unit under test is executed in isolated environment, e.g. all external dependencies are mocked.

Test execution:
```console
mvn test
```

### Acceptance (e2e) test
Acceptance (or end-to-end tests) verifies use case scenarios against running server. Thus, it is required to have server and related services up and running before executing the e2e tests.
In e2e tests the test cases generates input data and check the output of the system. In case of Geo Location Server the input are requests, and the output is server response and data stored in database.
TODO: configure surface-plugin and maven in order to have 2 commands running unit & e2e test independently.


## Continuous Integration
// TODO:

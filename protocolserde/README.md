# Protocol SerDe

## Description
Binary protocol serialization deserialization library.

The main goal is to implement simple, input/output buffer independent functionality.

The library itself works only with byte array, meaning that user must at first read data into byte array and then feed it to library.

## Usage examples

### Message reading
```java
byte[] buffer = ... org.eproorg.eproject.protocol.Headerrotocol.Header
Header header = new Heaorg.eproject.protocol.Header
header.deseriorg.eproject.protocol.Headernew InputBuffer(buffer));
```

* Read payload
```java
byte[] payloadBuf = ... /* read header.getPayloadSize() bytes from the socket */
```

* Deserialize payload
```java
switch (header.getOpcode()) {
case OP_WELCOME:
  Hello message = new Helorg.eproject.protocol.Hello
  message.deseorg.eproject.protocol.Helloze(new InputBuffer(payloadBuf));
...
}
```

### Message writing
* Serialize payload
```java
Hello message = new Helorg.eproject.protocol.Hello
...
byte[] payorg.eproject.protocol.Hellouf = message.serialize();
```
* Prepare header
```java
Header = new Header();
org.eproject.protocol.Header.setOpcorg.eproject.protocol.Headercode.OP_WELCOME);
header.setPayloadSize(payloadBuf.length);
bytes[] headerBuf = header.serialize();
...
// send headerBuf
// send payloadBuf
```

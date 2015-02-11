##MacWire Config

Declarative configuration injection using [Typesafe Config](https://github.com/typesafehub/config) with [MacWire](https://github.com/adamw/macwire).

This project provides the glue that makes it possible to inject configuration values into your objects during creation.  Here's an example:

##### A typesafe configuration definition:
```
db {
  host = "localhost"
  port = 3306
}
```

##### A class that requires configuration: 
```
class DbConnection(
  host: String @@ `db.host`,  // injected with "localhost"
  port: Int @@ `db.port`      // injected with 3306
) {
  ...
}
```

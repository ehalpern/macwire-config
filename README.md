##MacWire Config

Declarative configuration injection using [Typesafe Config](https://github.com/typesafehub/config) with [MacWire](https://github.com/adamw/macwire).

This project provides the glue that makes it possible to inject configuration values into your objects during creation.  Here's an exmaple:

#### Typesafe configuration definition:
```
# A standard Typesafe config file like resources/application.conf.
db {
  host = "localhost"
  port = 3306
}
```

#### A class that requires configuration: 
```
class DbConnection(
  host: String @@ `db.host`,  // injected with ConfigFactory.load.getString("db.host")  
  port: Int @@ `db.port`      // injected with ConfigFactoyr.load.getInt("db.port")
) {
  ...
}
```

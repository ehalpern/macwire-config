##MacWire Config

Declarative configuration injection using [Typesafe Config](https://github.com/typesafehub/config) with [MacWire](https://github.com/adamw/macwire).

This project provides the glue that makes it possible to inject configuration values into your objects during creation.  Here's an exmaple:

### resources/application.conf
```
db {
  host = "localhost"
  port = 3306
}
```

### scala/my/example/DbConnection
```
class DbConnection(
  host: String @@ `db.host`,  // injected with ConfigFactory.load.getString("db.host")  
  port: Int @@ `db.port`      // injected with ConfigFactoyr.load.getInt("db.port")
) {
  ...
}
```

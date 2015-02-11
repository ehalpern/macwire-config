##MacWire Config

Declarative configuration injection using [Typesafe Config](https://github.com/typesafehub/config) with [MacWire](https://github.com/adamw/macwire).

This project extends the MacWire dependency injection library so that you can automatically inject configuration properties into your objects during creation. Here's an example:

```
db {
  host = "localhost"
  port = 3306
}
```

```
class DbConnection(host: String @@ `db.localhost`, port: Int @@ `db.port`) {
  ...
}
```

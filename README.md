##MacWire Config

Declarative configuration injection using [Typesafe Config](https://github.com/typesafehub/config) with [MacWire](https://github.com/adamw/macwire).

The main benefit of dependency injection (DI) is that it removes initialization logic from (the bulk of) your code. Instead of initializing dependencies in the constructor, you simply declare these dependencies as constructor parameters and leave it to the DI system call the constructor with the right values.  This leaves you with simpler classes that can be easily tested in isolation and more generally reconfigured.

DI examples often focus on object dependencies.  A standard example is replacing a real service with a mock service for unit testing.  But objects are only half of the story.  Classes also depend on configuration values.  If the DI system doesn't inject these, then you're stuck writing code in the class to read and initialize these values.  The result is more boiler-plate code and a dependency on the configuration system wherever configuration values are needed.  


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

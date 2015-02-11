##MacWire Config

Declarative configuration injection using [Typesafe Config](https://github.com/typesafehub/config) with [MacWire](https://github.com/adamw/macwire).

The main benefit of dependency injection (DI) is that it removes initialization logic from your code. Instead of initializing dependencies in the constructor, you simply declare them as parameters and leave it to the DI system to call the constructor with the right values.  This leaves you with simpler classes that can be easily unit tested (and easily reconfigured). 

DI examples often focus on object dependencies.  The standard example replaces a real service with a mock service for unit testing.  Object dependencies, however, are only half of the story.  Classes also depend on configuration constants.  If the DI system doesn't inject these, then you're stuck writing code in the class to read and initialize these values.  The result is more boilerplate and dependencies on the configuration system sprinkled throughout the code.

Why not let the DI system manage configuration values as well?  This libary extends the MacWire DI system to do just that.  It uses the TypeSafe Config library as a starting point for defining and obtaining configuration.  It then provides a scala annotation macro that generates the code required to inject conifuration values by name in a type-safe manner. 

So given a configuration like this:

```
search {
  host = "localhost"
  port = 9200
}
```

You can define a service like this:

```
class SearchService(
  host: String @@ `search.host`,  // injected with "localhost"
  port: Int @@ `search.port`      // injected with 8091
) {
  ...
}
```

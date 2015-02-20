##MacWire Config

Declarative configuration injection using [Typesafe Config](https://github.com/typesafehub/config) with [MacWire](https://github.com/adamw/macwire).

The key benefit of dependency injection (DI) is that it removes initialization logic from your code. Instead of initializing dependencies in the constructor, you simply declare them as parameters and leave it to the DI system to call the constructor with the right values.  This leaves you with simpler classes that can be easily unit tested (and easily reconfigured). 

DI examples often focus on object dependencies.  The standard example replaces a real service with a mock service for unit testing.  Object dependencies, however, are only half of the story.  Classes also depend on configuration constants.  If the DI system doesn't inject these, then you're stuck writing code in the class to read and initialize these values.  The result is more boilerplate and dependencies on the configuration system sprinkled throughout the code.

Why not let the DI system manage configuration values as well?  This libary extends the MacWire DI system to do just that.  It uses the TypeSafe Config library as a starting point for defining and obtaining configuration.  It then provides a scala annotation macro that generates the code required to inject configuration values by name in a type safe manner. 

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
###An  Example
##### Define the configuration
```
search {
  host = "localhost"
  port = 9200
}
```
Define a standard Typesafe configuration (in resources/application.conf for example).  The only additional requirement is that the configuration files must be available at compile time and contain a default value for all properties that are to be injected.

##### Create a Config wiring object
```
import macwire.config.ConfigWiringGenerator

@ConfigWiringGenerator object Config
```
This object will contain the code-generated configuration tags and wiring required to inject configuration property values.

##### Create the main wiring module
```
import com.softwaremill.macwire._

trait MainModule extends Macwire with ConfigWiring.Wiring {
  lazy val searchService = wire[SearchService]
}
```
Add the config wiring by mixing Config.Wiring in with your standard wiring definition

##### Define the service
```
import com.softwaremill.macwire.Tagging._
import Config.Tags._

class SearchService(
  host: String @@ `search.host`,
  port: Int @@ `search.port`
) {
  def search(query: String) = {
    ...
  }
}

```
Inject configuration values into the service by declaring them in the constructor and tagging them with the 
approprate tag (generated in Config.Tags).

#### Behind the scenes

To inject a specific config value, be sure to declare the correct type and append the correct tag.  In the example above

```
  port: Int @@ `search.port`
```
The type of each config property is inferred from the property value as follows:

 Value Example | Scala type 
---------------|------------
"something" \| something | String     
true \| false  | Boolean    
5              | Int        
5.3            | Double     
["foo", "bar"] \| [foo, bar] | Seq[String]
[true, false]  | Seq[Boolean]
[1, 2, 3]      | Seq[Int]
[1.1, 2.2, 3.3] | Seq[Double]

##MacWire Config

Inject [Typesafe Configuration](https://github.com/typesafehub/config) using [MacWire](https://github.com/adamw/macwire) dependency injection.

The key benefit of dependency injection (DI) is that it removes initialization logic from your code. Instead of initializing dependencies in the constructor, you simply declare them as parameters and leave it to the DI system to call the constructor with the right values. This results in more modular code that can be easily reconfigured and unit tested. 

DI discussions often focus on interchangeable component implementations. A standard example is replacing a real implementation with a mock implementation in order to unit test a component. But impelementation injection is only part of the story.  Components often depend on configuration constants as well.  If DI only handles implementation injection, you're stuck sprinkling configuration system dependencies and initialization statements throughout your code.  

Why not let the DI system inject configuration values as well?  This libary extends the MacWire DI system to do just that.  It uses the TypeSafe Config library as a starting point for defining and obtaining configuration.  It then provides a scala annotation macro that generates the code required to inject configuration values by name in a type safe manner. 

So given a configuration definition like

```
search {
  host = "localhost"
  port = 9200
}
```

you can inject the configuration values like so:

```
class SearchService(
  host: String @@ `search.host`,  // injected with "localhost"
  port: Int @@ `search.port`      // injected with 8091
) {
  ...
}
```
###Complete  Example
##### Define the configuration
```
search {
  host = "localhost"
  port = 9200
}
```
Define a standard Typesafe configuration (in `src/main/resources/application.conf` for example).  

##### Add the configuration to the compiler classpath in build.sbt
```
unmanagedClasspath in Compile += sourceDirectory.value / "main" / "resources"
```
At compile time, the code generator reads the Typesafe configuration to determine configuration property names and types. This requires that the configuration file to be on the compiler classpath, which is not the case for `src/main/resources` by default.

##### Create a Config wiring object
```
import macwire.config.ConfigWiringGenerator

@ConfigWiringGenerator object Config
```
The `@ConfigWiringGenerator` annotation will add configuration tags (`Config.Tags`) and wiring (`Config.Wiring`) to the Config object.

##### Create the main wiring module
```
import com.softwaremill.macwire._

trait MainModule extends Macwire with Config.Wiring {
  lazy val searchService = wire[SearchService]
}
```
Mix Config.Wiring into the main wiring definition to make configuration values available for injection.

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
approprate tag (defined in Config.Tags).

### Matching Rules

When declaring a configuration parameter, be sure to specify the correct type and append the correct tag.  Consider the example 

```
import Config.Tags._
  ...
  port: Int @@ `search.port`
```

The tag is the configuration property name surrounded by backquotes, \`search.port\` in this case).  This is actually an empty trait declared in Config.Tags with the fully qualified name Config.Tags.\`search.port\`), so be sure to import Config.Tags._ wherever you refer to a tag name. 

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

### Troubleshooting
#### 

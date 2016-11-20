# Web Crawler


##Goals

This software has the goal to provide a simple Web Site crawler. This crawler at the moment has not configuration to allow to follow external site links (this feature is disabled at the moment).



##Technology

This software is entirely written in Java 1.8 and it's provided of a command line suite to activate the crawling. It's a passive system surfer using multiple threads and collecting the hierarchy in a specific standard output.

The system is provided of a single running interface and a multiple customizable Output formatter and Output device interface. These classes are sibling of two interfaces in order : `ResultFormatter` and `ResultCollector`.

The software has been written with the following minimal design patterns :
  * **Builder** (`WebCrawlerConfigurationBuilder`)
  * **Method Factory** (`WebCrawlerHelper#parseParametersAndRunCrawler`)
  * **Abstract Factory** (`WebCrawler` and `WebCrawlerConfiguration`)
  * **Command** (`WebCrawler`) recursive on itself on the scheduler request.
  * **Bridge** (`ResultFormatter`, `ResultCollector`) 
  * **Observer** (`WebCrawler` in order to `WebCrawlerProcess`) 
  * **Visitor** (`WebCrawlerConfiguration` is a Visitor behalf `WebCrawlerProcess` for the `WebSite` surfing state)

The application is a snapshot release because it doesn't gather the my minimum level of isolation and test harness, due to the first few effort provided.

In the future we will improve this application providing a wider range of configuration attributes, writing multiple formatters and collectors, providing a specific interpreter for the target platforms, define a Spring Boot and Spring Web configuration, isolating the API and providing a persistent layer to operate offline on WebClawler Project (dry-run, re-run, time schedule, remote access for defying and/or run processes behalf deamon agents). Just right now, we detach a Project UID that is transversable to all the application layer, and it can be persisted on a SQL (such as H2, ...), NoSQL (such as MongoDb, ..), File (such as a json file directory, ...) and/or configurable cache systems (such as ehcache, ...) all that in a kind of node environment application server, with specific a configuration, providing a configuration, a clustering, an administration and a maintenance services.

The current command usage is completely dynamic and the current formatters and collectors are recovered by the related implementation package. Implementing new elements automatically are presented to the user. Any attribute has a default value included in a properties file, including the web site URL, so the errors can be related to the human mistakes, only!! :D



##Prerequisites 

**Java 1.8** or upper and **Maven 3.x**. The exported variable **JAVA_HOME** pointing to the Java8 base folder.



##Build 

To build the application you can use the scripts *build.cmd* or *build.sh*.

The Maven 3.x command line to build the project is : 

`mvn -U -up clean cobertura:cobertura javadoc:jar source:jar assembly:single`



##Command Line interface

To test the command line interface there is a **windows command** and a **linux shell executable** shell scripts in the `bin` directory. To have the usage list you need simply use the `--help` option.

Here an example of use:

`Usage: webcrawler [options] [website]`

`samples: webcrowler --format=simple --output=stdout http://www.google.com`

`         webcrowler http://www.facebook.com`

`options:`

`--format		Identifies the site map format`

`     available formats :`

`     plain		Plain Text Site Map Format`

`default : plain`

`--output		Identifies output device`

`     available output :`

`     stdout		Standard Output Writer`

`default : stdout`

`--crawldelay		Identifies the delay in milliseconds before a surf thread starts to prevent the site`

`			security system to intercept the crawling activity and block the access from the IP address`

`default : 1000`

`--extends		Identifies the number of threads extension on the surf. This is the minimum number of threads`

`			running on the site hierarchy discovery`

`default : 15`

`--surflevel		Identifies the meximum number of levels descending from the the website root (0 means unimited)`

`default : 0`

`--skipdupl		Identifies the will to skip parametied calls on the same pages of a parent page`

`default : true`

`website:`

`Web Site to surf within (eg.: http://www.google.com or http://localhost:8085/mysite)`
`default : http://wiprodigital.com`



## Release Version

`1.0.0-SNAPSHOOT`



##Road Map

* *T.B.D.*
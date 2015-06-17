## Weather proxy REST app

Based on _spray-can_1.3_scala-2.11_ template

* gets the weather info from `http://api.openweathermap.org/data/2.5/weather`
* _spray-client_ is used

TODO:
* use case classes (custom structure) to transform the response from weather resource
* cache results (cache futures for results)

1. Launch SBT:

        $ sbt

2. Compile everything and run all tests:

        > test

3. Start the application:

        > re-start

4. Browse to [http://localhost:8080](http://localhost:8080/)

5. Stop the application:

        > re-stop


- Learn more at http://www.spray.io/
- Start hacking on `src/main/scala/com/example/MyService.scala`

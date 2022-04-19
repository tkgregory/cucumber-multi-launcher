An example launcher to run multiple Cucumber executions in the same JVM.

View [this YouTube video](https://youtu.be/oohJSnhwnBk) for a run through of how it works.

**Note that this example is experimental. Please modify to meet your requirements and thoroughy test before using.** 

## Running

Run `./gradlew cucumber`

This executes the class [CucumberLauncher.java](src/test/java/com/tomgregory/cucumber/CucumberLauncher.java)
which is responsible for parsing arguments and starting the Cucumber executions.

## Arguments

See the *cucumber* task definition within [build.gradle](build.gradle) for the list of configurable arguments.
All arguments are passed to each Cucumber execution, except for:

* `--dirx` type arguments (e.g. `--dir0 feature1`) - these form the final argument passed to Cucumber and determine the number of executions

The provided setup runs two Cucumber executions:

1. `--plugin pretty --plugin html:target/cucumber-report.html --glue com.tomgregory.cucumber src/test/resources/features1`
2. `--plugin pretty --plugin html:target/cucumber-report.html --glue com.tomgregory.cucumber src/test/resources/features2`

Use this codebase as an example which you can extend to meet your own requirements.

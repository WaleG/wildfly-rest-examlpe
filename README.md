# User and Project management application

Test application for projects and users management

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

You will need Maven installed, also for running postman tests you will need Newman - command line Collection Runner for Postman.

Newman is built on Node.js. To run Newman, make sure you have Node.js installed.

You can download and install Node.js on Linux, Windows, and Mac OSX.

After you install Node.js, Newman is just a command away. Install Newman from npm globally on your system, which allows you to run it from anywhere.

```
$ npm install -g newman
```

### Installing and running

To get application working follow this steps:

```
clone project
cd project_folder
mvn clean package
java -jar target\wildfly-rest-thorntail.jar
```

brief api documentation is available at http://localhost:8080/api/swagger.json

## Running the tests

To run postman tests make sure that application is running and then execute:

```
newman run src\main\resources\postman\Wildfly-rest.postman_collection.json
```

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Further steps

* Swagger forced to override getClasses() method of Application class to implicitly set resources. I guess, more proper way can be found
* Write some integration tests using Arquillian
* Increase number of unit tests


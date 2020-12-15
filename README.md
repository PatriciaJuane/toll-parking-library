# Toll-parking-library

Download the repository in your local machine and open your favorite IDE.

In your terminal, execute:

-mvn clean install

-mvn spring-boot:run

And you will be able to access the Swagger console in your navigator in this link:

http://localhost:8080/toll-parking-library/swagger-ui/index.html


The endpoints are the following ones:

### GET /toll-parking-library/enterparking/{plateNumber}  --> Get first available parking slot

### POST /toll-parking-library/initialize   --> Initialize toll parking library

### GET /toll-parking-library/leaveparking/{plateNumber}   --> Leave parking slot and return the bill

### PUT /toll-parking-librarypricingpolicy   --> Change pricing policy for toll parking library


That was it! Thanks for reading! :)

# Food-Facility-Search

Restful API that returns a list of Food Trucks in the San Francisco Area. Food Facilities data in the area is sourced from https://data.sfgov.org/Economy-and-Community/Mobile-Food-Facility-Permit/rqzj-sfat/data and given a user's coordinates the application returns a limited amount of Food Trucks within a specified distance.

## Core Features:
- Reading and parsing CSV dataset into FoodTruck Model which is stored in application memory for querying.
- Calculate the distance of each FoodTruck from the user's coordinates and return a list of Food Trucks sorted by shortest distance.
- Ability for User to search for a FoodTruck by name.

### Technology Stack:
- Java 11 Runtime
- SpringBoot
- Gradle

### Starting Application:
To run locally, you must have Java-11 or higher installed. Clone or Download this repository to your local machine and run in any 
 Java IDE or using Gradle :
- `./gradlew clean build`
- `./gradlew bootRun`

Alternatively, you can run this on docker using:
- `./gradlew bootBuildImage --imageName=<my-food-truck-image>`
-  `docker run -p 8080:8080 -t <my-food-truck-image>`

The application runs on embedded tomcat server `port 8080` by default.

### API Documentation:
Visit http://localhost:8080/swagger-ui.html after starting the application.

### Future Enhancements:
- Use a NoSQL database that supports geo-searching eg. Elastic-Search or MongoDB.

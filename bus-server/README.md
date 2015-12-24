## Bus-Server
Bus Information Provider

### Structure
This is only a guard service of bus api. It connects to a bus service and an oauth service with http.

### Dependencies
- Spring MVC
- Spring Boot
- Spring Security

### Gradle
- bootRun : start embedded server with production env

### Packages
- exception  : spring exception handler
- interceptor: spring interceptor
- web        : spring controller
- service    : spring service
- config     : spring java config

### Resources
There are two environments for this project

- test : `application-test.yml`
- production : `application-production.yml`

You have to write your own production config. See `.example` file in resources root path


### API

This api server is protected by api token.

See https://github.com/NCU-CC/API-Documentation for further information
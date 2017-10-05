# servlet-auth

![Build Status](https://travis-ci.org/nickscha/servlet-auth.svg?branch=master)
![Size](https://reposs.herokuapp.com/?path=nickscha/servlet-auth)
![License](https://img.shields.io/hexpm/l/plug.svg)

A simplistic servlet example project for custom programmatic authentication.  

When JASPIC, Container, JAAS, Identity Stores (Keycloak, ...) do not fit with your requirements this can be an alternative. You write your custom authentication code once and you can deploy it to every available container which supports your targeted servlet specification. 
 
Please note: For EE Containers EJB Security won't work. 
 
## Installation
* Download, Build with Maven and deploy it to any container with servlet capabilities
* Open your browser and go to localhost:8080/servlet-auth
* The login page will automatically popup
* Enter test=test as user and password
* MyLoginModule class will be invoked and MyCustomPrincipal will be set
### Launch project with embedded jetty
```
  mvn jetty:run-war
```

### Requirements

* Java 8 or later
* A container with servlet 3.1 support

### Tested with
* Tomcat 8.5.15
* Jetty 9.4.6
* Wildfly 10
* Payara 172
 
 
## Classes and responsibilities

### AuthenticationRequestFilter
This Servlet fiter is invoked for all requests on your webapp and checks if you're logged in and authorized to access an page, resource, image, ... If not you'll be redirected to the login page. 

### AuthenticationRequestWrapper
This request wrapper will override methods such as request::getUserPrincipal, request::isUserInRole(String), request::getRemoteUser() so that you get your custom principal, roles will be assigned whenever you call them on your webapp.

Notice: These methods will also work available in JSF for the FacesContext (e.g. ExternalContext::getUserPrincipal)

### MyLoginServlet (path=/login, action=post)
Authenticates the user and in this sample project creates the MyCustomPrincipal object. Delegate to your custom authentication (LDAP, ...) here.

### MyLogoutServlet (path=/logout, action=post)
Invalidates the Http Session.
 
 
## Further Stuff

### CDI @Inject MyCustomPrincipal
Use the following code (JSF + CDI):

```java
  @Produces
  public MyCustomPrincipal producePrincipal(){
    // getRemoteUser, isCallerInRole will also work as usual
    return FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
  }
```


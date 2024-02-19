Messing around with JWT in Java. 

Created a simple API which can be accesses at "http://localhost:8080/api/v1/auth". There are 2 methods "register and authenticate". 

1. The register method returns a token after successful registration of a user provided a payload of email, password, firstname and lastname is available. If not 403 error status code is returned.
2. The authenticate method checks to see if a user has the permission to access ressources.

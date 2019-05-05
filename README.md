# Spring Boot Security - MySQL and JWT Implementation
This is a simple role based authentication implementation using MySQL and JWT.

Features
-
* User authentication using JWT tokens
* User registration
* User roles
* Secured endpoints
* Password encryption using bcrypt
* Services and controllers tested using JUnit, Mockito and MockMvc

Prerequisites
-
* Java 1.8
* Maven
 
Setup
-
	* Update the application.properties file with your database configuration
	* Update the application.properties file with your jwt secret key and expiration time
	* Execute the following queries into your database:
        * INSERT INTO roles(name) VALUES('ROLE_USER');
        * INSERT INTO roles(name) VALUES('ROLE_ADMIN');

API Endpoints
-
Authenticate user
-
#### Request
```POST /api/auth```

```
curl -H 'Content-Type: application/json' -d '{ "username": "Ermal", "password": "123"}' -X POST http://localhost:8080/api/auth
```
#### Response
```
HTTP/1.1 200 
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sun, 05 May 2019 21:39:12 GMT

{"accessToken":"JWT_TOKEN","tokenType":"Bearer"}
```

Get authenticated user
-
#### Request
`GET /api/auth`
```
curl -i -H "Accept: application/json" -H "Authorization: Bearer JWT_TOKEN" http://localhost:8080/api/auth
```
#### Response
```
HTTP/1.1 200 
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sun, 05 May 2019 21:42:37 GMT

{"id":1,"name":"Ermal","username":"Ermal","email":"ermalferati@gmail.com","roles":["ROLE_USER","ROLE_ADMIN"]}
```

Register user
-
#### Request
```POST /api/users/register```

```
curl -i -H 'Content-type: application/json' -d '{"name": "Ermali", "username": "MoliFerati", "email": "ermalferatiii@gmail.com", "password": "1234567"}' -X POST http://localhost:8080/api/users/register
```
#### Response
```
HTTP/1.1 201 
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sun, 05 May 2019 21:44:10 GMT

{"id":2,"name":"Ermali","username":"MoliFerati","email":"ermalferatiii@gmail.com","roles":["ROLE_USER"]}
```

Create user with custom roles
-
#### Request
```POST /api/users/create```

```
curl -i -H 'Content-type: application/json' -H 'Authorization: Bearer JWT_TOKEN' -d '{ "name": "Ermal", "username": "Ermaliiii", "email": "ermalferatsi@gmail.com", "password": "ErmalFerati", "roles": [ { "id": 1, "name": "ROLE_USER" }, { "id": 2, "name": "ROLE_ADMIN" } ] }' -X POST http://localhost:8080/api/users/create
```
#### Response
```
HTTP/1.1 201 
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sun, 05 May 2019 22:50:40 GMT

{"id":3,"name":"Ermal","username":"Ermaliiii","email":"ermalferatsi@gmail.com","roles":["ROLE_USER","ROLE_ADMIN"]}
```



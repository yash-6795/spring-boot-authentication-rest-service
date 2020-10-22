# Spring Boot based JWT Authentication API. 

To run locally run - `docker-compose up`

---

To check if container up and running run:

`curl http://localhost:3000/api/auth/greeting`

It should return something like this:

`{"timestamp":"2020-10-22T03:09:11.836+00:00","status":403,"error":"Forbidden","message":"","path":"/api/auth/greeting"}%`

---

Clearly, this route is protected and can't be accessed until user is authenticate and authorized.

Let's create a new user:

`
curl --location --request POST 'http://localhost:3000/api/user/signUp' --header 'Content-Type: application/json' --data-raw '{
	"username":"guest@email.com",
	"password":"Pass1234"
}'
 `
 
If user with username doesn't already exist, it will create a new user. Response should look something like this:

`{"message":"Successfully added the user"}%`
 
---
 
Ok, let's go ahead and login:

`curl --location --request POST 'http://localhost:3000/api/user/login' --header 'Content-Type: application/json' --data-raw '{
"username":"guest@email.com",
"password":"Pass1234"
}'`

Once authenticated successfully, response should have JWT token and username. It should look something like this:

`{"jwt":"<token>","username":"guest@email.com"}% `

---
  
Copy token from the response of previoud request and pass as authorization header with every end-point that is protected.

`curl --location --request GET 'localhost:3000/api/user/greeting' --header 'Authorization: Bearer <Token>'`

Response: `{"message":"Greetings :)"}`


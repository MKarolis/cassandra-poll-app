<h1>Cassandra Polling App</h1>
<h6>
Created by Karolis Medekša for non-relational databases course<br/>
Vilnius University, Faculty of Mathematics and Informatics, 2020 Fall
</h6>

<h2>About</h2>
Simple polling WEB service built with `Spring Boot`.

The application uses `Apache Cassandra` and `Spring Data` for storage management.
`jwt` based authentication implemented with `Spring Security`.

JDK version 11 is required to build and run the project
<h2>Setup</h2>

- Pull git repo
- Open with IDE of your choice
- Set the following environment variables:
    - `JWT_SECRET={base64 encoded jwt secret}`
    - `JWT_EXP={jwt token TTL in miliseconds}`
- Start a Cassandra instance on `localhost` port `9042`
- Run the application

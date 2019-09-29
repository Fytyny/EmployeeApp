# EmployeeApp

Application was created using Spring-boot and tested with Spock framework.

To run application all You need to do is

```
mvn package
mvn spring-boot:run
```

Communication with server is done through JSON objects so You will need Content-Type: application/json header for post and put methods.

By default application is using 8081 port.

All avaible enpoints can be found under:
```
http://localhost:8081/v2/api-docs
```

Short endpoints description:

1. Read
```
GET /Employee/{id}
```

Gets employee by id.

2. Create
```
POST /Employee/insert
```

Inserts employee and returns full employee object with id.

For example:

```
curl -X POST -H 'Content-Type: application/json' -i http://localhost:8081/Employee/insert --data '{"name":"John","surname":"Not Sure","grade":1,"salary":2000}'
```

will return:
```
{
  "id": 3,
  "name": "John",
  "grade": 1,
  "surname": "Not Sure",
  "salary": 2000
}
```

3. Update
```
PUT /Employee/update/{id}
```

Updates employee and return confirmation message. Be wary of null fields, because they are not ignored.

For example:

employee
```
{
  "id": 3,
  "name": "John",
  "grade": 1,
  "surname": "Not Sure",
  "salary": 2000
}
```
after
```
curl -X PUT -H 'Content-Type: application/json' -i http://localhost:8081/Employee/update/3 --data '{"name":"Johnny"}'
```
will be
```
{
  "id": 3,
  "name": "Johnny",
  "grade": null,
  "surname": null,
  "salary": null
}
```

4. DELETE
```
DELETE /Employee/delete/{id}
```
Deletes employee. If no employee with that id exists then returns 404.

5.Find
```
POST /Employee/find
```
Returns all employees that matches employee in provided JSON body. Nulls are ignored so You can provide as many arguments as You want.

for example
```
curl -X POST -H 'Content-Type: application/json' -i http://localhost:8081/Employee/find --data '{}'
```

Will return every employee inserted into the database.

and

```
curl -X POST -H 'Content-Type: application/json' -i http://localhost:8081/Employee/find --data '{"name":"Johnny"}'
```
Will return every employee named Johnny.

# Getting Things Done
This repository contains a small REST API, written in Spring Boot.
The resources are related to David Allens [_Getting Things Done_](https://en.wikipedia.org/wiki/Getting_Things_Done) time management method.

## API

### 1. Creating an account

```sh
curl -v -d 'username=john@doe.com&password=d03' \
    http://localhost:8080/public/users/register
```

The outcome is an authentication token.

Alternatively, store the token in an environment variable:

```sh
TOKEN=$(curl -d 'username=john@doe.com&password=d03' http://localhost:8080/public/users/register)
```

### 2. Logging in

```sh
curl -v -d 'username=john@doe.com&password=d03' \
    http://localhost:8080/public/users/login
```

The outcome again is an authentication token.

Alternatively, store the token in an environment variable:

```sh
TOKEN=$(curl -d 'username=john@doe.com&password=d03' http://localhost:8080/public/users/login)
```

### 3. Get information about logged-in user
Pre-conditions

*  the authentication token is stored in an environment variable `TOKEN`

```sh
curl -v -H 'Authorization: Bearer $TOKEN' \
    http://localhost:8080/users/me
```
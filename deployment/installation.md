# Installation Guide

## Application
The application is packaged as a Debian package archive.
Install its prerequisites with `$ apt-get install openjdk-8-jre-headless` or `$ apt-get install openjdk-11-jre-headless`.

## Database
This application requires a relational database for persistent storage.
For development and (integration) testing it uses H2.
For production, PostgreSQL and Oracle are supported.

> Oracle support is currently **not** working due to a (possible) bug in Spring Data JDBC ([DATAJDBC-460](https://jira.spring.io/browse/DATAJDBC-460)).

### PostgreSQL
This assumes the use of an Ubuntu-packaged PostgreSQL database on the same machine as where the application is running.

First, create the user and the database, using

```sh
sudo -u postgres createuser --interactive
sudo -u postgres createdb <database-name>
``` 

This creates a PostgreSQL user that doesn't have a password assigned.
So start PSQL with `sudo -u postgres psql` and issue the following commands:

```sh
alter user <username> WITH PASSWORD '<password>';
```

In the same shell, install a PostgreSQL extension for generating UUIDs:

```sh
create extension if not exists 'uuid-ossp';
```

Configure the application by editing `/etc/gtd-server/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql:gtdapp
    driverClassName: org.postgresql.Driver
    username: <username>
    password: <password>
```

### Oracle
This assumes the use of an [Oracle Autonomous Database](https://www.oracle.com/database/autonomous-database.html) in the Oracle Cloud.
Create an instance (for example using the Terraform scripts in this folder).

Start SQL Developer and issue the following commands:

```sh
grant create session to <username> identified by '<password>';
grant resource to <database-name>;
alter user <username> quota unlimited on data;
```

Download the Credentials Wallet (a ZIP archive) and unpack it in a new folder `/etc/gtd-server/wallet`.

Configure the application by editing `/etc/gtd-server/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@gtddb_Medium?TNS_ADMIN=/etc/gtd-server/wallet
    driverClassName: oracle.jdbc.driver.OracleDriver
    username: <username>
    password: <password>
    hikari:
      schema: <database-name>
```
# SQLWrapper

This sql wrapper uses MySQL Connector/J internally and does not support pooling. It is used for single connection and is intended for simple projects. Larger projects that require many queries should use something else.

To establish a MySQL connection:
```java
MySQLWrapper sql = new MySQLWrapper("hostname", 3306, "database", "username", "password", true).connect();
```
The boolean parameter expects true for persistent connections and false for connections that will automatically close after some time of not being used. If true, it will open a new thread that will ensure that a query ("select 1;") is called every hour (unless another query was run) so that the connection does not close.

Executing a query is pretty simple:
```java
sql.queryBuilder("insert into table (column1, column2) values (?, ?);")
        .withParams(s -> {
            s.setString(1, "Testing");
            s.setInteger(2, 10);
        })
        .update(true) // when running update queries this must be true, but for queries like SELECT it will expect false
        .async(true) // this makes the query get called async
        .query(); // this will return null because it is async
```
There are two different query methods that are available with query builders. The default query() method shown above, and the query(SQLConsumer) method that allows for a consumer that will be called once the query finished. This is called no matter whether it is an async query or sync.

Closing connections is also pretty simple:
```java
sql.disconnect();
```

## Download

Latest version: 1.0-SNAPSHOT

Be sure to replace the **VERSION** key below with one of the versions available.

**Maven**

```xml
<repository>
    <id>swedz</id>
    <name>swedz-repo</name>
    <url>https://swedz.net/repo/</url>
</repository>
```

```xml
<dependency>
    <groupId>net.swedz</groupId>
    <artifactId>SQLWrapper</artifactId>
    <version>VERSION</version>
</dependency>
```

**Gradle**

```groovy
repositories {
    maven { url = 'https://swedz.net/repo/' }
}
```

```groovy
dependencies {
    compile group: 'net.swedz', name: 'SQLWrapper', version: 'VERSION'
}
```
# NYLA Solutions Core

Reusable Java utilities and design-pattern implementations for application configuration,
encryption, I/O, JDBC, expressions, caching, security, and related concerns—without adopting
a full application framework.

[![Java](https://img.shields.io/badge/Java-17%2B-blue)](https://www.java.com/)
[![License](https://img.shields.io/badge/License-Apache%202.0-green)](LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.nyla-solutions/nyla.solutions.core)](https://central.sonatype.com/artifact/com.github.nyla-solutions/nyla.solutions.core)

**Requirements:** Java 17+

**Why use it**

- Broad catalog of patterns and helpers in one dependency
- Minimal runtime footprint for core features (no extra framework required)
- Incremental adoption in existing applications
- Published on [Maven Central](https://central.sonatype.com/artifact/com.github.nyla-solutions/nyla.solutions.core) ([MVN Repository mirror](http://mvnrepository.com/artifact/com.github.nyla-solutions/nyla.solutions.core))

**Related modules in this repository**

| Module | Artifact | Docs |
|--------|----------|------|
| Core | `nyla.solutions.core` | (this file) |
| Email | `nyla.solutions.email` | [projects/nyla-email/README.md](projects/nyla-email/README.md) |
| Office | `nyla.solutions.office` | [projects/nyla-office/README.md](projects/nyla-office/README.md) |

---

## Table of contents

- [Install](#install)
- [Build from source](#build-from-source)
- [Configuration (`Config`)](#config)
- [Encryption (`Cryption`)](#cryption)
- [Debugging (`Debugger`)](#debugger)
- [Text, beans, and reflection](#text)
- [Statistics (`Mathematics`)](#package-nylasolutionscoreutilstats)
- [I/O, CSV, and file watching](#io)
- [JDBC and batch processing](#jdbc)
- [Search (`ReLookup`)](#search-patterns)
- [Faults and templates](#fault-pattern)
- [Generators and JSON](#java-bean-generator-creator)
- [Cache, LDAP, security, graphics](#cache-farm)
- [Expiration and performance](#expirations)
- [Business rules engine](#business-rule-engine-bre)
- [Observer registry](#subject-registry)
- [Publish to Maven Central (FAQ)](#publish-to-maven-central-faq)

---

## Install

### Maven

```xml
<dependency>
    <groupId>com.github.nyla-solutions</groupId>
    <artifactId>nyla.solutions.core</artifactId>
    <version>2.5.3</version>
</dependency>
```

Replace `2.5.3` with the version you need; the latest release is listed on Maven Central.

### Gradle

```groovy
dependencies {
    implementation 'com.github.nyla-solutions:nyla.solutions.core:2.5.3'
}
```

---

## Build from source

From the core module (Gradle 8.x wrapper included):

```bash
cd projects/core
./gradlew build test
```

API Javadoc:

```bash
cd projects/core
./gradlew javadoc
# open projects/core/build/docs/javadoc/index.html
```

---

# API guide

Property access in examples uses `Config.settings()`, which returns the active
[`Settings`](projects/core/src/main/java/nyla/solutions/core/util/settings/Settings.java)
instance (typed getters, passwords, class-prefixed keys, and so on).

# Solutions Core Overview



##	Package: nyla.solutions.core.util.stats

Use the Mathematics object to calculate statistics
such as the 95th percentile, standard deviation,
variance, mean of
set of numbers, etc.


Example Code

```java
Mathematics math = new Mathematics();
    
//Calculate the variance 
assertEquals(700,math.variance(10, 20, 60));
  
//Calculate mean
assertEquals(30,math.mean(10, 20, 60));

//std dev
assertEquals(0.816496580927726,math.stdDev(9, 10, 11));
    
//Percentile (ex 95th)

assertEquals(323,math.percentile(95.0,10,232,232,323,232));
assertEquals(454,math.percentile(95.0,23,75,19,3,5,454,100000,232,23,23,2,32,32,3,2,3,4,34,3,43,43,4,3,43,43,4,3,43,4,34,3,4,3,4343));
assertEquals(98,math.percentile(95.0,23,1,23,2,32,3,2,356,56,5,6,57,6,8,9,8,98,9,8,12,1,2,1,21,21,21));


```

## Package: `nyla.solutions.core.util`

### Config

This class provides a central mechanism for applications to access key/value property settings and encrypted passwords. Developer can get access to environment variable, system Java properties and other property. It also supports type conversion from property strings to numbers, booleans and arrays. There are several ways to specify the configuration properties.

	1.	Add file config.properties to CLASSPATH. This file will be loaded as a Java resource bundle. 
	2.	Add the JVM argument -Dconfig.properties where the value is equal to the location of the configuration file.

	Example: -Dconfig.properties=/dev/configurations/files/system/config.properties

There are methods to get string properties via `Config.settings().getProperty(key)` and typed accessors (`getPropertyInteger`, `getPropertyBoolean`, `getPropertyPassword`, and others on `Settings`).

```properties
nyla.solutions.core.util.Config.mergeSystemProperties=false
```

It also supports formatting several property values into a single property by adding the following property;

```properties
nyla.solutions.core.util.Config.useFormatting=true
``` 


By default the configuration is read only once when the application is initialized. Add the following to the configuration property file to always reload the property whenever a getProperty... method is called. Note that this is a potentially an expensive operation.

 ```properties
nyla.solutions.core.util.Config.alwaysReloadProperties=true
```

Note the following is a property file used for the sample usage code below.

```properties

application.name=JUNIT
debug=true
nyla.solutions.core.util.ConfigTest.integerProperty=24
password={cryption}102 42 -22 24 12 66 -35 89 50 -15 21 9 -67 73 -128 -105
	
nyla.solutions.core.util.Config.mergeSystemProperties=true	
nyla.solutions.core.util.Config.useFormatting=true
application.name.debug=${application.name}.${debug}.${user.dir}
```


*USAGE*

```java
 //Get a default string property
  //The following assumes;
  //application.name=JUNIT
  String property = Config.settings().getProperty("application.name");
  Assert.assertEquals("JUNIT",property);
  
  //An exception will be thrown if the referenced property does not exist in the property file
  //in this case the ConfigException will be thrown
  try
  {
   property = Config.settings().getProperty("missing.property");
  }
  catch(ConfigException e)
  {
   //valid configuration exception
  }
  
  //Provide a default value if the default value is missing
  property = Config.settings().getProperty("missing.property","default");
  Assert.assertEquals("default", property);
  
  //Properties can be retrieved by type (boolean, Integer, Character, Long, Bytes)
  //The following assumes;
  //debug=true
  boolean propertyBoolean = Config.settings().getPropertyBoolean("debug");
  Assert.assertTrue(propertyBoolean);
  
  //Each getProperty<Type> accepts a default value
  //The following assumes;
  //missing.boolean.property=false
   propertyBoolean = Config.settings().getPropertyBoolean("missing.boolean.property",false);
   Assert.assertFalse(propertyBoolean);
   
   //Config has a user friendly way to associate properties with classes
   //The properties can be prefixed with the class name
   //Each getProperty<Type> optional accept the class name as the first argument
   //The following assumes the property 
   //nyla.solutions.core.util.ConfigTest.integerProperty=24
   int integerProperty = Config.settings().getPropertyInteger(nyla.solutions.core.util.ConfigTest.class, "integerProperty");
   Assert.assertEquals(24, integerProperty);
   
   
   //Passwords encrypted with the nyla.solutions.core.util.Cryption object 
   //can be retrieved with the Config.settings().getPassword(key) method
   //An exception will be thrown if the password is not encrypted correctly in the property file
   //The following is example encrypted password stored in the property file
   //password={cryption} 2 -21 23 12 2 -21 23 12 2 -21 23 12 2 -21 23 12 2 -21 23 12
   char[] password = Config.settings().getPropertyPassword("password");
   Assert.assertNotNull(password);
   
   
   //Properties in the System.getProperties() can be merged with the Config's object properties
   //This is done by setting the property
   //nyla.solutions.core.util.Config.mergeSystemProperties=true
   String jvmSystemPropertyName = "user.dir";
   property = Config.settings().getProperty(jvmSystemPropertyName); 
   Assert.assertNotNull(property);
   
   
   //solutions.globa.util.Config.useFormatting property can be used to dynamically combine properties.
   //This feature uses the nyla.solutions.core.patterns.decorator.style  package (see Styles interface)
   //The value of property surrounded with ${property.name} will be formatted by replacing it with the
   //actual value from another property.
   
   //The following is based on the following properties (note this combines the system property "user.dir")
   //nyla.solutions.core.util.Config.useFormatting=true
   //application.name.debug=${application.name}.${debug}.${user.dir}

   property = Config.settings().getProperty("application.name.debug");
   Debugger.println(this,"property="+property);
  
  Assert.assertTrue("All values formatted:"+property, property.indexOf("${") < 0);
```

Supports observer pattern for file configuration changes.

Note that this requires two configuration properties.


Property                             | Example           | Is Required
----------------------------------   | ----------------  | ----------------
CONFIG_FILE_WATCH_POLLING_INTERVAL_MS |1000              | true
CONFIG_FILE_WATCH_DELAY_MS           | 10               | false (default is 5 seconds)



```java
		File config = Paths.get("src/test/resources/config/configTest.properties").toFile();
		
		System.setProperty(Config.SYS_PROPERTY, config.getAbsolutePath());
		
		SubjectObserver<Settings> settingsObserver = new SubjectObserver<Settings>()
		{
			private String name = "test";
			
			@Override
			public String getId()
			{
				return name;
			}
			
			@Override
			public void update(String subjectName, Settings data)
			{
				System.out.println("subjectNAme:"+subjectName+" data:"+data);
				isCalled = true;
			}
		};
		
		Config.registerObserver(settingsObserver);
		IO.touch(config);
		// observer will be called
```

### Cryption

Cryption provides a set of functions to encrypt and decrypt bytes and text. It uses the javax.crypto package.

The default encryption algorithm is the Advanced Encryption Standard (AES).

The default algorithm can be changed with a configuration property named nyla.solutions.core.util.Cryption.alogorithm.

```properties

# The following sets the encryption algorithm to Data Encryption Standard (DES)
nyla.solutions.core.util.Cryption.algorithm=DES
```

The Cryption object is used by nyla.solutions.core.util.Config object to decrypt properties prefixed with {cryption}. The Cryption class can be used to generate encrypted passwords that can be added to the config.properties file. The Cryption main method accepts a password and will print the encrypted password that can be added to the property file.

The printed password will be prefixed with the value “{cryption}”. Any properties prefixed with {cryption} in the config.properties is an indicator that content is encrypted.

The follow is a sample Cryption UNIX script:

```shell script
java -classpath build/libs/nyla.solutions.core-2.0.0-SNAPSHOT.jar -DCRYPTION_KEY=CHANGEMEKEY  nyla.solutions.core.util.Cryption MYPASSWORD
```

The following is a sample output of an encrypted password generated by the Cryption main method.

	{cryption}23 4 -3 -77 -128 -88 -34 -105 23 4 -3 -77 -128 -88 -34 -105

*USAGE*

```java
//The cryption default constructor using the 
//AES algorithm with a default key
Cryption cryption = new Cryption();

//Use the encryptText method to encrypt strings
String original = "Text to encrypt";
String encrypted = cryption.encryptText(original);
Assert.assertTrue(!original.equals(encrypted));

//Use the decryptText method to decrypt strings
String decrypted = cryption.decryptText(encrypted);
Assert.assertEquals(decrypted, original);

//Use encrypt for bytes
byte[] orginalBytes = original.getBytes();
byte[] encryptedBytes = cryption.encrypt(orginalBytes);

//Use decrypt 
byte[] decryptedBytes = cryption.decrypt(encryptedBytes);
Assert.assertTrue(Arrays.equals(orginalBytes, decryptedBytes));


//Create crypt with a specific key and algorithm
byte[] keyBytes = {0x22, 0x15, 0x27, 0x36, 0x41, 0x11, 0x79, 0x76};
Cryption desCryption = new Cryption(keyBytes,"DES"); 

String desEncrypted = desCryption.encryptText(original);
Assert.assertTrue(!original.equals(encrypted) && !desEncrypted.equals(encrypted));
decrypted = desCryption.decryptText(desEncrypted);
Assert.assertEquals(decrypted, original);
```

### Debugger

Debugger provides useful methods for obtaining exception stack traces.  It can build reader friendly strings for objects that do not implement their toString method.

It also provides a set of print functions to log DEBUG, INFO, WARN and FATAL level messages using the Debugger.println(...), Debugger.printInfo(...),Debugger.printWarn(...) and Debugger.printFatal(...) methods respectively.

The default log object implementation is nyla.solutions.core.operations.Log4J.

Set the configuration property to plug-in another logger (@see Config more information);

nyla.solutions.core.util.Debugger.logClass=className

The logClass class name indicated must implement the nyla.solutions.core.operations.Log interface.

*USAGE*

```java
//The Debugger toString(Object) can be used to debug objects where the toString method is not implemented.
String[] arraysNicely = { "first","second"};
String text = Debugger.toString(arraysNicely); 
Assert.assertEquals("{[0]=first ,[1]=second}", text);

//The print method wraps log levels of DEBUG,INFO,WARN and FATAL
Debugger.printInfo("This is a INFO level message");

//Two arguments can be passed where the first is the calling object
//The debugger will prepend the calling objects class name to the logged output
Debugger.println(this, "This is a DEBUG level message");

//Debugger can be used to efficiently print exception information
text = null;
try{
text.toString(); //causes a null pointer exception
}
catch(NullPointerException e)
{
//Use the stackTrace method to get the string version of the exception call stack
String stackTrace = Debugger.stackTrace(e);

//Print warn level
Debugger.printWarn(this,stackTrace);

//stack trace will be automatically created if the exception object is passed
Debugger.printError(e); 

Debugger.printFatal(this,"Stack trace will not be printed, because this is not an exception object.");
}
```


### BeanComparator

BeanComparator is a generic Bean property java.util.Comparator implementation. It compares specified property beans using reflection. This object is internally used by the Organizer.sortByJavaBeanProperty(String,Collection) method.

*USAGE*

```java
//The constructor accepts a JavaBean property name
BeanComparator beanComparator = new BeanComparator("firstName");

//The following are two sample user profile beans
UserProfile josiah = new UserProfile();
josiah.setFirstName("Josiah");
		
UserProfile nyla = new UserProfile();
nyla.setFirstName("Nyla");
		 
//Reflection is used to compare the properties of the beans
Assert.assertTrue(beanComparator.compare(josiah, nyla) < 0);
		 
//The following shows how the BeanComparator.sort method can be used
		 
//This method can be used to sort an given collection based on the JavaBean properties 
//of objects in the collection
ArrayList<UserProfile> unSorted = new ArrayList<UserProfile>();
unSorted.add(0, nyla);
unSorted.add(1, josiah);
		 
//Setting the descending will determine the sort order
beanComparator.setDescending(true);
beanComparator.sort(unSorted);

Assert.assertTrue(unSorted.get(0) == nyla);
		 
//Changing the descending flag changes the output of the sort method
beanComparator.setDescending(false);
beanComparator.sort(unSorted);
Assert.assertTrue(unSorted.get(0) == josiah);
```

### Text

nyla.solutions.core.util.Text is geared toward string based processing. It includes template engine support like Free Marker that builds composite strings/values dynamically at runtime (see http://freemarker.sourceforge.net/). There are also methods to support complex regular expressions with Boolean AND, OR and NOT logic, numerous string conversions, general text manipulation and parsing methods.

Note that the default implementation uses the class

nyla.solutions.core.patterns.decorator.BasicTextStyles

To use Free Marker directory add a configuration properties

```properties
	nyla.solutions.core.util.Text.textStyles=nyla.solutions.global.patterns.decorator.style.FreeMarkerTextStyles
```


*Note:* You must add the freemarker JARs to the classpath to use this implementation.


*USAGE*

```java
//Format text replacing template place-holders prefixed with ${ and suffixed by } 
//with the corresponding values in a map.
String text = "${company} A2D2 Solution Global Application Testings";
Map<String,String> map = new HashMap<String,String>();
map.put("company", "MyCompany");
text = Text.formatText(text,map);
Assert.assertEquals("MyCompany Solution Global Application Testings", text);


//Use complex text matching boolean logic to regular expressions by adding ${AND}, ${NOT} and $OR} tags
Assert.assertTrue(Text.matches("Kenya Africa", ".*Kenya.*"));
Assert.assertFalse(Text.matches("Kenya", "${NOT}.*Kenya.*"));
Assert.assertTrue(Text.matches("Kenya", "${NOT}${NOT}.*Kenya.*"));
Assert.assertFalse(Text.matches("America, Kenya, Paris", ".*Paris.*${AND}.${NOT}.*Kenya.*"));
Assert.assertFalse(Text.matches("America, Kenya, Paris", "(.*Paris.*${AND}.${NOT}.*Kenya.*)${OR}(.*Paris.*${AND}.${NOT}.*Kenya.*)"));
Assert.assertTrue(Text.matches("United States, Kenya, France", "${NOT}.*America.*${AND}.*Kenya.${NOT}.*Paris.*"));
Assert.assertTrue(Text.matches("United States, Kenya, France", "${NOT}.*America.*${AND}.*Kenya.${NOT}.*Paris.*"));

//Use the parse method to retrieve one or more token between a start and end strings
//Note the parse method can be used with non-regular expressions
String start = "Color:";
String end = ";";
Collection collection = Text.parse("Color:green; Weight:155oz; Color:Blue; Weight:23oz", start, end);
Assert.assertEquals(2,collection.size()); //two color
Iterator i  = collection.iterator();
Assert.assertEquals("green", i.next()); //first is green
Assert.assertEquals("Blue", i.next()); //second is Blue

//There methods to count of a given character
int count = Text.characterCount('A', "All Apples");
Assert.assertEquals(2, count);

//There are methods the get digit counts
count = Text.digitCount(text);
Assert.assertEquals(2, count);

//Format text numbers/decimals
String format = "#,###,###.###";
String formattedText = Text.formatNumber(123443243240.033423,format);
Assert.assertEquals("123,443,243,240.033",formattedText);

//Format text currency
formattedText = Text.formatCurrency("1000.33");
Assert.assertEquals("$1,000.33",formattedText);

//format text percentages
formattedText = Text.formatPercent("2.3");
Assert.assertEquals("2.3%",formattedText);

//Use grep to search for expressions across multiple lines in a string
text = "Greg on line 1\nGreen on line two";
String results = Text.grepText("Green", text);
Assert.assertEquals("Green on line two",results);
```


### PROXY

Use the nyla.solutions.core.util.PROXY method to execute methods generically.

```java
		Object response = PROXY.executeMethod(this, "verifyNoArgs", null);
		assertNull(response);
		assertTrue(verifyNoArgsCalled);
		
		response = PROXY.executeMethod(this, "verifyWityArgs", Arrays.asList("test").toArray());
		assertNull(response);
		assertTrue(verifyWityArgs);
		
		response = PROXY.executeMethod(this, "verifyWityArgsWithReturn", Arrays.asList("test").toArray());
		assertEquals("test", response);
		assertTrue(verifyWityArgsWithReturn);
	
	void verifyNoArgs()
	{
		System.out.println("verifyNoArgs");
		verifyNoArgsCalled = true;
	}
	void verifyWityArgs(String text)
	{
		System.out.println("verifyWityArgs:"+text);
		verifyWityArgs = true;
	}
	String verifyWityArgsWithReturn(String text)
	{
		System.out.println("verifyWityArgsWithReturn:"+text);
		verifyWityArgsWithReturn = true;
		return text;
	}
```

##	Package: nyla.solutions.core.util.stats

Use the Mathematics object to calculate statistics
such as the 95th percentile, standard deviation,
variance, mean of
set of numbers.


Example Code

```java
Mathematics math = new Mathematics();
    
//Calculate the variance 
assertEquals(700,subject.variance(10, 20, 60));
  
//Calculate mean
assertEquals(30,subject.mean(10, 20, 60));

//std dev
assertEquals(0.816496580927726,subject.stdDev(9, 10, 11));
    
//Percentile (ex 95th)

assertEquals(323,math.percentile(95.0,10,232,232,323,232));
assertEquals(454,math.percentile(95.0,23,75,19,3,5,454,100000,232,23,23,2,32,32,3,2,3,4,34,3,43,43,4,3,43,43,4,3,43,4,34,3,4,3,4343));
assertEquals(98,math.percentile(95.0,23,1,23,2,32,3,2,356,56,5,6,57,6,8,9,8,98,9,8,12,1,2,1,21,21,21));


```

# IO

See package nyla.solutions.core.io


## Grep


A flexible way to search content in files/directorys.


```java
  Grep actual = Grep.file(file).searchToFile(line ->
                line.contains("2025/05/11"),newFile)
                .searchToFile( line -> line.contains("sever"),finalFile);
```
## File Watcher

See [FileWatcher.java](projects/core/src/main/java/nyla/solutions/core/io/watcher/FileWatcher.java)

```java

AtomicBoolean  fileWasDetected = new AtomicBoolean(false);

         var watcher = FileWatcher.builder()
                 .path(Paths.get("runtime/fileWatcher"))
                 .wildcard(".txt)
                 .observer(file -> {
                     fileWasDetected.set(true);
                 })
                 .build();

       // Wait indefinitely for a file event
         watcher.watchForFile();

         // Or wait for a file event with a timeout
         watcher.watchForFileWithDuration(10, TimeUnit.SECONDS);
```

## FileMonitor

You can use the [nyla.solutions.core.io.FileMonitor](https://github.com/nyla-solutions/nyla/blob/main/src/main/java/nyla/solutions/core/io/FileMonitor.java) observer pattern to
notify added [SubjectObserver](https://github.com/nyla-solutions/nyla/blob/main/src/main/java/nyla/solutions/core/patterns/observer/SubjectObserver.java)(s) of new or update files in given directory that match a file  name patterns.

```java

var subject = new FileMonitor(pollingIntervalMs, delayMs);

var expectedResults  = new ArrayList<Boolean>();

//The observers add a TRUE value to expectedResults when file monitored
SubjectObserver<FileEvent> observer = (observable, arg) -> {

	System.out.println(" observable:"+observable+" arg:"+arg);
	expectedResults.add(Boolean.TRUE);
};

subject.add(observer);

var processCurrentFiles = false; //Process current files
subject.monitor("runtime", "*.txt", processCurrentFiles);

sleep(1000*2);

//Initially the results are empty
assertTrue(expectedResults.isEmpty());

//Update file
IO.touch(Paths.get("runtime/FileMonitor.txt").toFile());
sleep(1000*2);

//Observer should have executed to add expected result
assertFalse(expectedResults.isEmpty());

```

# Patterns

## JDBC

See **nyla.solutions.core.patterns.jdbc**.


Getting a connection

```java

        String driver = Config.settings().getProperty("test.sql.driver","org.h2.Driver");
        String connectionURL = Config.settings().getProperty("test.sql.connectionURL");
        String user = Config.settings().getProperty("test.sql.user");
        char[] password = Config.settings().getPropertyPassword("test.sql.password");
        Connection connection = Sql.createConnection(driver,connectionURL,user,password);
        

```

Query with results as a Java Map

```java
 ResultSetToMapConverter converter = ...;
 Map<String,?> actual = new Sql().queryForMap(connection,converter,sql);
```

Query with results as a Java Map using the default converter.

```java
Map<String,?> actual = new Sql().queryForMap(connection,sql);
```

Execute a statement

```java
new Sql().execute(connection,"insert into table values(1,2)");
```

query For a single Column value

```java
int actualCount = new Sql().queryForColumn(connection,"select 3 from dual",1, Integer.class);
```

## Batch Patterns

### BatchJob

See **nyla.solutions.core.patterns.batch**.

The **BatchJob** class handles reading, processing and writing records in a batch fashion.
The readings, procesors and writers are based on
java.util.function.Supplier, java.util.function.Function and java.util.function.Consumer.
This allows this framework to be used with simple
Lamba expressions.


Sample usage.

```java

  BatchJob batchJob = BatchJob.builder().supplier(supplier)
                                         .consumer(consumer)
                                         .batchChunkSize(batchChunkSize).processor(
                             processor).build();
 
            BatchReport batchRecord = batchJob.execute()

```

### JdbcBatch

See package **nyla.solutions.core.patterns.jdbc.batch**


```java

String sql = "select * from test";
PreparedStatementMapConsumer preparedStatementMapConsumer = new PreparedStatementMapConsumer(bindVariableInterpreter,preparedStatementCreator);
                                                                    Map<String, Object> map = new HashMap<>(); 

SelectResultSetConverterSupplier resultSetSupplier = new SelectResultSetConverterSupplier(
          () -> Sql.createConnection(driver, 
                connectionURL, 
                user, password),
          new ResultSetToMapConverter(), 
          sql);

        JdbcBatch jdbcBatch = new JdbcBatch(resultSetSupplier,
                new ResultSetToMapConverter(),
                preparedStatementMapConsumer,
                batchChunkSize);
```

### SQL Execute Update With Java Bean


Example Update
```java

        UserProfile expectedData = JavaBeanGeneratorCreator.of(UserProfile.class).create();
        String sql = "insert into table_name(email,firstName) values (:email,:firstName)";

        int actualCount = new Sql().executeUpdateSqlWithJavaBean(connection,sql,expectedData);
        assertEquals(1,actualCount);
        verify(preparedStatement).executeUpdate();
```


## Search Patterns

See package nyla.solutions.core.patterns.search

### ReLookup

ReLookup is a map that supports searching for values using a complex regular expression syntax. The key is a regular expression. This operation is similar to the lookup operation. The RELookup will iterate through the given expressions looking for a match on the corresponding source attribute. The value  of the lookup is used if the regular expression matches the source attribute value.


* Complex Regular Expression (And/Not) *
  By default, regular expressions do not have an easy way to chain expressions together using AND/NOT logic. The OR logical expression is supported with the character “|”. The RELookup operation combines regular expressions with a special syntax to support AND/NOT logic.

#### AND Operation

The RELookup supports chaining expressions together with “AND” logic. This is accomplished by chaining expressions together with “${AND}”. The string “${AND}” can be used to separate two regular expressions. If any of the regular expressions return false then the entire regular expression is false. In the following example, the regular expression “.*USA.*${AND}.*Greece.*”, only returns true if the text contains both “USA” and “Greece”.

| Complex RE 				|	Value 			|	Matches |
  ---------   			    |	-----   		|    -----
| .*USA.*${AND}.*Greece.*	|  USA and Greece | True		|
| .*USA.*${AND}.*Greece.*	| USA	          | False		|
| .*USA.*${AND}.*Greece.* 	|	Greece	      | False		|
| .*USA.*${AND}.*Greece.* 	|	Greece USA    | True		|

#### 	NOT Operation
The RELookup supports negative logic (NOT) for expressions. This is accomplished by prefixing the expressions with “${NOT}”. In the following example, the regular expression “.*USA.*” only returns true if the text does not contain the word “USA”. Note that multiple “${NOT}”(s) can be chained together with “${AND}”(s) (see table below).

| Complex RE 					|	Value 			|	Matches | 
| -----------					|	-------			| ---------- | 
| ${NOT}.*USA.*					| 	USA and Greece 	|	False | 
| ${NOT}.*USA.*					| USA				|	False | 
| ${NOT}.*USA.*					| Greece 			|	True  | 
| ${NOT}.*USA.*					| Greece USA 		| 	False | 
| .*Greece.*${AND}${NOT}.*USA.* ${AND}${NOT}.*Turkey.* 	|	Greece Turkey	| False | 
| .*Greece.*${AND}${NOT}.*USA.* ${AND}${NOT}.*Turkey.*	| Greece Africa		| True	| 


*USAGE*

```java
	ReLookup<FaultError> lookup = new ReLookup<FaultError>();

	assertTrue(lookup instanceof Map);
		
	lookup.put("(001)*.*Green.*${AND}${NOT}.*Blue.*", new FaultError("0001","ERROR"));
	lookup.put("(002)*.*Green.*${AND}.*Blue.*", new FaultError("0002","GB"));
	lookup.put("(003)*.*Blue.*", new FaultError("0003","BLUE"));
		
	assertEquals(lookup.get("Green").getCode(), "0001");
	assertEquals(lookup.get("Blue Green").getCode(), "0002");
	assertEquals(lookup.get("Blue with Live of pure").getCode(), "0003");
```

# Fault Pattern

See package nyla.solutions.core.exception.fault

Use the FaultsHtmlTextDecorator to get HTML summary of the FaultException or just objects the implement the nyla.solutions.core.exception.fault.Fault interface.


```java
		Collection<Fault> faults = new ArrayList<Fault>();
		faults.add(faultException);
		FaultsHtmlTextDecorator decorator = new FaultsHtmlTextDecorator(faults);

		String faultsSummaryHtml = decorator.getText();
```

You can override the HTML template by putting the following files first in the classpath:

	templates/FaultsHtmlTextDecorator_ROW.txt
	templates/FaultsHtmlTextDecorator.txt

You can also override the default template classpath locator from "template" by setting the following config property.

```properties
 nyla.solutions.core.util.Text.TEMPLATE_CLASSPATH_ROOT=myclassPathRoot
```	

Example

```properties
	nyla.solutions.core.util.Text.TEMPLATE_CLASSPATH_ROOT=/com/company/templates
```

Example HTML output:

```html
	<html>	<body>
			<div>
				<table>
					<tr>
						<thead>
							<td>code</td>
							<td>message</td>
							<td>category</td>
							<td>module</td>
							<td>errorStackTrace</td>
						</thead>
					</tr>
					<tr>	<td>001</td>
		<td>test_message</td>
		<td></td>
		<td></td>
		<td>nyla.solutions.core.exception.fault.FaultException: test_message
		at nyla.solutions.core.exception.fault.FaultsHtmlDecoratorTest.testGetText(FaultsHtmlDecoratorTest.java:34)
		at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
		at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
		at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
		at java.lang.reflect.Method.invoke(Method.java:498)
		at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
		at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
		at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
		at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
		at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
		at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)
		at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)
		at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
		at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
		at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
		at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
		at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
		at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
		at org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference.run(JUnit4TestReference.java:86)
		at org.eclipse.jdt.internal.junit.runner.TestExecution.run(TestExecution.java:38)
		at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:459)
		at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:678)
		at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner.java:382)
		at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:192)
	</td>
	</tr>
	
				</table>	
			</div>
		</body>
	</html>
```
## MappedTextFormatDecorator Pattern

The class nyla.solutions.core.patterns.decorator.MappedTextFormatDecorator

can be used to generator dynamic based
on template
text based on the Textable/TextDecorator


```java
MappedTextFormatDecorator subject = .new MappedTextFormatDecorator();
String url = "http://localhost:8080/template/MappedTextFormatDecoratorTest.txt";
Map<String, Textable> map = new HashMap<>();
map.put("fn", new StringText(new FirstNameCreator().create()));
map.put("ln", new StringText(new LastNameCreator().create()));

subject.setTemplateUrl(url);
String text = subject.getText();
```
The following is an example template
at a URL:

    Hello ${fn} ${ln}

You also set the template without using a
URL.

```java
subject.setTemplate("I love ${fn} ${ln}");

```

# Java Bean Generator Creator

See package nyla.solutions.core.patterns.creational.generator

JavaBeanGeneratorCreator can be used to auto generate properties for any object.


The following will auto generate random data for the "email" property.

```java
		JavaBeanGeneratorCreator<UserProfile> creator = new JavaBeanGeneratorCreator<UserProfile>(UserProfile.class);
		creator.randomizeProperty("email");
					
		UserProfile userProfile = creator.create();
		assertNotNull(userProfile);
					
		assertTrue(userProfile.getEmail() != null && userProfile.getEmail().length() > 0 );
```

The following auto generate all supported properties in the provided object.

```java
		JavaBeanGeneratorCreator<UserProfile> creator = new JavaBeanGeneratorCreator<UserProfile>(UserProfile.class);
				creator.randomizeAll();
		
		JavaBeanGeneratorCreator<UserProfile> creator = new JavaBeanGeneratorCreator<UserProfile>(UserProfile.class);
		creator.randomizeAll();
		
		assertTrue(creator.getRandomizeProperties().contains("firstName"));
		
		assertTrue(creator.create().getEmail().length() > 0);
```


The following uses a proxy to generate new objects. The proxy is copied with only the indicated properties auto generated with each create method.


```java
		UserProfile protoype = new UserProfile();
				protoype.setFirstName("Imani");
				protoype.setLastName("Green");
		
		JavaBeanGeneratorCreator<UserProfile> creator = new JavaBeanGeneratorCreator<UserProfile>(protoype);
		
		creator.randomizeProperty("email");
		
		UserProfile u1 = creator.create();
		assertEquals(u1.getFirstName(),protoype.getFirstName());
		assertEquals(u1.getLastName(),protoype.getLastName());
		assertNotNull(u1.getEmail());
		assertTrue(u1.getEmail().length() > 0);
```

If you have a fairly complex object with multiple nested objects,
you can use the generateNestedAll in addition to the randomizeAll to
create random data for nested objects.

```java
    JavaBeanGeneratorCreator<ComplexObject> creator = new
                    JavaBeanGeneratorCreator<ComplexObject>(ComplexObject.class)
                    .randomizeAll()
                    .generateNestedAll();

		ComplexObject complexObject = creator.create();
```


JavaBeanGenerator will also generator reasonably valid
values for fields such as "firstName", "lastName",
"phone", "mobile", "fax" and "email" based on the
provided objects properties names.

For example, the following code

```java
	UserProfile userProfile  = new JavaBeanGeneratorCreator<UserProfile>(UserProfile.class)
						.randomizeAll().create();
				System.out.println(userProfile);
```

Results in the following output. Notice,
the random generated values for first, last name,
email and phone.


```
nyla.solutions.core.security.user.
data.UserProfile[email=wslom@dutvs.ubvu,
loginID=467230864,
firstName=Walter,lastName=Neal,
title=20200524224944759144752447,
phone=555-555-5708,
...
```


The following is a more complex customization. The sample generate credit card
transaction data.


```java
new JavaBeanGeneratorCreator(Transaction.class)
                .generateNestedAll()
                .randomizeAll()
                .creatorForProperty("name_on_card", new FullNameCreator())
                .creatorForProperty("expiry_year",  () -> digits.generateShort(minYear,maxYear))
                .creatorForProperty("expiry_month",  () -> digits.generateShort(minMonth,maxMonth))
                .creatorForProperty("brand",  PickRandomTextCreator.options("Amex",
                        "Discover","Wells Fargo","Citigroup","Chase","Barclays",
                        "MasterCard","PNC Bank","Visa","Navy Federal Credit Union"))
                .creatorForProperty("card_type",  PickRandomTextCreator.options("Amex",
                        "Rewards","Cashback","Travel","Business","Corporate"))
                .creatorForProperty("description",PickRandomTextCreator.options("issued by a financial company",
                        " minimum payment each period","Summary of account activity","basic terms that you may come across when using your credit card."));
```

# JSON Generation

The class JsonGeneratorCreator is creational
implementation to generator example JSON
output similar to the JavaBeanGenerator.


See nyla.solutions.core.patterns.creational.generator.json.JsonGeneratorCreator

It will also generator reasonably valid
values for fields such as "firstName", "lastName",
"phone", "mobile", "fax" and "email" based on the
provided type properties names.

The supported data types are base on the JsonPropertyType
object (ex: String, Integer, Number and Boolean).

Example Usage using the Properties object.

```java

  
        
         Properties properties = new Properties();
         properties.setProperty("firstName","String");
         properties.setProperty("lastName","String");
         properties.setProperty("updateDate","String");
         properties.setProperty("cost","Number");
         properties.setProperty("count","Integer");

         JsonGeneratorCreator c =
                 new JsonGeneratorCreator(DateTimeFormatter.ISO_DATE,
                         properties);

         String json = c.create();
```

Example JSON output

```json

  {"firstName":"Keith","lastName":"Jackson","updateDate":"2020-07-04","cost":0.011549557733887772,"count":119629238}
   
```


You can also use a JsonSchemaBluePrint
object to create the JsonGeneratorCreator.

```java
        sJsonSchemaBluePrint jsonSchemaBluePrint = new JsonSchemaBluePrint(DateTimeFormatter.ISO_DATE);

        jsonSchemaBluePrint.addPropertySchema(new JsonPropertySchema("isTrue", JsonPropertyType.Boolean));
        jsonSchemaBluePrint.addPropertySchema(new JsonPropertySchema("cost", JsonPropertyType.Number));
        jsonSchemaBluePrint.addPropertySchema(new JsonPropertySchema("count", JsonPropertyType.Integer));
        jsonSchemaBluePrint.addPropertySchema(new JsonPropertySchema("name", JsonPropertyType.String));
        jsonSchemaBluePrint.addPropertySchema(new JsonPropertySchema("date", JsonPropertyType.String));
        jsonSchemaBluePrint.addPropertySchema(new JsonPropertySchema("firstName", JsonPropertyType.String));
        jsonSchemaBluePrint.addPropertySchema(new JsonPropertySchema("lastName", JsonPropertyType.String));

        JsonGeneratorCreator c =
                new JsonGeneratorCreator(jsonSchemaBluePrint);

        String json = c.create();
```


# Cache Farm

See package nyla.solutions.core.patterns.cache

Cache Farm is a simple singleton implementation of cached key/value pairs.


```java
	Cache<Object,Object> cacheFarm = CacheFarm.getCache();
	cacheFarm.put("key",myObject);
```

# CSV

See package `nyla.solutions.core.io.csv`.

The CSV utilities provide lightweight parsing and query-style operations for CSV data.

## CSV Reader

`CsvReader` can read CSV content from a `File` or `Reader`, while the builder APIs
provide additional options for configuring how CSV data is processed.

### Reading CSV Data

A `CsvReader` can be created from a `File` or a `Reader`.

```java
CsvReader reader = new CsvReader(file);
```

or:
```java
String csv = """
    sku,description
    sku1,Peanut butter
    sku2,Jelly
    sku3,Bread
    """;

CsvReader reader = new CsvReader(new StringReader(csv));

```

Note that is version of the reader reads all lines into memory.
See CsvStreamingReader.java for very large datasets. 

The number of rows can be obtained with size():

```java
assertEquals(3, reader.size());
```

Individual rows can be accessed with row(int):

```java

List<String> row = reader.row(0);

assertEquals("sku1", row.get(0));
```

The complete reader data is available through getData():

```java

List<List<String>> data = reader.getData();

assertEquals(3, data.size());
```

*Headers*

Use the CsvReader builder when the first row contains column headers that should
not be included in the data rows.

```java
String csv = """
sku,description
"sku1", "Peanut butter"
"sku2", "Jelly"
"sku3", "Bread"
"sku4", "Milk"
""".trim();

CsvReader reader = CsvReader.builder()
.reader(new StringReader(csv))
.skipHeader(true)
.build();

assertTrue(reader.isSkipHeader());

assertEquals(4, reader.size());
assertEquals("sku1", reader.get(0, 0, DataType.String));
assertEquals("Peanut butter", reader.get(0, 1, DataType.String));

```

When skipHeader(true) is enabled, the first CSV row is treated as a header and
is excluded from the reader's data rows.

Typed Column Access

Values can be retrieved by row and column index using a DataType.

```java
String sku = reader.get(0, 0, DataType.String);
String description = reader.get(0, 1, DataType.String);
```

Column indexes are zero-based.

For example:

```text
sku1,Peanut butter
↑          ↑
0          1
```

The DataType argument allows the reader to interpret the column using the requested
data type.


## CSV Parsing

`CsvReader.parse(String)` parses a single CSV record into a `List<String>`.

The parser supports:

- Comma-separated values
- Quoted values containing commas
- Quoted values containing escaped quotes
- Mixed quoted and unquoted values

### Examples

```java
List<String> results = CsvReader.parse("1,2");

assertNotNull(results);
assertEquals("1", results.get(0));
assertEquals("2", results.get(1));
```

Values containing commas can be enclosed in double quotes:

```java
results = CsvReader.parse("\"1,2\"");

assertEquals(1, results.size());
assertEquals("1,2", results.get(0));
```

Quoted and unquoted values can be mixed:

```java
results = CsvReader.parse("0,\"1,2\"");

assertEquals("0", results.get(0));
assertEquals("1,2", results.get(1));
```

Single quotes are treated as normal characters:

```java

var results = CsvReader.parse("0,\"1,2\",\"Greg's\"");

assertEquals("0", results.get(0));
assertEquals("1,2", results.get(1));
assertEquals("Greg's", results.get(2));
```


The parser also handles escaped double quotes using the standard CSV "" representation:

```java

var results = CsvReader.parse("\"0\",\"The \"\"GOOD\"\"\",2");

assertEquals("0", results.get(0));
assertEquals("The \"GOOD\"", results.get(1));
assertEquals("2", results.get(2));
```

## CSV Select Builder


CsvReader.selectBuilder() provides a fluent API for filtering, sorting, grouping, and generating CSV text.

This is useful when CSV data needs simple query-style processing without first mapping every row to a Java object.

Filtering with where

The where operation accepts a Predicate<List<String>>. The predicate receives each CSV row as a list of column values.

For example, the following selects rows where:

- column 0 is either ID000 or ID001; and
- column 1 is either Nyla or Imani.

```java
List<String> actual = csvReader.selectBuilder()
        .where(line ->
                (line.get(0).equals("ID000") || line.get(0).equals("ID001"))
                        && (line.get(1).equals("Nyla") || line.get(1).equals("Imani")))
        .orderBy(2)
        .groupBy(0)
        .buildCsvText();
```

Selecting, ordering, and grouping

The builder can combine multiple operations into a single fluent expression:


```java
List<String> actual = csvReader.selectBuilder()
        .where(line ->
                (line.get(0).equals("ID000") || line.get(0).equals("ID001"))
                        && (line.get(1).equals("Nyla") || line.get(1).equals("Imani")))
        .orderBy(2)
        .groupBy(0)
        .buildCsvText();
```

The operations are applied as part of the builder pipeline:


| Operation	     | Purpose                              |
|----------------|--------------------------------------|
| where(...)	    | Filters rows using a Java Predicate  |
|                |                                      |
| orderBy(...)	  | Orders the selected rows by a column |
| groupBy(...)	  | Groups rows using a column           |
| buildCsvText() | 	Builds the resulting CSV output     |


Column indexes are zero-based. For example:

```text
ID000,Nyla,100
  ↑     ↑    ↑
  0     1    2
```

Therefore:

```java
line.get(0)   // ID000
line.get(1)   // Nyla
line.get(2)   // 100
```

Example

Given CSV data such as:

```text
ID000,Nyla,30
ID001,Imani,10
ID002,Nyla,20
ID001,Nyla,40
```

A query can filter the rows and then apply ordering and grouping:

```java
String actual = csvReader.selectBuilder()
.where(line ->
(line.get(0).equals("ID000") || line.get(0).equals("ID001"))
&& (line.get(1).equals("Nyla") || line.get(1).equals("Imani")))
.orderBy(2)
.groupBy(0)
.buildCsvText();
```


## CsvStreamingReader

`CsvStreamingReader` provides an iterator-based way to read CSV records sequentially.

It is useful when CSV data should be processed one row at a time rather than loaded
into a collection before processing.

Create a streaming reader with `CsvStreamingReader.Builder`:

```java
CsvStreamingReader reader =
        new CsvStreamingReader.Builder()
                .reader(new StringReader(csv))
                .build();

The reader can be used directly with Java's enhanced for loop because it implements
Iterable<List<String>>.

Iterate over CSV records
String csv = """
        "A", "A record"
        "B", "B record"
        """;

CsvStreamingReader reader =
        new CsvStreamingReader.Builder()
                .reader(new StringReader(csv))
                .build();

for (List<String> row : reader) {
        System.out.println(row);
}
```

Each CSV record is returned as a List<String>.

For example:

        [A, A record]
        [B, B record]

A simple way to count the records is:

```java
int count = 0;

for (List<String> row : reader) {
count++;
        }

assertEquals(2, count);
```

Reading records record


```java
String csv = """
        "A", "A record"
        """;

CsvStreamingReader reader =
        new CsvStreamingReader.Builder()
                .reader(new StringReader(csv))
                .build();

for (List<String> row : reader) {
assertThat(row).containsExactly("A", "A record");
}
```


Skip the header row

Use skipHeader(true) when the first CSV record contains column names rather than
data.

        For example, given:

        "id", "name"
        "A", "A record"
        "B", "B record"

configure the reader as follows:

```java
CsvStreamingReader reader =
        new CsvStreamingReader.Builder()
                .reader(new FileReader(file))
                .skipHeader(true)
                .build();
```


The header:

        "id", "name"

        is skipped, and iteration begins with:

        "A", "A record"
        "B", "B record"

Example:


Builder options

The CsvStreamingReader.Builder is used to configure the streaming reader.

| Option           | Description                                  | 
|------------------|----------------------------------------------|
| reader(Reader)   | Specifies the Reader containing the CSV data |
| skipHeader(true) | 	Skips the first CSV record                  |
| build()          | 	Creates the CsvStreamingReader              |

Example:


```java
CsvStreamingReader reader =
        new CsvStreamingReader.Builder()
                .reader(new FileReader(file))
                .skipHeader(true)
                .build();
```

Processing records sequentially

Because CsvStreamingReader can be used as an Iterable, records can be processed
sequentially without first converting the entire input into a List:

```java
CsvStreamingReader reader =
        new CsvStreamingReader.Builder()
                .reader(new FileReader(file))
                .skipHeader(true)
                .build();

for (List<String> row : reader) {
String id = row.get(0);
String name = row.get(1);

// Process the record
}
```

This makes CsvStreamingReader particularly suitable for processing CSV files where
records should be consumed incrementally.



# LDAP

See package nyla.solutions.core.ds

The object nyla.solutions.core.ds.LDAP provides a simple wrapper for LDAP authentication and searching.


```java
		String adminDN = "uid=admin,ou=system";
		char[] adminPwd  ="secret".toCharArray();
		String user = "imani";
		char[] pwd = "password".toCharArray();
	  
	     try(LDAP ldap = new LDAP("ldap://localhost", adminDN, adminPwd))
		{
			Principal principal = ldap.authenicate(user,pwd,"ou=system","uid","memberOf","CN",100);
			assertEquals(user,principal.getName());
		}
```

**LDAPS** (or LDAP over SSL/TLS support)

Set the following configuration properties in order to enable secure LDAP communication.


|  Property | Notes  |
|---|---|
| LDAP_USE_SSL_CONFIG_FACTORY  |(**true** or **false**)  Boolean value to determine if LDAPS is used with the following configurations properties |
|  "LDAP_SSL_KEYSTORE"         | The SSL KEYSTORE file path location |
|  "LDAP_SSL_TRUSTSTORE"          | The SSL KEYSTORE file path location |
| "LDAP_SSL_KEYSTORE_PASSWORD"    | The password for the key store  |
| "LDAP_SSL_TRUSTSTORE_PASSSWORD" | The password for the trust store |


# Security

See package nyla.solutions.core.security.data

**Access Control List**

```java	
	 Principal caller = null;
	    Principal principal = null;
	    
		SecurityAcl securityAcl = new SecurityAcl();
		
		principal = new SecurityGroup("group");
		caller = new SecurityClient("admin");
		securityAcl.addEntry(caller,principal, false,"CLUSTER");
		assertTrue(securityAcl.checkPermission(principal, "CLUSTER"));
		
		securityAcl.addEntry(caller,principal, true,"CLUSTER");
		assertFalse(securityAcl.checkPermission(principal, "CLUSTER"));	
```

# Graphics

See package nyla.solutions.core.media

**Capture screen shots**

```java
Graphics graphics = new Graphics();
graphics.printScreen(0, 0, 1000, 800, "png", new File("runtime/tmp/output/screenshot.png"));
```

**Rotate images**

Example rotate 45 degrees

```java
Graphics graphics = new Graphics();
graphics.rotateImage(Paths.get("test-in.png").toFile(),Paths.get("test-out.png"),toFile(),Graphics.Format.PNG,45);
```	

# Expirations

See package nyla.solutions.core.data.expiration

**ExpiringKeyValueLookup** 


The ExpiringKeyValueLookup class can be used for simple hash based look ups with expiring content.
This is a good class to use for implementing caching.

*Example Code*

```java
		ExpiringKeyValueLookup<String,String> bag = ExpiringKeyValueLookup.withExpirationMS(1000);
		assertNotNull(bag);
		bag.putEntry("1","1");
		
		assertEquals("1",bag.getValue("1"));
		Thread.sleep(1001);
		
		assertNull(bag.getValue("1"));

```

**ExpiringItem**

The ExpiringItem class can be used for a single value with expiring content.

*Example Code*

```java	
	ExpiringItem<String> e = new ExpiringItem<>("hello",
			LocalDateTime.now().plusSeconds(1));
		
		assertEquals("hello",e.value());
		
		Thread.sleep(1050);
		
		assertNull(e.value());
```


# Performance


##


```java
       var perfTest = new PerformanceCheck(benchMark,statsCapacity);
        try(producer)
        {
            perfTest.perfCheck(()->{
                        producer.send(new ProducerRecord<String, String>("my-topic", key,value));
                    });
        }
```

## nyla.solutions.core.operations.performance.stats.ThroughputStatistics

This calculating throughput statistics

```java
     ThroughputStatistics subject = new ThroughputStatistics();

        long expected = 1000;
        LocalDateTime now = LocalDateTime.now();
        subject.increment(expected);

        assertEquals(Double.valueOf(expected),subject.throughputPerSecond(now,now.plusSeconds(1)));

```


# Generator

## nyla.solutions.core.patterns.creational.generator


**GenerateTextWithPropertiesCreator** can replace common placeholder values with randomly
generated text.


Support placeholders


- {id}
- ${email}
- ${firstName}
- ${lastName}
- ${name}
- ${fullName}
- ${phone}
- ${mobile}
- ${fax}
- ${date}
- ${intRange}

```java
String template = "Hello ${id} ${email} ${firstName} ${lastName} ${name} ${fullName}, "+
"your phone is ${phone}, your cell is ${mobile} your fax is ${fax} "+
"generated on ${date}";

GenerateTextWithPropertiesCreator subject = new GenerateTextWithPropertiesCreator(template);
String actual = subject.getText();

```


# Business Rule Engine (BRE)

## package nyla.solutions.core.patterns.expression.bre

The business rules engine is a software system that executes one or more function rules in at runtime.
The *BusinessRuleEngine* supports chaining Function calls to be executed based on a logical rule name.

```java
  /**
         *
         * The rules for Abnormal Vital Statistics: where "+
         *                     " (statName = 'heartRate' and (value < 55 or value >  105))       "+
         *                     " or                                                                "+
         *                     " (statName = 'bodyTemperature' and (value < 95 or value >  103)) "+
         *                     " or                                                                "+
         *                     " (statName = 'respirationRate' and (value < 12 or value >  16)) "+
         *                     " or                                                                "+
         *                     " (statName = 'bloodPressureDiastolic' and value < 80 )"+
         *                     " or                                                                "+
         *                     " (statName = 'bloodPressureSystolic' and value > 130)
         */
        
        BusinessRuleEngine<Integer,Boolean> abnormalVitalBre = BusinessRuleEngine
                .builder()
                .rule("heartRate",
                        new OrExpression<Integer>(ComparableExpression.lessThan(55),
                                ComparableExpression.greaterThan(105)))
                .rule("bodyTemperature",new OrExpression<Integer>(
                    ComparableExpression.lessThan(95),
                    ComparableExpression.greaterThan(103)))
                .rule( "respirationRate",new OrExpression<Integer>(
                    ComparableExpression.lessThan(12),
                    ComparableExpression.greaterThan(16)))
                .rule("bloodPressureDiastolic", ComparableExpression.lessThan(80))
                .rule("bloodPressureSystolic", ComparableExpression.greaterThan(130))
                .build();

        assertEquals(true,abnormalVitalBre.applyForRule("bodyTemperature",200));

        assertEquals(false,abnormalVitalBre.applyForRule("bodyTemperature",98));
```

# Building

Set your ossrUsername and ossrhPassword in the ~/.gradle

	ossrhUsername=userName
	ossrhPassword=<password>

1. Change directory to the root directory
2. $gradle install


# FAQ

- I get the following /bin/sh: gpg: command not found

    1. Install the gpg https://www.gnupg.org/download
    2. /usr/local/gnupg-2.1/bin/gpg --gen-key
    3. gpg --list-keys
    4. gpg --keyserver hkp://pgp.mit.edu --send-keys gpg --keyserver hkp://pgp.mit.edu --send-keys FB70F1D1
       Also see http://blog.sonatype.com/2010/01/how-to-generate-pgp-signatures-with-maven/#.Vu84SRIrKjQ

Add the following to gradle.properties

	signing.keyId=KEYID
	#ossrh
	signing.password=PASSWORD
	signing.secretKeyRingFile=<HOME>/secring.gpg

# Subject Registry

The nyla.solutions.core.patterns.observer.SubjectRegistry object is an object/mapping of multiple topics and observers.


```java
String subjectName = "hello";

subject = new SubjectRegistry();

subject.register(subjectName,observer);

//Notify registered with subject
subject.notify(subjectName,userProfile);

//Notify All
subject.notifyAll(userProfile);
		
```
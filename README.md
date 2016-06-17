Quark
==

[![Build Status](https://travis-ci.org/PierreBeucher/quark.svg?branch=master)](https://travis-ci.org/PierreBeucher/quark)

Quark is a light-weight integration test framework providing easy-to-use Helpers for various transports and technologies.

The idea behind Quark is to provide facility to write automated test easily and provide user-friendly test output. These principles should allow users to quickly have an overview of what happened and track potential bugs to aim directly the problematic component or piece of code.

-----

 - [Introduction](#introduction)
 - [Features](#features)
 - [Quickstart](#quickstart)
 - [Available Helpers](#available-helpers)
 - [Usage](#usage)
	 - [Basics](#basics)
		 - [Flavours](#flavours)
		 - [Lifecycle](#lifecycle)
		 - [SFTP](#sftp)
		 - [JDBC](#jdbc)
		 - [CMIS](#cmis)
		 - [MantisBT](#mantisbt)
		 - [File](#file)
	 - [Advanced](#advanced)
		 - [Build up infrastructure level Helpers](#build-up-infrastructure-level-helpers)
		 - [Report and log with HelperResult](#report-and-log-with-helperresult)
- [Internal design](#internal-design)
	- [Core](#core)
	- [Lifecycle Management](#lifecycle-management)

- [Developers and maintainers](#developers-and-maintainers)

-----

## Introduction

Quark lets you define the __Context__ under which your test is being run and provide __Helpers__ with various test-oriented functions and methods. Helpers allow you to easily run your test actions under the pre-defined Context, enabling re-usability and automation for your test infrastructure.

![Quark Overview Diagram](https://github.com/PierreBeucher/quark/raw/master/docs/assets/img/intro-overview.jpg)

The _Context_ define the environment in which your system under test (SUT) is running: the system itself (a web service, a server, a database...) and any component gravitating around your SUT (another server, a set of config files, authentication tokens, etc.). Quark provide a few basics Contexts and lets you create your own.

Helpers are components providing test functionalities on top of your context, allowing to easily assert your test result. By configuration one or more Helpers with a given Context, is becomes an efficient and user-friendly test machine! To create a test, nothing simpler than defining your Context and using the appropriate Helper.

## Features
Quark provide the following functionalities:

- __Test automation framework__ - Automate your tests by defining them once and running them whenever you need
- __Provide complete and user-friendly test result__ No more _what the Quark is this assertion error?_, simply find how, why and where your test failed. 
- __Support for various transports and technologies__ - Support for SFTP, FTP, CMIS, JDBC, CMIS, MantisBT, local File, and much more.
- __Integrate with common test frameworks__ - Easy integration with TestNG, JUnit and other widely spread test framework

## Quickstart

A picture speaks better than words. Let's consider a simple application consuming files from SFTP server (server hosting files) and archiving them to a CMIS server (a document management server). A simple Quark test for our application may be implemented as follow:

	@Test
	public void testFileArchive(){

		File testFile = new File("/home/quark/some_test_file.xml");
		
		// 1.
		// create and initialise a Helper using our SFTP context
		SftpHelper sftpHelper = new SftpHelper()
			.host("localhost")
			.port(2222)
			.login("login")
			.password("xxx");
		sftpHelper.initialize();

		// 2.
		// upload some test file
		// this file shall be consumed by our application
		sftpHelper.upload(testFile, "/sftp/myapp/to-archive/");

		// 3.
		// now let's ensure the file has been archived
		// create and initialise a Helper for our CMIS context
		CMISHelper cmisHelper = new CMISHelper()
			.atomPubUrl("http://localhost/atom")
			.login("login")
			.password("pass")
			.repository("myrepo");
		cmisHelper.initialise();
		
		// 4.
		// assert our file has been archived
		Assert.assertTrue(
			cmisHelper.containsDocument("/myapp/archive/" + file.getName()),
			testFile.getName() + " not archived properly."
			);
		
		// 5. 
		// clean-up everything after our test
		sftpHelper.dispose();
		cmisHelper.dispose();
	} 
This example used two Helpers: the SFTP Helper to feed data on our SFTP server, and the CMIS Helper to assert file archive. 
 
 1. We created and initialised a SftpHelper to perform actions on our SFTP server, located on sftp://localhost:2222, using user '*login*' and password '*xxx*'
 2. We uploaded a test file on this SFTP server, file which is supposed to be consumed by our application during the test
 3. We created and initialised a CmisHelper to perform actions on our CMIS server, located on http://localhost using Atom binding. 
 4. Using our CmisHelper, we ensured the file was properly archived
 5. Finally, we cleaned-up any resources used by our Helpers

This example is very simple to demonstrate the use of Quark. Some things may be improved a bit:

 - Ensuring the file has been consumed (checking the file does not exists anymore) or performing more advanced test actions such as verifying the file checksum before/after archive
 - Wrapping our test actions under a try-catch clause, using `dispose()` methods in the finally block.
 - Adding a few logs

## Available Helpers

Helpers are Quark's central components. A Helper is defined for a specific transport or technology and manage a given Context on which test actions will be performed.

| Transport or Technology | Availability | Underlying Client or API |
| ------------- | ------------- | ----- |
| SFTP  | Alpha | [JSch](http://www.jcraft.com/jsch/) |
| FTP | Alpha | [Apache Commons Net][apache-common-net] |
| JDBC | Alpha | [Spring Framework JDBC](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/jdbc.html) |
| CMIS | Alpha | [Apache Chemistry](https://chemistry.apache.org/java/opencmis.html)|
|Mantis Bug Tracker | Alpha | [Mantis Axis Soap Client](https://github.com/rombert/mantis-axis-soap-client) |
| File | Alpha | [Java IO Package](https://docs.oracle.com/javase/7/docs/api/java/io/package-summary.html) |
| JMS | Soon | |
| HTTP/REST | Soon | |
| SSH | Soon | |

## Usage

Basic and Advanced usage for Quark: how to define your Context and create your Helpers, how to use them, and a few advanced mechanisms available.

### Basics

A few usage basics.

#### Flavours

Helpers come in several flavours. For most transports, at least the following Helpers flavours are available:

- __Plain Helper__ - A base Helper, Provide low-level test functions allowing to assert various behaviors on the managed context.
- __Cleaner Helper__ - A Helper specifically aimed at cleaning a Context before and/or after test. Useful when performing a lot of test on a single environment to avoid data collision, or performing clean-up before or after a test suite is run. Most Cleaner helper work in a non-destructive way, ensuring cleaned data can be retrieved post-testing.
- __Service Helper__ - The Service Helper provides more focused functionalities aiming services commonly implemented with the managed technology or protocol, such as data producing and consumption.

#### Lifecycle

Helper implement a Lifecycle pattern, allowing the allocation and disposition of resources. For example, to open a connection before and after test. The following lifecycle states exists:

 - __None__ where the Helper has not been used yet. In this phase, it is possible to configure the Context and other parameters for the Helper.
 - __Initialised__  where resources (such as connections) has been allocated by the Helper. Most initialisable Helper _must_ be initialized prior to use. Such Helpers implement the  `Initialisable` interface. 
 - __Disposed__ where resources allocated to the Helper has been freed. Disposable Helpers implements the `Disposable` interface and should be disposed to ensure correct clean-up, otherwise some resources may be kept unavailable. _Note: a warning is emitted if a `Disposable` object is garbage collected without being disposed._

Typically, a Lifecycle Helper may implement the `Initialisable` interface to allocate resources and optionally the `Disposable` interface if some clean-up is required. A short example using the `SftpHelper`, which is both `Initialisable` and `Disposable`:

	//create and init (open connection to SFTP)
	SftpHelper helper = new SftpHelper(someSftpContext);
	helper.initialise();
	
	//now the connection to the SFTP server is open,
	//we can now perform actions...
	
	// once we're done, dispose of used resources (SFTP connection)
	helper.dispose();
	
#### SFTP

`SftpHelper` functionalities:

 - Anonymous, password or key authentication
 - Common SFTP actions: upload, download, move, delete, list directories and files...
 - Test actions: assert file existence, check file checksum, more directory content, create dummy file or directory, clean managed context...


| Configuration | Required | Default |
| --- | --- | --- |
| host | Y | |
| port| | 22 |
| login | | |
| password | | |
| key| | |
| key passphrase| | |

_Note:_ if no login and password / key are provided, anonymous login will be used.

Example:

	# Use either password or key authentication
	SftpAuthContext passwdCtx = new SftpAuthContext(login, pass);
	SftpAuthContext keyCtx = new SftpAuthContext(login, keyFile, keyPass);
	
	SftpHelper helper = new SftpHelper(keyCtx);
	helper.initialise();
	
	//do stuff...
	
	helper.dispose();

#### FTP
`FtpHelper` functionalities:

 - Password or anonymous authentication
 - Common FTP actions: upload, download, move, delete, list...
 - Test actions: assert file existence, more to come.

| Configuration | Required | Default |
| --- | --- | --- |
| host | Y | |
| port| | 21 |
| login | | |
| password | |

Example:

    FtpContext ctx = new FtpContext(host, port, login, password);
    FtpHelper helper = new FtpHelper(ctx);
    helper.initialise();
    
    //perform actions...

    helper.dispose();	


#### JDBC
`JdbcHelper` functionalities:

 - `javax.sql` package friendly
 - Leverage the Spring JDBC Framework so you can focus on your tests
 - More use case to come...

| Configuration | Required | Default |
| --- | --- | --- |
| datasource | Y | |
| database | | |

_Note: the Context configuration will be improved to allow more flexible parameters_

Example:
	
	JdbcContext context = new JdbcContext(dataSource, "mydatabase");
	JdbcHelper helper = new JdbcHelper(context);
	helper.initialise();
	//perform actions...
	
#### CMIS

`CmisHelper` functionalities:

 - Support __Web Service__ and __AtomPub__ binding
 - Manage CMIS documents and folders
 - Test actions: assert file existence, create dummy directory or file, check file checksum...

__Atom Pub Binding __
| Configuration | Required | Default |
| --- | --- | --- |
| AtomPub URL| Y | |
| user | Y | |
| password | | |

Example:
	
	//main context is initialized using the binding context
	AtomPubBindingContext atomPubBindingContext = new AtomPubBindingContext(user, password, atomPubUrl);
	CMISContext ctx = new CMISContext(bindingContext, repo);
	CMISHelper helper = new CMISHelper(ctx);
	
__Web Service Binding__

| Configuration | Required | Default |
| --- | --- | --- |
| WS base URL| Y | |
| user | Y | |
| password | | |

_Note: the context is configured by appending CMIS services WSDL to the base URL. It is also possible to define or override each services URL one per one._

Example:
	
	//main context is initialized using the binding context
	WebServiceBindingContext wsBindingContext = new WebServiceBindingContext(user, password, wsBaseUrl);
	CMISContext ctx = new CMISContext(bindingContext, repo);
	CMISHelper helper = new CMISHelper(ctx);

#### MantisBT

`MantisBT` helper is used to interact with a Mantis Bug Tracker server instance. Functionalities:

 -  Password authentication
 - Manage issue lifecycle: create, update, delete, assign, add notes...
 - Test actions: list project's issues, assert issue with attachment exist, create dummy issues and projects...

| Configuration | Required | Default |
| --- | --- | --- |
| url| Y | |
| user | Y | |
| password | | |
| project| Y | |

Example:
		
	MantisBTContext context = new MantisBTContext(url, username, password, project);
	new MantisBTHelper(context);
	helper.initialise();

#### File

`FileHelper` functionalities:
 - Check file checksum, assert file content, stream content...
 - Generate and transform files for your tests

| Configuration | Required | Default |
| --- | --- | --- |
| file | Y | |
| charset | | System default charset |

Example:

	FileHelper fileHelper = new FileHelper("myfile.txt", "UTF-8");
	boolean containBanana = fileHelper.contains("I am a banana");

### Advanced 

Knowing the basics of Quark, the advanced usage will allow you to:

 - Create a flexible hierarchy of Helper matching your system architecture, so you can run test against your entire application simply
 - Provide clear, complete human-redable test output to detect and solve bugs efficiently

#### Build-up Infrastructure level Helpers
Often, you have to reproduce similar test steps or actions during your test, such as feeding data, asserting the completion of a task, reading data from database matching certain condition, etc. Instead of re-implementing the Helper configuration and use in each test, you can create your Infrastructure Helper which will handle all your test use-cases in a simple and reusable component. 

Your specialized Helpers can re-use existing Helpers and provide high-level test functions allowing your teams to run test directly against your applications without having to worry about retrieving and configuring every bit of configuration required for your application.

For example, considering again our application receiving files from SFTP and archiving them to CMIS. Instead of using a `CmisHelper` and `SftpHelper` in every-one of your test, we can implement our `MyCompanyHelper` providing directly our test functions:

	public class MyCompanyHelper extends AbstractHelper<AppContext> implements Helper, Initialisable, Disposable {
		
		private SftpHelper sourceSftpHelper;
		private CmisHelper archiveCmisHelper;
		
		public AppHelper(AppContext ctx){
			super(context);
			sourceSftpHelper = new SftpHelpet(context.getSourceSftp());
			archiveCmisHelper = new CMISHelper(context.getArchiveCmis());
		}
		
		public void initialise(){
			sourceSftpHelper.initialise();
			archiveCmisHelper.initialise();
		}
		
		public void feedFile(File file){
			sourceSftpHelper.upload(file, context.getSourcePollDirectory());
		}
		
		public HelperResult<Document> checkCmisArchive(File file){
			String archiveDir = context.getCmisArchiveDir();
			Pattern pattern = Pattern.compile(file.getName());
			return archiveCmisHelper.containsDocument(archiveDir, pattern);
		}
	}

With the `MyCompanyContext`:

	public class MyCompanyContext implements Context {
		private SftpContext sourceSftpContext;
		private CmisContext archiveCmisContext;
		private String sourceSftpPollDir;
		private String archiveCmisDir;

		//and a constructor with setters/getters...
	}

Our `AppHelper` can now be used within our test to perform simple test actions, and can be easily extended for more advanced use. `feedData(File)` upload a test file of our choice to the SFTP, to be consumed by our application. `checkCmisArchive(File)` ensure our file has been archive. (Note the implementation is quite naïve, for better result we should also check file checksum and other details as required.)

A few things were introduced in our previous example:

 - `MyCompanyHelper` is a simple POJO class containing our application Context: SFTP and CMIS server along with the source and archive directories.
 - `AbstractHelper` class is extended by any `Helper`, using the concrete Context as generic. It provides basic Helper infrastructure, including a `context` protected field.
 - `Initialisable` and `Disposable` represent the Helper lifecycle, using `initialise()` and `dispose()` to allocate and free sub-Helpers resources and connections. See [Lifecycle](#lifecycle)
 - the `HelperResult`, representing a test result with output, success or failure and the context under which the test action ran. See [Report and log with HelperResult](#report-and-log-with-helperresult).

#### Report and log with `HelperResult`

##### Helper what?
`HelperResult` is a small container providing access to various test action results and data. `HelperResult` instances are often returned by Helpers functions.

 - Whether the test actions succeed or failed through `isSuccess()`
 - The test action output, if any (data obtained or produced by the test action) through `getActual()`)
 - The Context under which the action ran  through `getContext()`
 - And a complete and user-friendly description of the Result through `getDescription()`

##### Test report with HelperResult

You can leverage the `HelperResult` to log or write test reports easily. Consider a test where you assert the checksum of a File on your SFTP server against a local file:

	File file = new File("testfile.xml")
	HelperResult result = sftpHelper.compareChecksum(someLocalFile, "/app/somefile.xml");
	//return :
	// {
	//	success : false,
	//	actual: ff66...,
	//	expected: 35d9...,
	//	context: [sftp://user@localhost:22/myapp/file/somefile.xml, "testfile.xml"]
	// }

We can log this result in our test and/or perform assertions:

	logger.info("Checksum comparison > " + helper.getDescription());

	Assert.assertTrue(helper.isSuccess(), "Checksum comparison failed with result " + helper.getDescription());

Giving an output like:
	
	# Checksum comparison > failure: found ff66..., expected 35d9... 
	# with [sftp://user@localhost:22/myapp/file/somefile.xml, testFile.xml]
	#
	# Checksum comparison failed with result failure: found ff66..., expected 35d9...
	# with [sftp://user@localhost:22/myapp/file/somefile.xml, testFile.xml]

It is also possible to use `getActual()`, `getExpected()`, `getActual()`, `getContext()`, etc. to report relevant parts of our results. See Javadoc for more details.

##### Create HelperResult for your Helpers

When creating your own Helpers, you can return `HelperResult` to give an easily usable output. The `com.github.pierrebeucher.quark.core.result.ResultBuilder` class allow you to create `HelperResult` instances easily, such as:

	//result when a asserting at least 3 files has been archived on some SFTP server
	ResultBuilder.instance(archiveList) //actual result
		.success(archiveList.size() == 3) // success or failure
		.context(sftpContext); 	// context under which the test action took place

To give an understandable and readable result, make sure to **provide at least: actual output, success or failure and context !**

## Internal Design

Overview of Quark internal design. This section will help users understand how Quark works internally, and Developers work-out the internal class structure. 

### Core
We aim to keep our internal architecture simple, flexible and maintainable. The diagram below represent the core components of Quark:

![architecture overview diagram](https://github.com/PierreBeucher/quark/raw/master/docs/assets/img/internal-design-overview.jpg)

The Helper will interact with the SUT through the Test Controller (which will depend of the test framework or method used to run Quark). Helper is simply linked to one or more Context, which will be used when performing test actions. Also, a Helper may contain one or more child Helper to perform lower level of operations. 

These components are found in the `com.github.pierrebeucher.quark.core` package with:

 - `helper` for Helper interfaces and base classes
 - `context` for Context base classes
 - `result` for HelperResult interfaces and base classes
 - `lifecycle` for Helper Lifecycle API and base classes

### Lifecycle management

Helper with Lifecycle have a slightly more complex architecture, using a `LifecycleManager` internally to manage their lifecycle phases. Such Helper extends the `AbstractLifecycleHelper` class providing a `LifecycleManager` protected field. 

See the `LifecycleManager` Javadoc for more details.

## Developers and maintainers

Pierre Beucher - pierre.beucher@equaternaire.com
 
[apache-common-net]: https://commons.apache.org/proper/commons-net/

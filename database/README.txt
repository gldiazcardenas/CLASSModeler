Database Configuration:

The database connection is based in a DataSource configuration, this MUST be provided by the Glassfish server
in order to be used by the CLASSModeler JPA implementation.


The DBMS is MySQL version 5.1.
The DataSource name MUST BE 'jdbc/CLASSModelerDS', the persistence.xml looks for that name on JPA initialization.



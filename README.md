# Sclera - Text Files Connector

Enables Sclera to work with free-form text files.

The text files are viewed as tables, with two columns: an identifier column containing the file's path, and another column containing the file's contents. These files can now be accessed in a manner similar to tables in a SQL query.

A common use case is to use this in conjunction with the [Sclera - OpenNLP Connector](https://www.scleradb.com/doc/ref/components#sclera-opennlp) which can be used to extract entities from the file contents.

For details on how to use the connector, please see the [ScleraSQL Reference](https://www.scleradb.com/doc/ref/sqlextdataaccess#sclera-textfiles) document.

This is a sample component showcasing Sclera's ability to interface with external data. For the implementation details, please see the [Sclera Data Access Connector Development](https://www.scleradb.com/doc/sdk/sdkextdataaccess) document.

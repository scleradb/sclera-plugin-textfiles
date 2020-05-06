# Sclera - Text Files Connector

[![Build Status](https://travis-ci.org/scleradb/sclera-plugin-textfiles.svg?branch=master)](https://travis-ci.org/scleradb/sclera-plugin-textfiles)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.scleradb/sclera-plugin-textfiles_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.scleradb/sclera-plugin-textfiles_2.13)
[![scaladoc](https://javadoc.io/badge2/com.scleradb/sclera-plugin-textfiles_2.13/scaladoc.svg)](https://javadoc.io/doc/com.scleradb/sclera-plugin-textfiles_2.13)

Enables Sclera to work with free-form text files.

The text files are viewed as tables, with two columns: an identifier column containing the file's path, and another column containing the file's contents. These tables can be scanned, filtered, joined, and aggregated in a manner similar to database tables in a SQL query.

A common use case is to use this in conjunction with the [Sclera - OpenNLP Connector](https://www.scleradb.com/docs/setup/components/#sclera-opennlp) which can be used to [extract entities from the file contents](https://www.scleradb.com/docs/sclerasql/sqlexttext/).

For details on how to use the connector, please see the [ScleraSQL Reference](https://www.scleradb.com/docs/sclerasql/sqlextdataaccess/#accessing-text-files) document.

This is a sample component showcasing Sclera's ability to interface with external data. For the implementation details, please see the [Sclera Data Access Connector Development](https://www.scleradb.com/docs/sdk/sdkextdataaccess/) document.

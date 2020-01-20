/**
* Sclera - Text Files Connector
* Copyright 2012 - 2020 Sclera, Inc.
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*     http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.scleradb.plugin.datasource.textfiles.source

import java.io.File

import com.scleradb.sql.types.SqlCharVarying
import com.scleradb.sql.datatypes.Column
import com.scleradb.sql.result.TableResult

import com.scleradb.external.objects.ExternalSource

/** Text files data source
  * @param paths List of paths of directories and text files
  */
class TextFilesSource(
    paths: List[String]
) extends ExternalSource {
    /** Name of the data source */
    override val name: String = TextFilesSource.name

    /** Iterator over the files in the result */
    private def files: Iterator[File] = {
        def getFiles(fs: Iterator[File]): Iterator[File] = fs.flatMap { f =>
            if( f.isDirectory ) getFiles(f.listFiles().iterator)
            else Iterator(f)
        }

        getFiles(paths.iterator.map { path => new File(path) })
    }

    /** Columns of the result (virtual table).
      * Total two columns; the first contains the file name,
      * and the second its contents.
      */
    override val columns: List[Column] = List(
        Column("FILE", SqlCharVarying(None)),
        Column("CONTENTS", SqlCharVarying(None))
    )

    /** TextFilesResult object to generate the result */
    override def result: TableResult = new TextFilesResult(columns, files)

    /** String used for this source in the EXPLAIN output */
    override def toString: String =
        "%s(\"%s\")".format(name, paths.mkString(","))
}

object TextFilesSource {
    /** The data source name */
    val name: String = "TEXTFILES"
}

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
import scala.io.Source

import com.scleradb.sql.expr.{SortExpr, CharConst}
import com.scleradb.sql.datatypes.Column
import com.scleradb.sql.result.{TableResult, ScalTableRow}

/** Generates a table containing the contents of a CSV file
  *
  * @param columns Generated table's columns
  * @param filesIter Iterator over the text files
  */
class TextFilesResult(
    override val columns: List[Column],
    val filesIter: Iterator[File]
) extends TableResult {
    override val resultOrder: List[SortExpr] = Nil

    /** Reads the text files and emits the data as an iterator on rows */
    override def rows: Iterator[ScalTableRow] = filesIter.map { f =>
        val src: Source = Source.fromFile(f)
        val text: String = src.getLines().mkString(" ")
        src.close()

        ScalTableRow(
            "FILE" -> CharConst(f.getCanonicalPath()),
            "CONTENTS" -> CharConst(text)
        )
    }

    override def close(): Unit = { }
}

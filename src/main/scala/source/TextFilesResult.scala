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

import com.scleradb.sql.expr.{SortExpr, CharConst}
import com.scleradb.sql.datatypes.Column
import com.scleradb.sql.result.{TableResult, ScalTableRow}

/** Generates a table containing the contents of a text file
  *
  * @param columns Generated table's columns
  * @param paths List of paths of directories and text files
  */
class TextFilesResult(
    override val columns: List[Column],
    paths: List[String]
) extends TableResult {
    override val resultOrder: List[SortExpr] = Nil
    private val contentIter: ContentIter = ContentIter()

    /** Reads the text files and emits the data as an iterator on rows */
    override def rows: Iterator[ScalTableRow] =
        paths.iterator.flatMap(contentIter.iter).map { content =>
            ScalTableRow(
                "FILE" -> CharConst(content.name),
                "CONTENTS" -> CharConst(content.text)
            )
        }

    override def close(): Unit = { contentIter.close() }
}

object TextFilesResult {
    /** Constructor */
    def apply(columns: List[Column], paths: List[String]): TextFilesResult =
        new TextFilesResult(columns, paths)
}

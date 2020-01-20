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

package com.scleradb.plugin.datasource.textfiles.service

import com.scleradb.sql.expr.{ScalValueBase, CharConst}
import com.scleradb.external.service.ExternalSourceService

import com.scleradb.plugin.datasource.textfiles.source.TextFilesSource

/** Text files data service */
class TextFilesService extends ExternalSourceService {
    /** Identifier for the service */
    override val id: String = TextFilesSource.name

    /** Creates a TextFileSource object given the generic parameters
      * @param params Generic parameters
      */
    override def createSource(
        params: List[ScalValueBase]
    ): TextFilesSource = {
        if( params == Nil )
            throw new IllegalArgumentException(
                "Input files not specified for \"" + id + "\""
            )

        val fileNames: List[String] = params.map {
            case CharConst(s) if( s != "" ) => s
            case v =>
                throw new IllegalArgumentException(
                    "Illegal file name specified for \"" + id +
                    "\": " + v.repr
                )
        }

        new TextFilesSource(fileNames)
    }
}

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

import java.nio.file.{Path, PathMatcher, FileSystem, FileSystems}
import java.io.{File, InputStream, FileInputStream}
import java.util.zip.{ZipInputStream, GZIPInputStream}

import scala.collection.mutable
import scala.io.Source

/** Recursively iterate over the files in a directory/zip file */
class ContentIter(filterOpt: Option[String => Boolean]) {
    /** Content of a file */
    class Content(val name: String, val text: String)

    private val streams: mutable.ListBuffer[InputStream] = mutable.ListBuffer()

    def iter(path: String): Iterator[Content] = iter(new File(path))

    def iter(f: File): Iterator[Content] =
        if( f.isDirectory ) f.listFiles.iterator.flatMap(iter) else {
            val fis: FileInputStream = new FileInputStream(f)
            streams.append(fis)
            iter(f.getName, fis)
        }

    def iter(name: String, is: InputStream): Iterator[Content] =
        if( filterOpt.exists(filter => !filter(name)) ) {
            Iterator()
        } else if( name.endsWith(".zip") ) {
            val zis: ZipInputStream = new ZipInputStream(is)
            unzipIter(zis)
        } else if( name.endsWith(".gz") ) {
            val gzis: GZIPInputStream = new GZIPInputStream(is)
            iter(name.substring(0, name.length - 3), gzis)
        } else {
            val src: Source = Source.fromInputStream(is)
            Iterator(new Content(name, src.mkString))
        }

    def unzipIter(zis: ZipInputStream): Iterator[Content] =
        Option(zis.getNextEntry) match {
            case Some(zipEntry) if( zipEntry.isDirectory ) => unzipIter(zis)
            case Some(zipEntry) => iter(zipEntry.getName, zis) ++ unzipIter(zis)
            case None => Iterator()
        }

    def close(): Unit = {
        streams.foreach { is => is.close() }
        streams.clear()
    }
}

object ContentIter {
    def apply(filterOpt: Option[String => Boolean] = None): ContentIter =
        new ContentIter(filterOpt)

    def apply(patterns: List[String]): ContentIter = apply(filterOpt(patterns))

    /** File name filter that accepts if name matches any of the patterns */
    private def filterOpt(patterns: List[String]): Option[String => Boolean] =
        if( patterns.isEmpty ) None else {
            val fs: FileSystem = FileSystems.getDefault()
            val matchers: List[PathMatcher] =
                patterns.map { pattern => fs.getPathMatcher(s"glob:$pattern") }
            Some(path => matchers.exists { m => m.matches(Path.of(path)) })
        }
}

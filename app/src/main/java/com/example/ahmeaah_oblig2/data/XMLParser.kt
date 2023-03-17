package com.example.ahmeaah_oblig2.data

import android.util.Xml
import com.example.ahmeaah_oblig2.Party

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

class XMLParser {
    private val ns: String? = null
    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): List<Party> {
        inputStream.use {
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(it, null)
            parser.nextTag()
            return readFeed(parser)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readFeed(parser: XmlPullParser): List<Party> {
        val listOfVotes = mutableListOf<Party>()

        parser.require(XmlPullParser.START_TAG, ns, "districtThree")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            if (parser.name == "party") {
                listOfVotes.add(readEntry(parser))
            } else {
                skip(parser)
            }
        }
        return listOfVotes
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readEntry(parser: XmlPullParser): Party {
        parser.require(XmlPullParser.START_TAG, ns, "party")
        var id = " "
        var votes = " "
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "id" -> id = readId(parser)
                "votes" -> votes = readVotes(parser)
                else -> skip(parser)
            }
        }
        return Party(id, votes)
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readId(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "id")
        val name = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "id")
        return name
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readVotes(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "votes")
        val votes = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "votes")
        return votes
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}
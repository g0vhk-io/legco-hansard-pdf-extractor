package g0vhk.legco.hansard

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDNamedDestination
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination
import org.apache.pdfbox.text.PDFTextStripper
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStreamWriter

fun parseMembers(lines: List<String>): List<String> {
    val output = ArrayList<String>()
    lines.drop(1).forEach {
        if (it.trim().length > 0) {
            val p = it.indexOf(',')
            if (p > 0) {
                output.add(it.substring(0, p))
            }
            else {
                output.add(it)
            }
        }

    }
    return output
}

fun main(args: Array<String>) {
    val document = PDDocument.load(File(args[0]))
    val catalog = document.documentCatalog
    val outline = catalog.documentOutline
    var currentBookmark = outline.firstChild.firstChild
    val bookmarks = ArrayList<PDPageXYZDestination>()
    val bookmarkNames = ArrayList<String>()
    while (currentBookmark != null) {
        if (currentBookmark.action != null) {
            val action = currentBookmark.action as PDActionGoTo
            val destionation = action.destination as PDNamedDestination
            val namesDict = catalog.names
            val dests = namesDict.dests
            val xyz = dests.getValue(destionation.namedDestination) as PDPageXYZDestination
            bookmarkNames.add(destionation.namedDestination)
            bookmarks.add(xyz)
        }

        currentBookmark = currentBookmark.nextSibling
    }

    for (i in 0 .. bookmarkNames.size - 2) {
        val startBookmark = bookmarks[i]
        val endBookmark = bookmarks[i + 1]
        val stripper = HansardTextStripper(startBookmark, endBookmark)
        val lines = stripper.getText(document).replace(Regex("\n +\n"),"<br/>").replace("\n", "").replace("<br/>", "\n").split("\n")
        when (bookmarkNames[i]) {
            "mbp" -> {
                parseMembers(lines).forEach {
                    println(it)
                }

            }
            "mba" -> {
                parseMembers(lines).forEach {
                    println(it)
                }
            }
            "poa" -> {
                parseMembers(lines).forEach {
                    println(it)
                }
            }
        }
    }
    document.close()
}
package g0vhk.legco.hansard

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDNamedDestination
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination
import java.io.File
import com.google.gson.GsonBuilder

fun parseMembers(lines: List<String>): List<String> {
    val output = ArrayList<String>()
    lines.drop(1).forEach {
        if (it.trim().isNotEmpty()) {
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

fun cleanLines(lines: List<String>): List<String> {
    return lines.drop(1).map { it.trim() }.filter { ! it.isEmpty() }
}

fun main(args: Array<String>) {
    val document = PDDocument.load(File(args[0]))
    val catalog = document.documentCatalog
    val outline = catalog.documentOutline
    var currentBookmark = outline.firstChild.firstChild
    val bookmarks = ArrayList<PDPageXYZDestination>()
    val bookmarkNames = ArrayList<String>()
    val hansard = Hansard()
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

    val dateStripper = HansardDateStripper(bookmarks[0])
    hansard.date = dateStripper.getText(document)
    for (i in 0 .. bookmarkNames.size - 2) {
        val startBookmark = bookmarks[i]
        val endBookmark = bookmarks[i + 1]
        val stripper = HansardTextStripper(startBookmark, endBookmark)
        val lines = stripper.getText(document).replace(Regex("\n +\n"),"<br/>").replace("\n", "").replace("<br/>", "\n").split("\n")
        when (bookmarkNames[i]) {
            "mbp" -> {
                hansard.membersPresent.addAll(cleanLines(lines))
            }
            "mba" -> {
                hansard.membersAbsent.addAll(cleanLines(lines))
            }
            "poa" -> {
                hansard.publicOfficersAttending.addAll(cleanLines(lines))
            }
            "cia" -> {
                hansard.clerksInAttendance.addAll(cleanLines(lines))
            }
            else -> {
                if (bookmarkNames[i].startsWith("SP")) {
                    val content = lines.joinToString("\n")
                    val parts = content.split('：')
                    val speech = Speech(parts[0], parts[1].trim())
                    hansard.speeches.add(speech)
                }
            }
        }
    }
    val gson = GsonBuilder().setPrettyPrinting().create()
    val json = gson.toJson(hansard)
    println(json)
    document.close()
}
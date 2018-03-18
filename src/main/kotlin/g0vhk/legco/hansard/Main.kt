package g0vhk.legco.hansard

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDNamedDestination
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination
import java.io.File
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.pdfbox.io.IOUtils
import java.io.FileOutputStream
import java.net.URI

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

fun downloadPDF(url: String): String {
    val uri = URI(url)
    val path = uri.path
    val fileName = path.substring(path.lastIndexOf('/') + 1)
    val file = File(fileName)
    if (file.exists()) {
        println("${fileName} already downloaded.")

    } else {
        val client = getUnsafeOkHttpClient()
        val request = Request.Builder()
                .url(url)
                .build()
        val response = client.newCall(request).execute()
        response.body()?.byteStream().let { stream ->
            val outputStream = FileOutputStream(file)
            IOUtils.copy(stream, outputStream)
        }
    }
    return fileName
}

fun splitByTwo(s: String, sep: String): Array<String> {
    val p = s.indexOf(sep)
    if (p == -1) {
        return arrayOf<String>(s, "")
    }
    val first = s.substring(0, p)
    val second = s.substring(p + 1)
    return arrayOf<String>(first, second)
}

fun processContent(content: String): Array<String> {
    for (sep in arrayOf("：", "︰", ":")) {
        var parts = splitByTwo(content,sep)
        if (parts[0].length <= 50) {
            return parts
        }
    }
    throw Exception("${content} cannot be processed.")
}

fun main(args: Array<String>) {
    val url = args[0]
    val fileName = downloadPDF(url)
    val document = PDDocument.load(File(fileName))
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
            var xyz = dests.getValue(destionation.namedDestination) as PDPageXYZDestination?

            xyz?.let {
                //ugly workaround for cm20141112-confirm-ec.pdf
                if (fileName == "cm20141112-confirm-ec.pdf") {
                    if (destionation.namedDestination == "SP_MB_KKK_00195")
                        xyz?.top = 700
                    if (destionation.namedDestination == "SP_MB_KKK_00200")
                        xyz?.top = 400
                }
                //ugly workaround for cm0711-confirm-ec.pdf
                var skipped = false
                if (fileName == "cm0711-confirm-ec.pdf" &&
                        destionation.namedDestination == "SP_MB_AS_00025") {
                    skipped = true
                }
                if (fileName == "cm1204-confirm-ec.pdf" &&
                        destionation.namedDestination == "SP_MB_AS_00466") {
                    skipped = true
                }
                if (fileName == "cm1107-confirm-ec.pdf" &&
                        destionation.namedDestination == "SP_MB_AS_00070") {
                    skipped = true
                }
                if (fileName == "cm0222-confirm-ec.pdf" &&
                        destionation.namedDestination == "SP_PO_STH_00098") {
                    skipped = true
                }
                if (fileName == "cm0222-confirm-ec.pdf" &&
                        destionation.namedDestination == "SP_PO_STH_00097") {
                    skipped = true
                }
                if (fileName == "cm0221-confirm-ec.pdf" &&
                        destionation.namedDestination == "SP_MB_DK_00022") {
                    skipped = true
                }
                if (fileName == "cm0221-confirm-ec.pdf" &&
                        destionation.namedDestination == "SP_MB_AS_00025") {
                    skipped = true
                }
                if (fileName == "cm0221-confirm-ec.pdf" &&
                        destionation.namedDestination == "SP_MB_KL_00029") {
                    skipped = true
                }
                if (fileName == "cm0220-confirm-ec.pdf" &&
                        destionation.namedDestination == "SP_MB_AS_00077") {
                    skipped = true
                }
                if (fileName == "cm0220-confirm-ec.pdf" &&
                        destionation.namedDestination == "SP_MB_AS_00085") {
                    skipped = true
                }
                if (fileName == "cm0219-confirm-ec.pdf" &&
                        destionation.namedDestination == "SP_MB_AS_00340") {
                    skipped = true
                }
                if (fileName == "cm0116-confirm-ec.pdf" &&
                        destionation.namedDestination == "SP_MB_AS_00066") {
                    skipped = true
                }
                if (fileName == "cm0116-confirm-ec.pdf" &&
                        destionation.namedDestination == "SP_PO_CE_00078") {
                    skipped = true
                }
                if (fileName == "cm0703-confirm-ec.pdf" &&
                        destionation.namedDestination == "SP_MB_SCK_00282") {
                    skipped = true
                }
                if (fileName == "cm0110-confirm-ec.pdf" &&
                        destionation.namedDestination == "SP_MB_DK_00145") {
                    skipped = true
                }
                if (fileName == "cm1212-confirm-ec.pdf" &&
                        destionation.namedDestination == "SP_MB_AS_00433") {
                    skipped = true
                }
                if (fileName == "cm1024-confirm-ec.pdf" &&
                        destionation.namedDestination == "SP_MB_AS_00257") {
                    skipped = true
                }
                if (! skipped) {
                    bookmarkNames.add(destionation.namedDestination)
                    bookmarks.add(xyz)
                }
            }

        }

        currentBookmark = currentBookmark.nextSibling
    }

    val dateStripper = HansardDateStripper(bookmarks[0])
    hansard.date = dateStripper.getText(document)
    hansard.url = url
    var sequence = 0
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
                val bookmark = bookmarkNames[i]
                val isSP = bookmark.startsWith("SP_")
                val isOLE = bookmark.startsWith("OLE_")
                val isEV = bookmark.startsWith("EV")
                if (isSP || isOLE || isEV) {
                    sequence++
                    val content = lines.joinToString("\n").trim()
                    val speech = if (isEV) {
                        Speech("", content, sequence, bookmark)
                    } else {
                        val parts = processContent(content)
                        Speech(parts[0], parts[1].trim(), sequence, bookmark)
                    }
                    hansard.speeches.add(speech)
                }
            }
        }
    }
    val gson = GsonBuilder().setPrettyPrinting().create()
    val json = gson.toJson(hansard)
    val outputFileName = fileName.replace(".pdf", ".json")
    File(outputFileName).printWriter().use { out ->
        out.print(json)
    }
    document.close()
    println("Conversion completed.")
}
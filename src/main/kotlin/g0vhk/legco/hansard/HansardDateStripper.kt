package g0vhk.legco.hansard

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination

class HansardDateStripper(startBookmark: PDPageXYZDestination): HansardTextStripper(null, startBookmark){
    override fun getText(doc: PDDocument?): String {
        val lines = super.getText(doc).split('\n').filter { it.isNotBlank() }
        val regex = Regex("(\\d+)年(\\d+)月(\\d+)日")
        var output = ""
        lines.forEach {
            val result = regex.find(it)
            result?.let { m ->
                output = m.groupValues[1] + "-" + m.groupValues[2] + "-" + m.groupValues[3]
            }
        }
        return output
    }
}
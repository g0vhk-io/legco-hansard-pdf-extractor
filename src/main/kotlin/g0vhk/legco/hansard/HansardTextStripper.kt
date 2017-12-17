package g0vhk.legco.hansard


import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.text.TextPosition

class HansardTextStripper: PDFTextStripper {
    private val startBookmark: PDPageXYZDestination
    private val endBookmark: PDPageXYZDestination
    private var lastPage: Int? = null
    private var lastY: Float? = null
    private var lastEndY: Float? = null
    private var prevS: String? = null
    constructor(startBookmark: PDPageXYZDestination, endBookmark: PDPageXYZDestination): super() {
        this.startBookmark = startBookmark
        this.endBookmark = endBookmark
        this.startPage = startBookmark.retrievePageNumber() + 1
        this.endPage = endBookmark.retrievePageNumber() + 1
    }

    override fun processTextPosition(text: TextPosition?) {
        text?.let {
            if (currentPageNo < startPage) {
               return
            }

            if (currentPageNo > endPage) {
                return
            }

            val textY = it.endY

            if (it.y <= 70) {
                return
            }


            if (currentPageNo == startPage && startBookmark.top <= textY) {
                return
            }
            if (currentPageNo == endPage && endBookmark.top >= textY) {
                return
            }

            if (it.x >= 510) {
                return
            }


            var s = it.unicode
            //println(s + " " + s[0].toInt() + " " + textY + " " + it.x)
            lastPage?.let { n ->
                if (lastPage != currentPageNo) {
                    //output.write("\n")
                    lastY = null
                    prevS = null
                }
                lastEndY?.let { y ->
                    if (Math.abs(it.endY - y) >= 10) {
                        output.write("\n")
                    }
                }
            }
            output.write(s)
            lastPage = currentPageNo
            lastY = textY
            lastEndY = it.endY
            prevS = s
        }
    }
}
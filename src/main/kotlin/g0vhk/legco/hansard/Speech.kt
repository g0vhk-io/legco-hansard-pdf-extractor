package g0vhk.legco.hansard

import java.util.regex.Pattern

class Speech {
    val title: String
    val content: String
    val sequence: Int
    val bookmark: String
    constructor (title: String, content: String, sequence: Int, bookmark: String){
        val p = Pattern.compile("^(\\d+. +)(.*)")
        val m = p.matcher(title)
        if (m.find()) {
            this.title = m.group(2)
        } else {
            this.title = title
        }
        this.content = content
        this.sequence = sequence
        this.bookmark = bookmark
    }
}
package g0vhk.legco.hansard

import java.util.regex.Pattern

class Speech {
    val title: String
    val content: String
    constructor (title: String, content: String){
        val p = Pattern.compile("^(\\d+. +)(.*)")
        val m = p.matcher(title)
        if (m.find()) {
            this.title = m.group(2)
        } else {
            this.title = title
        }
        this.content = content
    }
}
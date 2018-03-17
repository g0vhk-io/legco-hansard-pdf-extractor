package g0vhk.legco.hansard

open class Hansard {
    val membersPresent = ArrayList<String>()
    val membersAbsent = ArrayList<String>()
    val publicOfficersAttending = ArrayList<String>()
    val clerksInAttendance = ArrayList<String>()
    val speeches = ArrayList<Speech>()
    var date: String = ""
    var url: String = ""
}
class GroupManager {
    private val groups: MutableList<Group> = mutableListOf()

    fun createGroup(name: String, description: String): Group {
        val newGroup = Group(generateGroupId(), name, description)
        groups.add(newGroup)
        return newGroup
    }

    fun addParticipantToGroup(group: Group, user: User) {
        group.participants.add(user)
    }

    fun removeParticipantFromGroup(group: Group, user: User) {
        group.participants.remove(user)
    }

    private fun generateGroupId(): String {
        // Implement your logic to generate a unique group ID
        return "group_${System.currentTimeMillis()}"
    }
}
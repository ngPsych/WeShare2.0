class UserManager {
    private val users: MutableList<User> = mutableListOf()

    fun createUser(name: String, contactDetails: String): User {
        val newUser = User(generateUserId(), name, contactDetails)
        users.add(newUser)
        return newUser
    }

    fun updateUserProfile(user: User, newName: String, newContactDetails: String) {
        user.name = newName
        user.contactDetails = newContactDetails
    }

    fun updateUserNotificationSettings(user: User, enableNotifications: Boolean) {
        user.notificationSettings = enableNotifications
    }

    private fun generateUserId(): String {
        // Implement your logic to generate a unique user ID
        return "user_${System.currentTimeMillis()}"
    }
}
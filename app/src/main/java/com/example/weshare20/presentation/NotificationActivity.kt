import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.weshare20.R
import com.example.weshare20.business.DatabaseHandler
import com.example.weshare20.business.Notification
import com.example.weshare20.business.SessionManager
import com.example.weshare20.business.UserGroup
import com.example.weshare20.interfaces.NotificationActionListener

class NotificationActivity : AppCompatActivity(), NotificationActionListener {

    private lateinit var db: DatabaseHandler
    private lateinit var session: SessionManager
    private lateinit var notificationList: MutableList<Notification>
    private lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        db = DatabaseHandler(this)
        session = SessionManager(this)

        notificationList = mutableListOf() // Initialize an empty list
        adapter = NotificationAdapter(this, notificationList, this)

        val notificationListView: ListView = findViewById(R.id.notificationListView)
        notificationListView.adapter = adapter

        loadNotifications() // Fetch and load notifications
    }

    private fun loadNotifications() {
        // Load notifications using db and session
        val userId = session.getUserId()
        notificationList.clear() // Clear previous notifications
        notificationList.addAll(db.getUserNotifications(userId))
        adapter.notifyDataSetChanged()
    }

    override fun onAccept(notification: Notification, position: Int) {
        db.addUserToGroup(UserGroup(notification.userID, notification.groupID))
        db.deleteNotification(notification.userID, notification.groupID, notification.message)
        notificationList.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    override fun onDecline(notification: Notification, position: Int) {
        db.deleteNotification(notification.userID, notification.groupID, notification.message)
        notificationList.removeAt(position)
        adapter.notifyDataSetChanged()
    }
}

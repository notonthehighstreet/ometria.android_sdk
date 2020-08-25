package com.android.ometriasdk.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.android.ometriasdk.core.Ometria
import com.android.ometriasdk.core.event.OmetriaEventType.NOTIFICATION_INTERACTED

/**
 * Created by cristiandregan
 * on 30/07/2020.
 */

internal class PushClickBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        if (action != null && action == PUSH_TAP_ACTION) {
            val mainIntent = context!!.packageManager.getLaunchIntentForPackage(context.packageName)
            mainIntent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(mainIntent)

            // ToDo extract notificationId
            Ometria.instance().trackNotificationInteractedEvent("notificationId")
        }
    }
}
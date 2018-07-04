package com.eury.touristai.services

import com.eury.touristai.utils.Loggable.Companion.log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        log.d(p0?.messageId ?: "")
    }

    
}

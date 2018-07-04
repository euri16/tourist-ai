package com.eury.touristai.services

import com.google.firebase.iid.FirebaseInstanceIdService

class MyFirebaseIDService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        super.onTokenRefresh()

    }
}

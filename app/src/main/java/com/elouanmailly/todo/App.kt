package com.elouanmailly.todo

import android.app.Application
import com.elouanmailly.todo.network.Api

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Api.INSTANCE = Api(this)
    }
}
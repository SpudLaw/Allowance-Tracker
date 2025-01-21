package com.cal.allowancetracker

import android.app.Application
import com.cal.allowancetracker.data.AppContainer
import com.cal.allowancetracker.data.AppDataContainer

class AllowanceTrackerApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}

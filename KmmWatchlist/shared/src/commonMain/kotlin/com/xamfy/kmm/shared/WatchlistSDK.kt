package com.xamfy.kmm.shared

import com.xamfy.kmm.shared.cache.Database
import com.xamfy.kmm.shared.cache.DatabaseDriverFactory
import com.xamfy.kmm.shared.entity.RocketLaunch
import com.xamfy.kmm.shared.network.WatchlistApi

class WatchlistSDK (databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = WatchlistApi()

    @Throws(Exception::class) suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            api.getAllLaunches().also {
                database.clearDatabase()
                database.createLaunches(it)
            }
        }
    }
}
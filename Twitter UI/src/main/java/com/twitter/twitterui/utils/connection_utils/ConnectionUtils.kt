package com.twitter.twitterui.utils.connection_utils

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission

class ConnectionUtils(var context: Context) : IConnectionUtils {
    /**
     * Get Network connectivity state
     * @return : true if connected
     */
    override val isConnected: Boolean
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        get() {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                ?: return false
            return when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) or
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }
}
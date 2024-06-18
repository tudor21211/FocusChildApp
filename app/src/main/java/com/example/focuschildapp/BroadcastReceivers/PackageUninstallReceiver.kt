package com.example.focuschildapp.com.example.focuschildapp.BroadcastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class PackageUninstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_PACKAGE_REMOVED) {
            val packageName = intent.data?.schemeSpecificPart
            context?.let {
                Toast.makeText(it, "Package uninstalled: $packageName", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

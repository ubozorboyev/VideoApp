package ubr.persanal.videoapp

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import ubr.persanal.videoapp.data.AppDataBase

class App : Application() {


    override fun onCreate() {
        AppDataBase.getInstance(applicationContext)
        super.onCreate()
    }

    override fun onTerminate() {
        AppDataBase.instance?.close()
        super.onTerminate()
    }

    companion object {


        fun getDownloadSpeed(context: Context): String {
            return try {
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val nc = cm.getNetworkCapabilities(cm.activeNetwork)
                nc!!.linkDownstreamBandwidthKbps.toString()
            } catch (e: Exception) {
                ""
            }
        }
    }


}
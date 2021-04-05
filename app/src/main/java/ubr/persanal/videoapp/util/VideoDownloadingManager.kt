package ubr.persanal.videoapp.util

import android.content.Context
import android.util.Log
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class VideoDownloadingManager(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    private  val TAG = "VideoDownloadingManager"

    override suspend fun doWork(): Result {

       return withContext(Dispatchers.IO) {
            try {
                val connection =
                    URL(inputData.getString("MOVIE_URL")).openConnection() as HttpURLConnection
                var downloaded = 0

                val destinationPath = inputData.getString("FILE_PATH")!!
                val file = File(destinationPath)

                if (file.exists()) {
                    downloaded = file.length().toInt()
                    connection.setRequestProperty("Range", "bytes=" + file.length() + "-" )
                } else {
                    file.createNewFile()
                }
                connection.doInput = true
                connection.connect()
                val input: InputStream= BufferedInputStream(connection.inputStream)

                var fileSize = connection.contentLength

                Log.d(TAG, "responseMessage: ${connection.responseMessage}")
                Log.d(TAG, "responseCode: ${connection.responseCode}")
                Log.d(TAG, "requestMethod: ${connection.requestMethod}")
                Log.d(TAG, "contentLength MB : ${fileSize/1024/1024}")

                val fos = if (downloaded == 0)
                    FileOutputStream(destinationPath)
                else {
                    fileSize += downloaded
                    FileOutputStream(destinationPath, true)
                }

                setProgress(Data.Builder().putFloat("FileSize", fileSize.toFloat()/1024/1024).build())

                val data = ByteArray(4096)
                var x = 0
                while (input.read(data).also { x = it } >= 0) {
                    fos.write(data, 0, x)
                    downloaded += x
                    val progress = 100 * downloaded.toFloat() / fileSize
                    setProgress(Data.Builder().putFloat("FileSize", fileSize.toFloat()/1024/1024).build())
                    if (progress <= 100)
                        setProgress(workDataOf("Progress" to progress))
                    Log.d(TAG, "doWork: progress $progress")
                }

                fos.flush()
                fos.close()
                input.close()
                connection.disconnect()
                return@withContext Result.success()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                return@withContext Result.failure(workDataOf("FAILURE" to "ERROR : Fileni yuklab olsihda muammo boladi qaytadan harakat qilib koring"))
            }catch (e:Exception){
                return@withContext Result.retry()
            }
        }
    }
}
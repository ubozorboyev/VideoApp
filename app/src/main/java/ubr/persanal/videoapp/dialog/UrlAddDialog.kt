package ubr.persanal.videoapp.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import ubr.persanal.videoapp.R

class UrlAddDialog(context: Context) {

    private val dialog = AlertDialog.Builder(context).create()
    private val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_url, null, false)
    private val editText = view.findViewById<TextInputEditText>(R.id.editText)

    private var listener: ((String) -> Unit)? = null

    fun show() {

        dialog.setView(view)
        dialog.setCancelable(false)

        view.findViewById<Button>(R.id.addButton).setOnClickListener {

            val text = editText.text.toString()

            if (validHttp(text) && validMp4(text)) {
                listener?.invoke(text)
                dialog.dismiss()
            }
        }

        view.findViewById<Button>(R.id.cancelButton).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun validHttp(uri: String): Boolean {

        return if (uri.startsWith("http://") || uri.startsWith("https://")) true
        else {
            editText.error = "file manzili noto'g'ri kiritilgan"
            false
        }
    }

    private fun validMp4(uri: String): Boolean {

        if (!uri.endsWith(".mp4", true)) {
            editText.error = "Video file farmati kiritilmagan"
            return false
        }
        return true
    }

    fun setUrlAddListener(ls:(String)->Unit){
        listener = ls
    }
}
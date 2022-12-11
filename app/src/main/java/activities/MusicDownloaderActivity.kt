package activities

import android.content.Intent

class MusicDownloaderActivity : DownloaderActivity() {
    override val afterIntent: Intent = Intent(applicationContext, TagEditorActivity::class.java)
}
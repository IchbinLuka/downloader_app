package activities

import android.content.Intent

class VideoDownloaderActivity: DownloaderActivity() {
    override val afterIntent: Intent = Intent(Intent.ACTION_VIEW)
}
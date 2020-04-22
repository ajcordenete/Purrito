package com.aljon.purrito.util

object Constants {

    const val BASE_URL_CAT = "https://api.thecatapi.com/"
    const val BASE_URL_DOG = "https://api.thedogapi.com/"

    const val VIEW_TYPE_LOADING = 0
    const val VIEW_TYPE_ITEM_SMALL = 1
    const val VIEW_TYPE_ITEM_BIG = 2

    const val GRID_ITEM_COUNT = 2

    const val DESC_ORDER = "DESC"
    const val PAGE_LIMIT = 20

    const val GIF_TYPE = "gif"

    const val CONNECTION_ERROR_CATS = "Could not load awesome cats right now..."
    const val CONNECTION_ERROR_DOG = "Could not load awesome dogs right now..."
    const val PERMISSION_ERROR = "Could not proceed with downloading. Go to device settings and check storage permission."

    const val DOWNLOAD_COMPLETE = "File downloaded"
    const val DOWNLOAD_FAILED = "Couldn't download file"

    const val DOWNLOADED_FOLDER_PATH = "Pictures/Purrito"
    const val MIME_TYPE_GIF = "image/gif"
    const val MIME_TYPE_JPG = "image/jpeg"

    val IMAGE_FILE_TYPE = listOf<String>("jpeg", "jpg", "png")

    const val PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0x001;
}
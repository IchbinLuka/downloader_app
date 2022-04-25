package com.ichbinluka.downloader.data

const val httpsRegex = "https://|http://"

val supportedPlatforms = listOf(
    "${httpsRegex}youtube.com(.*)",
    "(.*)youtu.be(.*)",
    "https://reddit",
    "https://redd.it"
)

val unsupportedPlatforms = listOf(
    ""
)
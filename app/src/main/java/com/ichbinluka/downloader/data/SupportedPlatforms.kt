package com.ichbinluka.downloader.data

import java.util.regex.Pattern

const val httpsRegex = "https://|http://"

val supportedPlatforms = listOf(
    "${httpsRegex}youtube.com(.*)",
    "(.*)youtu.be(.*)",
    "https://www.reddit(.*)",
    "https://redd.it(.*)"
).map { Regex(it) }

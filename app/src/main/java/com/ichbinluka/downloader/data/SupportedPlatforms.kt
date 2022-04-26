package com.ichbinluka.downloader.data

import java.util.regex.Pattern

const val httpsRegex = "(https://|http://)"

val supportedPlatforms = listOf(
    "${httpsRegex}youtube.com(.*)",
    "(.*)youtu.be(.*)",
    "${httpsRegex}www.reddit(.*)",
    "${httpsRegex}redd.it(.*)",
    "${httpsRegex}(www.)?tiktok.com(.*)",
    "${httpsRegex}(www.)?twitter.com(.*)"
).map { Regex(it) }

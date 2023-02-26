package com.ichbinluka.downloader.data

const val httpsRegex = "(https://|http://)"

val supportedPlatforms = listOf(
    "${httpsRegex}youtube.com(.*)",
    "(.*)youtu.be(.*)",
    "${httpsRegex}www.reddit(.*)",
    "${httpsRegex}redd.it(.*)",
    "${httpsRegex}(www.)?tiktok.com(.*)",
    "${httpsRegex}(www.)?twitter.com(.*)",
    "${httpsRegex}(www.)?music.youtube.com(.*)"
).map { Regex(it) }

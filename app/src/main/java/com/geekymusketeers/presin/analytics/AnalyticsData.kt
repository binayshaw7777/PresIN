package com.geekymusketeers.presin.analytics


interface AnalyticsData {
    interface ScreenName {
        companion object {
            const val MAIN_ACTIVITY = "MAIN_ACTIVITY"
            const val LOGIN_FRAGMENT = "LOGIN_FRAGMENT"
            const val SPLASH_FRAGMENT = "SPLASH_FRAGMENT"
        }
    }

    interface EventName {
        companion object {
            const val UNAUTHORIZED_ACCESS = "UNAUTHORIZED_ACCESS"
        }
    }
}
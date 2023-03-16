package com.geekymusketeers.presin.analytics


interface AnalyticsData {
    interface ScreenName {
        companion object {
            const val MAIN_ACTIVITY = "MAIN_ACTIVITY"
        }
    }

    interface EventName {
        companion object {
            const val UNAUTHORIZED_ACCESS = "UNAUTHORIZED_ACCESS"
        }
    }
}
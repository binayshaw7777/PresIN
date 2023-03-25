package com.geekymusketeers.presin.analytics


interface AnalyticsData {
    interface ScreenName {
        companion object {
            const val MAIN_ACTIVITY = "MAIN_ACTIVITY"
            const val LOGIN_FRAGMENT = "LOGIN_FRAGMENT"
            const val SPLASH_FRAGMENT = "SPLASH_FRAGMENT"
            const val HOME_FRAGMENT = "HOME_FRAGMENT"
            const val FORGOT_PASSWORD_FRAGMENT = "FORGOT_PASSWORD_FRAGMENT"
            const val VERIFY_OTP_FORGOT = "VERIFY_OTP_FORGOT"
            const val SET_NEW_PASSWORD_FRAGMENT = "SET_NEW_PASSWORD_FRAGMENT"
            const val USER_REGISTER_FRAGMENT = "USER_REGISTER_FRAGMENT"
            const val ORGANIZATION_REGISTER_FRAGMENT = "ORGANIZATION_REGISTER_FRAGMENT"
            const val AVATAR_REGISTER_FRAGMENT = "AVATAR_REGISTER_FRAGMENT"
            const val FACE_SCAN_REGISTER_FRAGMENT = "FACE_SCAN_REGISTER_FRAGMENT"
            const val SETTINGS_FRAGMENT = "SETTINGS_FRAGMENT"
            const val STATS_FRAGMENT = "STATS_FRAGMENT"
            const val ATTENDEES_FRAGMENT = "ATTENDEES_FRAGMENT"
        }
    }

    interface EventName {
        companion object {
            const val UNAUTHORIZED_ACCESS = "UNAUTHORIZED_ACCESS"
        }
    }
}
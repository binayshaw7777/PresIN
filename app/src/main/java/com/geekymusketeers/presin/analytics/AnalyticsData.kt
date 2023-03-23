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
        }
    }

    interface EventName {
        companion object {
            const val UNAUTHORIZED_ACCESS = "UNAUTHORIZED_ACCESS"
        }
    }
}
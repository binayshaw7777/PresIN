package com.geekymusketeers.presin.utils

import android.annotation.SuppressLint

interface SimilarityClassifier {
    /**
     * An immutable result returned by a Classifier describing what was recognized.
     */
    class Recognition(
        /**
         * A unique identifier for what has been recognized. Specific to the class, not the instance of
         * the object.
         */
        private val id: String?,
        /**
         * Display name for the recognition.
         */
        private var title: String?, distance: Float?
    ) {
        private val distance: Float?
        var extra: Any?

        init {
            title = title
            this.distance = distance
            extra = null
        }

        @SuppressLint("DefaultLocale")
        override fun toString(): String {
            var resultString = ""
            if (id != null) resultString += "[$id] "
            if (title != null) resultString += "$title "
            if (distance != null) resultString += String.format("(%.1f%%) ", distance * 100.0f)
            return resultString.trim { it <= ' ' }
        }
    }
}
package de.phyrone.buildscripts

data class SemanticVersion(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val preRelease: PreReleaseType? = null,
) : Comparable<SemanticVersion> {
    override fun compareTo(other: SemanticVersion): Int {
        return when {
            major != other.major -> return major.compareTo(other.major)
            minor != other.minor -> return minor.compareTo(other.minor)
            patch != other.patch -> return patch.compareTo(other.patch)
            preRelease != other.preRelease -> return preRelease?.compareTo(other.preRelease!!) ?: 1
            else -> 0
        }
    }

    override fun toString(): String {
        return "$major.$minor.$patch${preRelease?.let { "-$it" } ?: ""}"
    }

    fun toComposeCompatibleVersion(): String {
        return "${major.coerceAtLeast(1)}.$minor.$patch"
    }

    enum class PreReleaseType : Comparable<PreReleaseType> {
        DEV, ALPHA, BETA, RC;

        override fun toString(): String {
            return name.lowercase()
        }
    }

}

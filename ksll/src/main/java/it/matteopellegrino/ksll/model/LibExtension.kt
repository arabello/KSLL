package it.matteopellegrino.ksll.model

/**
 * Enum data structure to maintain all the library extensions
 * supported by this module
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
enum class LibExtension(private val value: String) {
    DEX("dex"),
    JAR("jar"),
    ZIP("zip"),
    APK("apk");

    companion object {
        fun from(s: String): LibExtension? = values().find { it.value == s }
    }

    override fun toString(): String {
        return value
    }
}
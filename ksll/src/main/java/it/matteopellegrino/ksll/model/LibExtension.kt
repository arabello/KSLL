package it.matteopellegrino.ksll.model

/**
 * TODO: Add class description
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
enum class LibExtension(private val value: String) {
    DEX("dex");

    companion object {
        fun from(s: String): LibExtension? = values().find { it.value == s }
    }


    override fun toString(): String {
        return value
    }
}
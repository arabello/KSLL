package it.matteopellegrino.ksll.model

/**
 * TODO: Add class description
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
enum class LibExtension(private val extension: String) {
    DEX("dex");

    override fun toString(): String {
        return extension
    }}
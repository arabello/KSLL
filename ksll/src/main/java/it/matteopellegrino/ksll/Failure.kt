package it.matteopellegrino.ksll

/**
 * Type of errors passed to failure callback as parameter.
 * Exceptions are wrapped into these types
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
enum class Failure{
    NotTrustedData,
    HTTPRequestError,
    MalformedMetaData
}
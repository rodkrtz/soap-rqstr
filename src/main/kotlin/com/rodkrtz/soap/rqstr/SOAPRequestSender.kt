package com.rodkrtz.soap.rqstr

import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URLConnection
import javax.net.ssl.HttpsURLConnection

class SOAPRequestSender {

    fun send(soapRequest: SOAPRequest): SOAPResponse {

        val connection = getConnection(soapRequest)
        connection.doOutput = true
        connection.connectTimeout = soapRequest.connectionTimeout ?: 30000
        connection.readTimeout = soapRequest.readTimeout ?: 30000

        soapRequest.headers.forEach(connection::addRequestProperty)

        send(soapRequest.soapRequestMessage, connection)

        return getSOAPResponse(connection)
    }

    private fun readResponse(inputStream: InputStream): String {
        return InputStreamReader(inputStream).use { it.readText() }
    }

    private fun getSOAPResponse(connection: URLConnection) : SOAPResponse {
        connection as HttpURLConnection

        val headers = connection.headerFields
            .map { it.key to it.value.joinToString(",") }
            .toMap()

        return SOAPResponse(
            statusCode = connection.responseCode,
            soapResponseMessage = readResponse(connection.errorStream ?: connection.inputStream),
            headers = headers
        )
    }

    private fun send(soapRequestMessage: String, connection: URLConnection) {
        val writer = OutputStreamWriter(connection.outputStream)
        writer.write(soapRequestMessage)
        writer.flush()
        writer.close()
    }

    private fun getConnection(soapRequest: SOAPRequest): URLConnection {
        val isHttps = soapRequest.targetURL.protocol == "https"

        var connection = soapRequest.targetURL.openConnection()

        connection = if (isHttps) {
            connection as HttpsURLConnection
            connection.sslSocketFactory = soapRequest.sslSocketFactory
            connection
        } else {
            connection as HttpURLConnection
        }

        return connection
    }

}
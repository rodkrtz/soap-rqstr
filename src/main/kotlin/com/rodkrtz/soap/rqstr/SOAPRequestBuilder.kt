package com.rodkrtz.soap.rqstr

import java.net.URL
import javax.net.ssl.SSLSocketFactory

class SOAPRequestBuilder private constructor() {

    private lateinit var soapRequestMessage: String
    private lateinit var targetURL: URL
    private var headers: Map<String, String> = mutableMapOf()
    private var sslSocketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory
    private var readTimeout: Int = 30000
    private var connectionTimeout: Int = 30000

    companion object {
        fun getInstance(): SOAPRequestBuilder {
            return SOAPRequestBuilder()
        }
    }

    fun soapRequestMessage(soapRequestMessage: String) = apply { this.soapRequestMessage = soapRequestMessage }

    fun targetURL(targetURL: URL) = apply { this.targetURL = targetURL }

    fun headers(headers: Map<String, String>) = apply { this.headers = headers }

    fun sslSocketFactory(sslSocketFactory: SSLSocketFactory) = apply { this.sslSocketFactory = sslSocketFactory }

    fun readTimeout(readTimeout: Int) = apply { this.readTimeout = readTimeout }

    fun connectionTimeout(connectionTimeout: Int) = apply { this.connectionTimeout = connectionTimeout }

    fun build(): SOAPRequest {
        return SOAPRequest(
            soapRequestMessage = soapRequestMessage,
            targetURL = targetURL,
            headers = headers,
            sslSocketFactory = sslSocketFactory,
            readTimeout = readTimeout,
            connectionTimeout = connectionTimeout
        )
    }
}
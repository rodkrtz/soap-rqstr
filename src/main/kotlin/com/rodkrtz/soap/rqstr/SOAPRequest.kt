package com.rodkrtz.soap.rqstr

import java.net.URL
import javax.net.ssl.SSLSocketFactory

data class SOAPRequest(
    val soapRequestMessage: String,
    val headers: Map<String, String> = mutableMapOf(),
    val targetURL: URL,
    val sslSocketFactory: SSLSocketFactory? = null,
    val readTimeout: Int? = null,
    val connectionTimeout: Int? = null
)
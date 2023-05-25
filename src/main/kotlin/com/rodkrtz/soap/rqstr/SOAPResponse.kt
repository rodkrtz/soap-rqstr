package com.rodkrtz.soap.rqstr

data class SOAPResponse(
    val statusCode: Int,
    val soapResponseMessage: String,
    val headers: Map<String, String> = mutableMapOf()
)
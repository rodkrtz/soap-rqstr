package com.rodkrtz.soap.rqstr

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.net.URL
import javax.net.ssl.SSLSocketFactory

class SOAPRequestSenderTest {

    @Test
    fun teste() {
        val soapMessage = """
            <?xml version="1.0" encoding="utf-8"?>
            <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
              <soap:Body>
                <NumberToWords xmlns="http://www.dataaccess.com/webservicesserver/">
                  <ubiNum>500</ubiNum>
                </NumberToWords>
              </soap:Body>
            </soap:Envelope>
        """.trimIndent()

        val soapRequest = SOAPRequestBuilder.getInstance()
            .soapRequestMessage(soapMessage)
            .targetURL(URL("https://www.dataaccess.com/webservicesserver/NumberConversion.wso"))
            .headers(mutableMapOf("Content-Type" to "text/xml; charset=utf-8"))
            .sslSocketFactory(SSLSocketFactory.getDefault() as SSLSocketFactory)
            .connectionTimeout(30000)
            .readTimeout(30000)
            .build()

        val soapResponse = SOAPRequestSender.send(soapRequest)
        println(soapResponse)

        assertEquals(200, soapResponse.statusCode)
    }
}
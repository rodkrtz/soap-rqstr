package com.rodkrtz.soap.rqstr

import java.security.KeyStore
import java.security.SecureRandom
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

class SLLConfiguration(
    private val trustStore: ByteArray,
    private val trustStorePassword: String,
    private val clientCertificate: ByteArray,
    private val clientCertificatePassword: String
) {

    fun createSSLContext(): SSLContext {
        // Carregar o truststore
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(trustStore.inputStream(), trustStorePassword.toCharArray())

        // Criar o gerenciador de confiança
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(keyStore)

        // Carregar o certificado de cliente
        val clientKeyStore = KeyStore.getInstance("PKCS12")
        clientKeyStore.load(clientCertificate.inputStream(), clientCertificatePassword.toCharArray())

        // Criar o gerenciador de chave
        val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        keyManagerFactory.init(clientKeyStore, clientCertificatePassword.toCharArray())

        // Criar o contexto SSL
        val sslContext = SSLContext.getInstance("TLSv1.2")
        sslContext.init(keyManagerFactory.keyManagers, trustManagerFactory.trustManagers, SecureRandom())

        return sslContext
    }
}
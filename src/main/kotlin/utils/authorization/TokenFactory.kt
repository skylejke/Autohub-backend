package ru.point.utils.authorization

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import ru.point.feature.authorization.model.Token
import java.util.*

object TokenFactory {

    private const val EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 182

    private const val SECRET =
        "f9c82c34f3a86cbaad0ed3b48618c51b17fd1b44ae2ff4ab6efa7798a4db5d99f51209cc516262de87faac352f5c25252df21835e11546063009f810d8fe81791710f9914d74c8462ba8249acf53c7ccadf1a9e9b2cd0db8cc2aaacc2490abecf62a03047cef73a606a116da59ff11801ed846eee6751d0becfcf5a31f872816479004219a1b0565f5a985edcb512d771b5b18dd2a8e9e1406a8b7d9f5787efe319b3d0079ab93568758ca8672f48b2a36900f96006b7e087a2881ee9fed845b99a622a6ddc3576c0bbd9a014ce7497df2b654e151684675cba91912e6f9745b83431fd4991fabdaacef6e574370c859cd2d508867d26da7fc59ac410139470d"

    private val jwsAlgorithm = JWSAlgorithm.HS256

    fun generate(username: String): Token {
        val claimsSet = JWTClaimsSet.Builder()
            .subject(username)
            .expirationTime(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .build()

        val signedJWT = SignedJWT(JWSHeader(jwsAlgorithm), claimsSet)

        signedJWT.sign(MACSigner(SECRET.toByteArray()))
        return Token(signedJWT.serialize())
    }
}
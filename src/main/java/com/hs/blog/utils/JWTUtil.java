package com.hs.blog.utils;

import io.jsonwebtoken.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JWTUtil {

    private static final long EXPIRATION_TIME = 3_600_000; // 1 小时 = 60 * 60 * 1000 毫秒

    /**
     * 创建JWT令牌
     * @param secretKey 秘钥
     * @param claims map中key为"username", value为对应的值
     * @return 生成一个String类型的jwtToken
     */
    public static String createJWT(String secretKey, Map<String, Object> claims) {

        JwtBuilder builder = Jwts.builder();
        // 指定签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // header: JWT头部信息
        String jwtToken = builder.setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                // Payload: JWT主体信息
                .setClaims(claims)
                //设置过期时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                // signature: 签名算法和密钥
                .signWith(signatureAlgorithm, secretKey)
                .compact();
        return jwtToken;
    }

    /**
     * jwt令牌解密
     * @param secretKey 秘钥
     * @param token 即为jwt令牌
     * @return 返回解密的信息，包含过期时间等
     */
    public static Claims parseJWT(String secretKey, String token){
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJwt(token).getBody();
        return claims;
    }

}

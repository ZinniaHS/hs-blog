package com.hs.blog.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;


import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JWTUtil {

    // 密钥，用于签名和验证JWT令牌
    private static final String secretKey = "hs-blog-very-long-secret-key-1234567890abcdef";
    // 过期时间，单位毫秒：// 1 小时 = 60 * 60 * 1000 毫秒
    private static final long EXPIRATION_TIME = 3_600_000;

    private static final SecretKey key =
            Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));


    /**
     * 创建JWT令牌
     * @param claims map中key为"username", value为对应的值
     * @return 生成一个String类型的jwtToken
     */
    public static String createJWT(Map<String, Object> claims) {
        /**
         * JWT由三个部分组成
         * 1. Header: 头部信息
         *    类型（type）：通常为 JWT，表明该令牌是一个 JSON Web Token。
         *    算法（algorithm）：指定签名所使用的算法，如 HS256、HS512、RS256 等。
         * 2. Payload: 有效载荷
         *     JWT 的第二部分包含了声明（Claims），即传递的信息。声明分为三种类型：
         *   2.1 注册声明（Registered Claims）
         *     这些是 JWT 规定的声明，具有特定的含义。常见的有：
         *     iss（issuer）：发行者。
         *     sub（subject）：主题（例如用户ID）。
         *     exp（expiration）：过期时间（Unix时间戳）。
         *     aud（audience）：受众。
         *     iat（issued at）：签发时间。
         *     jti（JWT ID）：JWT 的唯一标识符。
         *   2.2 公共声明（Public Claims）：可以自定义的声明，避免与 JWT 注册声明冲突，可以使用 URI 命名空间。
         *   2.3 私有声明（Private Claims）：在两个系统之间共享的私有声明，通常是应用于内部数据传递的声明。
         * 3. Signature: 签名
         *     为了防止数据被篡改，JWT 会使用头部和载荷的内容以及一个密钥进行签名，确保数据的完整性。
         */
        JwtBuilder builder = Jwts.builder();
        // 指定签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        System.out.println(signatureAlgorithm.getValue());
        // header: JWT头部信息
        String jwtToken = builder.setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", signatureAlgorithm.getValue())
                // Payload: JWT主体信息，注册声明
                .setClaims(claims)
                // 设置签发时间：当前时间
                .setIssuedAt(new Date())
                // 设置过期时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                // signature: 签名算法signatureAlgorithm 和 密钥secretKey
                .signWith(signatureAlgorithm, key)
                .compact();
        return jwtToken;
    }

    /**
     * jwt令牌解密
     * @param token 即为jwt令牌
     * @return 返回解密的信息，包含过期时间等
     */
    public static Claims parseJWT(String token){
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

}

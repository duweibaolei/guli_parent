package com.dwl.common_utils.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.ApiModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * JWT令牌工具类
 */
@ApiModel("JWT令牌工具类")
public class JwtUtil {

    /**
     * 过期时间
     */
    public static final long EXPIRE = 1000 * 60 * 60 * 24;

    /**
     * 密钥
     */
    public static final String APP_SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLPHO";

    /**
     * iss：发行人
     * exp：到期时间
     * sub：主题
     * aud：用户
     * nbf：在此之前不可用
     * iat：发布时间
     * jti：JWT ID用于标识该JWT
     * 获取 JWT令牌
     *
     * @param id
     * @param nickname
     * @return
     */
    public static String getJwtToken(String id, String nickname) {
        String JwtToken = Jwts.builder()
                // 设置JWT头信息
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                // 设置有效载荷
                .setSubject("guli-user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .claim("id", id)
                .claim("nickname", nickname)
                // 设置加密方式
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();
        return JwtToken;
    }

    /**
     * 判断token是否存在与有效
     *
     * @param jwtToken
     * @return
     */
    public static boolean checkToken(String jwtToken) {
        if (StringUtil.isEmpty(jwtToken)) return false;
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断token是否存在与有效
     *
     * @param request
     * @return
     */
    public static boolean checkToken(HttpServletRequest request) {
        try {
            System.out.println("request:" + request);
            String jwtToken = request.getHeader("token");
            if (StringUtil.isEmpty(jwtToken)) return false;
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据token获取会员id
     *
     * @param request
     * @return
     */
    public static String getMemberIdByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("token");
        if (StringUtil.isEmpty(jwtToken)) return "";
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody();
        return (String) claims.get("id");
    }
}

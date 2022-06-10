package com.yaoxiong.retail.base.component;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

@Component
public class TokenManager {
    //token effective duration
    private long tokenEcpiration = 24*60*60*1000;
    //key
    private String tokenSignKey = "123456";

    public String createToken(String username){
        String token = Jwts.builder().setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis()+tokenEcpiration))
                .signWith(SignatureAlgorithm.HS512, tokenSignKey).compressWith(CompressionCodecs.GZIP).compact();
        System.out.println("token = " + token);
        return token;
    }

    // Get user information according to token string
    public String getUserInfoFromToken(String token) {
        String userinfo = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token).getBody().getSubject();
        return userinfo;
    }
    //3 delete token
    public void removeToken(String token) { }

    public static void main(String[] args) {
       String str =  new TokenManager().getUserInfoFromToken("eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJSSkzJzcxT0lFKrShQsjI0MzUysTA3sTSpBQCUBoQ0IAAAAA.DxvcPJd6MUxpFtJShu-Yku6FB6aCUGNukroNfPQXEk8UMdJ8rxcMajd_OTIk24t1RXDWspHesn8QAFrcxIJNvA");
        System.out.println("args = " + str);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appsdeveloperblog.photoapp.api.users.shared;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    Environment environment;

    public JwtUtil(Environment environment) {
        this.environment = environment;
    }

    public String getUserId(String header) {
        String token = header.replace(environment.getProperty("authorization.token.header.prefix"), "");

        Claims claims = Jwts.parser().setSigningKey(environment.getProperty("token.secret"))
                .parseClaimsJws(token).getBody();

        if (claims == null) {
            return null;
        }

        return (String) claims.get("userId");
    }
}

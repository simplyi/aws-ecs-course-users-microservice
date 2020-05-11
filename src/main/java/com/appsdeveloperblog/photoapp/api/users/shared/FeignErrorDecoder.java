/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appsdeveloperblog.photoapp.api.users.shared;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {
    
    Environment environment;
    
    @Autowired
    public FeignErrorDecoder(Environment environment)
    {
        this.environment = environment;
    }
    
    @Override
    public Exception decode(String methodKey, Response response) {
 
        switch (response.status()){
            case 400:
                //return new BadRequestException();
                System.out.println("Status code " + response.status() + ", methodKey = " + methodKey);
            case 404:
            {
                if(methodKey.contains("getAlbums"))
                {
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()), environment.getProperty("albums.exceptions.albums-not-found"));
                    //System.out.println("getAlbums Status code " + response.status() + ", methodKey = " + methodKey);
                }
                
            }
                // System.out.println("Status code " + response.status() + ", methodKey = " + methodKey);
                //return new NotFoundException();
            default:
                return new Exception(response.reason());
        } 
    }
    
}

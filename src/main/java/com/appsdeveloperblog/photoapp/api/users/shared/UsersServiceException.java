/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appsdeveloperblog.photoapp.api.users.shared;

/**
 *
 * @author skargopolov
 */
public class UsersServiceException extends RuntimeException {
    
    public UsersServiceException(){}
    
    public UsersServiceException(String message)
    {
        super(message);
    }
    
}

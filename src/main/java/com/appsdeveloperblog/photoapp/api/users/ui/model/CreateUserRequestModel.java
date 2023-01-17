/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appsdeveloperblog.photoapp.api.users.ui.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 *
 * @author skargopolov
 */
public class CreateUserRequestModel {
    
    @NotNull(message="First name cannot be null")
    @Size(min=2, message = "First name must not be less than 2 characters")
    private String firstName;
    
	
    @NotNull(message="Last name cannot be null")
    @Size(min=2, message = "Last name must not be less than 2 characters")
    private String lastName;
    
    @NotNull(message="Password cannot be null")
    @Size(min=8,max=16, message="Password must be equal or grater than 8 characters and less than 16 chaeracters")
    private String password;
    
    @NotNull(message="Email cannot be null")
    @Email
    private String email;

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
}

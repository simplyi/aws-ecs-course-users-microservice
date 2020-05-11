/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appsdeveloperblog.photoapp.api.users.ui.controllers;

import com.appsdeveloperblog.photoapp.api.users.ui.model.CreateUserRequestModel;
import com.appsdeveloperblog.photoapp.api.users.service.UsersService;
import com.appsdeveloperblog.photoapp.api.users.shared.UserDto;
import com.appsdeveloperblog.photoapp.api.users.shared.UsersServiceException;
import com.appsdeveloperblog.photoapp.api.users.ui.model.UserResponseModel;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
@RefreshScope
public class UsersController {
    
    @Value("${token.secret}")
    String token;

    @Autowired
    UsersService usersService;
    
    @Autowired
    Environment environment;

    @Value("${server.port}")
    private String port;

    @GetMapping("/status/check")
    public String status() {
        return "Working on port " + port + " with token " + token + ". Token from environment " + environment.getProperty("token.secret");
    }

    @PostMapping(
            consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, 
            produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<UserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel requestModel) {
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(requestModel, UserDto.class);
        
        UserDto createdUserDetails = usersService.createUser(userDto);
        
        UserResponseModel returnValue = modelMapper.map(createdUserDetails, UserResponseModel.class);
      
        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }
    
    @GetMapping(value="/{userId}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId,
            @RequestHeader(value="Authorization") String authorization) {
   
        UserDto userDto = usersService.getUserByUserId(userId);
        
        UserResponseModel returnValue = new ModelMapper().map(userDto, UserResponseModel.class);
        
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
    
    @GetMapping()
    public ResponseEntity<List<CreateUserRequestModel>> getUsers(
            @RequestHeader(value="Authorization") String authorization) {
       
        List<UserDto> userDtoList = usersService.getUsers();
        
        Type listType = new TypeToken<List<CreateUserRequestModel>>(){}.getType();
 
        List<CreateUserRequestModel> returnValue = new ModelMapper().map(userDtoList, listType);
        
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
    
    
    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable("userId") String userId,
            @RequestHeader(value="Authorization") String authorization) {
 
        usersService.deleteUser(userId, authorization);
        
        
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

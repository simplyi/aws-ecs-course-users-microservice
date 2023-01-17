/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appsdeveloperblog.photoapp.api.users.ui.controllers;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.photoapp.api.users.service.UsersService;
import com.appsdeveloperblog.photoapp.api.users.shared.UserDto;
import com.appsdeveloperblog.photoapp.api.users.ui.model.AlbumResponseModel;
import com.appsdeveloperblog.photoapp.api.users.ui.model.CreateUserRequestModel;
import com.appsdeveloperblog.photoapp.api.users.ui.model.UserResponseModel;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersController {

	@Value("${token.secret}")
	String token;

	@Autowired
	UsersService usersService;

	@Autowired
	Environment environment;

	@Value("${server.port}")
	private String port;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@PostMapping
	public ResponseEntity<UserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel requestModel) {
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(requestModel, UserDto.class);

		UserDto createdUserDetails = usersService.createUser(userDto);

		UserResponseModel returnValue = modelMapper.map(createdUserDetails, UserResponseModel.class);
		log.info("User created with id: " + returnValue.getUserId());

		return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
	}

	@GetMapping("/{userId}")
	@PostAuthorize("principal == returnObject.body.userId")
	public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId,
			@RequestHeader("Authorization") String authorization,
			@RequestParam(value = "fields", required = false) String fields) {

		UserDto userDto = usersService.getUserByUserId(userId);

		UserResponseModel returnValue = new ModelMapper().map(userDto, UserResponseModel.class);

		// Include albums if requested
		if (fields != null) {
			String[] includeFields = fields.split(",");
			for (String field : includeFields) {
				if (field.trim().equalsIgnoreCase("albums")) {
					List<AlbumResponseModel> albums = usersService.getUserAlbums(authorization);
					returnValue.setAlbums(albums);
					break;
				}
			}
		}

		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}

	@GetMapping()
	public ResponseEntity<List<UserResponseModel>> getUsers(@RequestHeader("Authorization") String authorization) {

		List<UserDto> userDtoList = usersService.getUsers();

		Type listType = new TypeToken<List<UserResponseModel>>() {
		}.getType();

		List<UserResponseModel> returnValue = new ModelMapper().map(userDtoList, listType);
		log.info("Total users in database table: " + returnValue.size());

		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity deleteUser(@PathVariable("userId") String userId,
			@RequestHeader("Authorization") String authorization) {

		usersService.deleteUser(userId, authorization);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/status/check")
	public String status(@RequestHeader("Authorization") String authorizationHeader) {
		String returnValue = "Working on port " + port + " with token " + token + ". Token from environment "
				+ environment.getProperty("token.secret") + "authorizationHeader = " + authorizationHeader
				+ ". My application environment = " + environment.getProperty("myapplication.environment");
		log.info(returnValue);
		return returnValue;
	}

	@GetMapping("/ip")
	public String getIp() {
		String returnValue;

		try {
			InetAddress ipAddr = InetAddress.getLocalHost();
			returnValue = ipAddr.getHostAddress();
		} catch (UnknownHostException ex) {
			returnValue = ex.getLocalizedMessage();
		}

		return returnValue;
	}

}

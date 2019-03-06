package com.appStore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appStore.dao.UserMapper;
import com.appStore.entity.User;
import com.appStore.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	private UserMapper userMapper;
	  
	@Override
	public User userLogin(User user) {
		User record = userMapper.findByUser(user);
		return record;
	}
}

package com.apabi.r2k.security.service;

import java.util.List;

import com.apabi.r2k.security.model.AuthEntity;

public interface AuthService {

	public List<AuthEntity> getAuthTree(String[] roleIds) throws Exception;
}

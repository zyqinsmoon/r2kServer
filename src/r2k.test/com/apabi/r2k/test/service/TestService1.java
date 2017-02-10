package com.apabi.r2k.test.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.apabi.r2k.test.dao.TestDao;
import com.apabi.r2k.test.model.Test;

@Service
public class TestService1 {

	@Resource
	private TestDao testDao;
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void save(Test test){
		testDao.save(test);
	}
}

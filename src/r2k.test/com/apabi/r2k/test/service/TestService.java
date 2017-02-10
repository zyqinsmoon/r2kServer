package com.apabi.r2k.test.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.apabi.r2k.test.dao.TestDao;
import com.apabi.r2k.test.model.Test;

@Service
public class TestService {

	@Resource
	private TestDao testDao;
	@Resource
	private TestDao testDao1;

	@Resource
	private TestService1 testService1;
	
	public TestDao getTestDao() {
		return testDao;
	}

	public void setTestDao(TestDao testDao) {
		this.testDao = testDao;
	}

	public TestDao getTestDao1() {
		return testDao1;
	}

	public void setTestDao1(TestDao testDao1) {
		this.testDao1 = testDao1;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void save(Test test) {
		testDao.save(test);
	}

	public List<Test> list() {
		return testDao.list();
	}
	
	
	public List<Test> nameList(String value){
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", value);
		return testDao.nameList(map);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveTests(){
		Test t1 = new Test();
		t1.setName("t4");
		t1.setContent("content4");
		testDao.save(t1);
		Test t2 = new Test();
		t2.setContent("content5");
		testDao1.save(t2);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveServiceTest(){
		Test t1 = new Test();
		t1.setName("t5");
		t1.setContent("content5");
		testDao.save(t1);
		Test t2 = new Test();
		t2.setContent("content6");
		testService1.save(t2);
	}
	
	
	
}

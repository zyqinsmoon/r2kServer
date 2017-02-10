package com.apabi.r2k.test.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.apabi.r2k.test.model.Test;

@Repository
public class TestDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	
	public List<Test> list(){
		return sqlSessionTemplate.selectList("com.apabi.r2k.test.model.Test.list");
	}
	
	public List<Test> nameList(Map<String, String> map){
		return sqlSessionTemplate.selectList("com.apabi.r2k.test.model.Test.namelist",map);
	}
	
	public void save(Test test){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", test.getName());
		map.put("content", test.getContent());
		sqlSessionTemplate.insert("com.apabi.r2k.test.model.Test.insert", map);
	}
}

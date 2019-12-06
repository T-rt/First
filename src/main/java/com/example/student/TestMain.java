package com.example.student;

import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
 
 
public class TestMain {
 
	public static void main(String[] args) throws IOException {
		    String resource = "mybatis/mybatis-config.xml";   
	        InputStream inputStream = Resources.getResourceAsStream(resource);
	        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
	        SqlSession sqlSession = sqlSessionFactory.openSession();
	        String statement = "mapper.userMapper.getUser";   
	        People p = sqlSession.selectOne(statement, 1);   
	        sqlSession.close();
	        System.out.println(p);
 
 
	}
 
}

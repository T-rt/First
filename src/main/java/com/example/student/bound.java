package com.example.student;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Controller
public class bound {

	@Autowired
	StudentApplication _app;
	Map<String, Object> map = new TreeMap<String, Object>();

	public List<People> GetList() throws IOException {
		List<People> list = new ArrayList<People>();
		String resource = "mybatis/mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		String statement = "mapper.userMapper.getUser";
		list = sqlSession.selectList(statement);
		sqlSession.close();
		return list;
	}

	@RequestMapping(value = "/")
	public String Login() {
		return "login";
	}

	// @RequestMapping(value = "login",method = RequestMethod.POST)
	@PostMapping(value = "/login")
	public String Login(@RequestBody(required = false) String params, @RequestParam("number") String number,
			@RequestParam("password") String password, Map<String, Object> m, HttpSession session) throws IOException {
		String[] pa = params.split("&");
		String[] n = pa[0].split("=");
		String[] p = pa[1].split("=");
		List<People> list = new ArrayList<People>();
		list = GetList();
		for (People s : list) {
			if (!StringUtils.isEmpty(number) && n[1].length() == 3 && p[1].equals(s.getPassword())) {
				if (n[1].equals(Integer.toString(s.getId()))) {
					session.setAttribute("login", number);
					map.put(number, password);
					m.put(number, password);
					return "roottotal";
				} else {
					m.put("msg", "用户名或密码错误");
					return "login";
				}
			} else if (!StringUtils.isEmpty(number) && n[1].equals(Integer.toString(s.getId()))) {
				if (p[1].equals(s.getPassword())) {
					session.setAttribute("login", number);
					map.put(number, password);
					m.put(number, password);
					return "student";
				} else {
					m.put("msg", "用户名或密码错误");
					return "login";
				}
			}
		}
		return "register";
	}

	@RequestMapping(value = "/register")
	public String Register() {
		return "register";
	}

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String Reg(@RequestBody(required = false) String params, HttpServletResponse response) throws IOException {
		String[] p = params.split("&");
		String[] n = p[0].split("=");
		String[] pa = p[1].split("=");
		String[] na = p[2].split("=");
		String[] s = p[3].split("=");
		String[] a = p[4].split("=");
		String resource = "mybatis/mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		String statement = "mapper.userMapper.insert";
		String statement1 = "mapper.userMapper.selectID";
		People peo = new People();
		// 注册时不符合要求注册
		int x = sqlSession.selectOne(statement1, Integer.parseInt(n[1]));
		if (x == 1) {
			//页面弹出alert
			response.setContentType("text/html; charset=UTF-8"); // 转码
			PrintWriter out = response.getWriter();
			out.flush();
			out.println("<script>");
			out.println("alert('此用户名已存在，请重新输入！');");
			out.println("history.back();");
			out.println("</script>");
			return "register";
		} else {
			if (n[1].length() == 10
					&& (URLDecoder.decode(s[1], "utf-8").equals("男") || URLDecoder.decode(s[1], "utf-8").equals("女"))
					&& a[1].length() == 2) {
				System.out.println("2222");
				peo.setId(Integer.parseInt(n[1]));
				String name = URLDecoder.decode(na[1], "utf-8");
				peo.setUsername(name);
				peo.setAge(Integer.parseInt(a[1]));
				String sex = URLDecoder.decode(s[1], "utf-8");
				peo.setSex(sex);
				peo.setPassword(pa[1]);
				sqlSession.insert(statement, peo);
				sqlSession.commit();
				sqlSession.close();
				return "login";
			} else {
				return "register";
			}

		}

	}

	@RequestMapping(value = "student", method = RequestMethod.POST)
	@ResponseBody
	public String Student(HttpSession session) throws IOException {
		String resource = "mybatis/mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		String statement = "mapper.userMapper.showUser";
		People p = new People();
		for (String key : map.keySet()) {
			p = sqlSession.selectOne(statement, key);
		}
		System.out.println(p);
		sqlSession.close();
		return "success" + "-------->" + p;
	}

	@PostMapping(value = "/roottotal")
	public String Root(@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "insert", required = false) String insert,
			@RequestParam(value = "delete", required = false) String delete,
			@RequestParam(value = "change", required = false) String change,
			@RequestParam(value = "export", required = false) String export,
			@RequestParam(value = "upload", required = false) String upload) {
		if (search != null) {
			return "search.html";
		}
		if (insert != null) {
			return "insert.html";
		}
		if (delete != null) {
			return "delete.html";
		}
		if (change != null) {
			return "change.html";
		}
		if (export != null) {
			return "export.html";
		}
		if (upload != null) {
			return "upload.html";
		}
		return null;
	}

	@RequestMapping(value = "insert", method = RequestMethod.POST)
	@ResponseBody
	public List<People> insert(@RequestBody(required = false) String params,
			@RequestParam(value = "insert", required = false) String insert,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "sex", required = false) String sex,
			@RequestParam(value = "age", required = false) String age,
			@RequestParam(value = "grade", required = false) String grade, HttpSession session) throws IOException {
		String[] p = params.split("&");
		List<People> list = new ArrayList<People>();
		String resource = "mybatis/mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		if (URLDecoder.decode(p[6].split("=")[1], "utf-8").equals("添加")) {
			String statement = "mapper.userMapper.insertRoot";
			String statement1 = "mapper.userMapper.showAllUser";
			String statement2 = "mapper.userMapper.selectID";
			int result = sqlSession.selectOne(statement2, Integer.parseInt(p[0].split("=")[1]));
			System.out.println(result);
			People peo = new People();
			if (result >= 1) {
				return null;
			} else {
				peo.setId(Integer.parseInt(p[0].split("=")[1]));
				peo.setAge(Integer.parseInt(age));
				peo.setGrade(Integer.parseInt(grade));
				peo.setPassword(password);
				peo.setSex(sex);
				peo.setUsername(name);
				sqlSession.insert(statement, peo);
				sqlSession.commit();
				list = sqlSession.selectList(statement1);
				sqlSession.close();
				return list;
			}
		}
		return null;
	}

	@RequestMapping(value = "search", method = RequestMethod.POST)
	@ResponseBody
	public List<People> Search(@RequestBody(required = false) String params,
			@RequestParam(value = "search", required = false) String search, HttpSession session) throws IOException {
		String[] p = params.split("&");
		List<People> list = new ArrayList<People>();
		String resource = "mybatis/mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		if (URLDecoder.decode(p[0].split("=")[1], "utf-8").equals("查看")) {
			String statement = "mapper.userMapper.showAllUser";
			list = sqlSession.selectList(statement);
			sqlSession.close();
			return list;
		}
		return null;
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public List<People> delete(@RequestBody(required = false) String params,
			@RequestParam(value = "delete", required = false) String delete, HttpSession session) throws IOException {
		String[] p = params.split("&");
		List<People> list = new ArrayList<People>();
		String resource = "mybatis/mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sqlSessionFactory.openSession();

		if (URLDecoder.decode(p[1].split("=")[1], "utf-8").equals("删除")) {
			String str1 = URLDecoder.decode(p[0].split("=")[1], "utf-8");
			String statement = "mapper.userMapper.rdelete";
			String statement1 = "mapper.userMapper.showAllUser";
			sqlSession.delete(statement, str1);
			list = sqlSession.selectList(statement1);
			sqlSession.commit();
			sqlSession.close();
			return list;
		}
		return null;
	}

	@RequestMapping(value = "changed", method = RequestMethod.POST)
	@ResponseBody
	public List<People> change(@RequestBody(required = false) String params,
			@RequestParam(value = "change", required = false) String change,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "sex", required = false) String sex,
			@RequestParam(value = "age", required = false) String age,
			@RequestParam(value = "grade", required = false) String grade, HttpSession session) throws IOException {
		String[] p = params.split("&");
		List<People> list = new ArrayList<People>();
		String resource = "mybatis/mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		if (URLDecoder.decode(p[1].split("=")[1], "utf-8").equals("修改")) {
			String statement = "mapper.userMapper.rupdate";
			String statement1 = "mapper.userMapper.showAllUser";
			People peo = new People();
			peo.setId(Integer.parseInt(p[0].split("=")[1]));
			peo.setAge(Integer.parseInt(age));
			peo.setGrade(Integer.parseInt(grade));
			peo.setPassword(password);
			peo.setSex(sex);
			peo.setUsername(name);
			sqlSession.update(statement, peo);
			sqlSession.commit();
			list = sqlSession.selectList(statement1);
			sqlSession.close();
			return list;
		}
		return null;
	}

	@RequestMapping(value = "export", method = RequestMethod.POST)
	@ResponseBody
	public String Export(@RequestBody(required = false) String params,
			@RequestParam(value = "export", required = false) String export, HttpSession session,
			HttpServletResponse response) throws IOException {
		String[] p = params.split("&");
		String[] name = p[0].split("=");
		ExcelUtil e = new ExcelUtil();
		e.GetExcel(name[1], response);
		return null;
	}

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@ResponseBody
	public String Upload(@RequestParam(value = "upload") MultipartFile file, HttpSession session) throws Exception {
		int result = 0;
		try {
			ExcelUtil e = new ExcelUtil();
			result = e.addUser(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result > 0) {
			return "excel文件数据导入成功！";
		} else {
			return "excel数据导入失败！";
		}
	}

}

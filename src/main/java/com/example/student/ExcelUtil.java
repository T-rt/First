package com.example.student;

import java.io.*;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExcelUtil implements UserService {

	public List<People> GetExcel(String filename, HttpServletResponse response) throws IOException {
		List<People> list = new ArrayList<People>();
		bound b = new bound();
		list = b.GetList();
		SXSSFWorkbook wb = new SXSSFWorkbook();
		SXSSFSheet sheet = wb.createSheet("member");
		SXSSFRow row = null;
		row = sheet.createRow(0);
		row.setHeight((short) (26.25 * 20));
		row.createCell(0).setCellValue("用户信息表");
		CellRangeAddress rowRegion = new CellRangeAddress(0, 0, 0, 3);
		sheet.addMergedRegionUnsafe(rowRegion);

		row = sheet.createRow(1);
		row.setHeight((short) (22.5 * 20));
		row.createCell(0).setCellValue("id");
		row.createCell(1).setCellValue("name");
		row.createCell(2).setCellValue("sex");
		row.createCell(3).setCellValue("grade");
		row.createCell(4).setCellValue("age");
		row.createCell(5).setCellValue("password");
		for (int i = 0; i < list.size(); ++i) {
			row = sheet.createRow(i + 2);
			People p = list.get(i);
			row.createCell(0).setCellValue(p.getId());
			row.createCell(1).setCellValue(p.getname());
			row.createCell(2).setCellValue(p.getSex());
			row.createCell(3).setCellValue(p.getGrade());
			row.createCell(4).setCellValue(p.getAge());
			row.createCell(5).setCellValue(p.getPassword());
		}
		sheet.trackAllColumnsForAutoSizing();
		sheet.setDefaultRowHeight((short) (16.5 * 20));
		for (int i = 0; i <= 13; ++i) {
			sheet.autoSizeColumn(i);
		}

		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		OutputStream os = response.getOutputStream();
		response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");

		wb.write(os);
		os.flush();
		os.close();
		wb.close();
		return null;
	}

	// public int Import(MultipartFile file) throws Exception {
	//
	// }

	@Override
	public int addUser(MultipartFile file) throws Exception {
		// TODO Auto-generated method stub
		int result = 0;
		List<People> userList = new ArrayList<>();
		String fileName = file.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		InputStream ins = file.getInputStream();
		Workbook wb = null;

		if (suffix.equals("xlsx")) {

			wb = new XSSFWorkbook(ins);

		} else {
			wb = new HSSFWorkbook(ins);
		}

		Sheet sheet = wb.getSheetAt(0);

		if (null != sheet) {

			for (int line = 2; line <= sheet.getLastRowNum(); line++) {

				People user = new People();

				Row row = sheet.getRow(line);

				if (null == row) {
					continue;
				}
				row.getCell(0).setCellType(CellType.STRING);
				String id = row.getCell(0).getStringCellValue();
				row.getCell(1).setCellType(CellType.STRING);
				String name = row.getCell(1).getStringCellValue();
				row.getCell(2).setCellType(CellType.STRING);
				String sex = row.getCell(2).getStringCellValue();
				row.getCell(3).setCellType(CellType.STRING);
				String grade = row.getCell(3).getStringCellValue();
				row.getCell(4).setCellType(CellType.STRING);
				String age = row.getCell(4).getStringCellValue();
				row.getCell(5).setCellType(CellType.STRING);
				String password = row.getCell(5).getStringCellValue();

				user.setId((int) Double.parseDouble(id));
				user.setUsername((String) name);
				user.setGrade((int) Float.parseFloat(grade));
				user.setAge((int) Float.parseFloat(age));
				user.setSex((String) sex);
				user.setPassword((String) password);

				userList.add(user);

			}

			for (People userInfo : userList) {
				int id = userInfo.getId();
				String resource = "mybatis/mybatis-config.xml";
				InputStream inputStream = Resources.getResourceAsStream(resource);
				SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
				SqlSession sqlSession = sqlSessionFactory.openSession();
				String statement1 = "mapper.userMapper.selectUser";
				String statement2 = "mapper.userMapper.addUser";
				String statement3 = "mapper.userMapper.updateUser";
				int c = sqlSession.selectOne(statement1, id);
				if (0 == c) {
					sqlSession.insert(statement2, userInfo);
					sqlSession.commit();
					result = 1;
				} else {
					sqlSession.update(statement3, userInfo);
					sqlSession.commit();
					result = 1;
				}
				sqlSession.close();
			}
		}
		wb.close();
		return result;
	}

}

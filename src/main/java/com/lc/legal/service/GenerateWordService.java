package com.lc.legal.service;

import com.lc.legal.common.bean.FileEntity;
import com.lc.legal.common.constants.CommonConstants;
import com.lc.legal.common.constants.ExcelConstants;
import com.lc.legal.common.constants.FileConstants;
import com.lc.legal.common.utils.CommonUtils;
import com.lc.legal.common.utils.ExcelUtils;
import com.lc.legal.common.utils.FileUtils;
import com.lc.legal.common.utils.IdCardUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liangc
 * @version 1.0
 * @Description: 生成word service类
 * @date: 2021/7/24 18:36
 */
@Service
public class GenerateWordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateWordService.class);

    /**
     * 根据sheet页内容生成文件流List
     *
     * @param sheet sheet页
     * @return 文件流List
     */
    public List<FileEntity> getWordStreamList(XSSFSheet sheet) {
        List<FileEntity> outputStreamList = new ArrayList<>();

        String sheetName = sheet.getSheetName();
        List<Map<String, String>> dataList = this.getDataListFromSheet(sheet);

        String templatePath;

        // 获取模板文件位置
        if (CommonUtils.isWindows()) {
            templatePath = "E:\\opt\\cloud\\template\\" + sheetName + ".docx";
        } else {
            templatePath = "/opt/cloud/template/" + sheetName + ".docx";
        }

        String finalTemplatePath = templatePath;
        dataList.forEach(map -> {
            try {
                String tempFileName = sheetName + FileConstants.FILE_UNDERLINE + map.get(ExcelConstants.EXCEL_TITLE_ROOM_NUMBER)
                        + FileConstants.FILE_UNDERLINE + map.get(ExcelConstants.EXCEL_TITLE_PERSON_NAME)
                        + FileConstants.FILE_SPOT + FileConstants.FILE_WORD_TYPE;
                ByteArrayOutputStream byteArrayOutputStream = FileUtils.generateDoc(finalTemplatePath, map);
                outputStreamList.add(new FileEntity(tempFileName, byteArrayOutputStream));
            } catch (IOException e) {
                LOGGER.error("使用模板文件生成失败！", e);
            }
        });

        return outputStreamList;
    }

    /**
     * 获取sheet中的数据
     *
     * @param sheet sheet页
     * @return sheet中的数据
     */
    private List<Map<String, String>> getDataListFromSheet(XSSFSheet sheet) {
        ArrayList<Map<String, String>> dataList = new ArrayList<>();

        //默认第一行为标题行，i = 0
        XSSFRow titleRow = sheet.getRow(0);

        // 循环获取每一行数据
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            XSSFRow row = sheet.getRow(i);
            LinkedHashMap<String, String> dataMap = new LinkedHashMap<>();

            StringBuffer buffer = new StringBuffer();
            // 读取每一格内容
            for (int index = 0; index < row.getPhysicalNumberOfCells(); index++) {
                XSSFCell titleCell = titleRow.getCell(index);
                String titleName = ExcelUtils.getString(titleCell).trim();
                XSSFCell cell = row.getCell(index);

                String cellValue = "";

                try {
                    cellValue = ExcelUtils.getString(cell).trim();
                } catch (IllegalStateException e) {
                    LOGGER.error("{} 行 {} 列数据异常！", i + 1, index + 1);
                }


                // 如果单元格内容为空，设置内容为不详
                if (CommonConstants.EMPTY_STRING.equals(cellValue)) {
                    cellValue = CommonConstants.UNKNOWN_INFO;
                }

                // 内容特殊处理
                switch (titleName) {
                    case ExcelConstants.EXCEL_TITLE_ROOM_NUMBER: {
                        String[] strArr = cellValue.split(FileConstants.FILE_BAR);
                        cellValue = strArr[0] + ExcelConstants.EXCEL_CONTENT_DONG + strArr[1] + ExcelConstants.EXCEL_CONTENT_ROOM;
                        dataMap.put(ExcelUtils.getString(titleCell), cellValue);
                        break;
                    }
                    case ExcelConstants.EXCEL_TITLE_ARREARS_PERIOD: {
                        String[] strArr = cellValue.split(FileConstants.FILE_BAR);
                        String startDate = strArr[0].replace(FileConstants.FILE_SPOT, ExcelConstants.EXCEL_CONTENT_YEAR) + ExcelConstants.EXCEL_CONTENT_MONTH;
                        String endDate = strArr[1].replace(FileConstants.FILE_SPOT, ExcelConstants.EXCEL_CONTENT_YEAR) + ExcelConstants.EXCEL_CONTENT_MONTH;
                        dataMap.put(ExcelConstants.EXCEL_TITLE_START_DATE, startDate);
                        dataMap.put(ExcelConstants.EXCEL_TITLE_END_DATE, endDate);
                        break;
                    }
                    case ExcelConstants.EXCEL_TITLE_PERSON_ID:
                        // 身份证号特殊处理
                        if (!IdCardUtils.isValid(cellValue)) {
                            cellValue = CommonConstants.UNKNOWN_INFO;

                            // 根据根据身份证号获取性别
                            String sex = CommonConstants.UNKNOWN_INFO;
                            dataMap.put(ExcelConstants.EXCEL_TITLE_SEX, sex);

                            // 根据身份证号获取出生日期
                            String birthDay = CommonConstants.UNKNOWN_INFO;
                            dataMap.put(ExcelConstants.EXCEL_TITLE_BIRTHDAY, birthDay);
                        } else {
                            // 根据根据身份证号获取性别
                            String sex = IdCardUtils.getSex(cellValue);
                            dataMap.put(ExcelConstants.EXCEL_TITLE_SEX, sex);

                            // 根据身份证号获取出生日期
                            String birthDay = IdCardUtils.getBirthday(cellValue);
                            dataMap.put(ExcelConstants.EXCEL_TITLE_BIRTHDAY, birthDay);
                        }

                        dataMap.put(titleName, cellValue);
                        break;
                    default:
                        dataMap.put(titleName, cellValue);
                        break;
                }
            }

            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                buffer.append(entry.getKey()).append(": ").append(entry.getValue()).append(" ");
            }
            LOGGER.info(buffer.toString());
            if (dataMap.isEmpty()) {
                continue;
            }
            dataList.add(dataMap);
        }

        return dataList;
    }
}

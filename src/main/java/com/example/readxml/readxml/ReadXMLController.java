package com.example.readxml.readxml;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ReadXMLController {

    @GetMapping("/readxml")
    public List<Employee> displayData(@RequestParam String filepath) {

        List<Employee> empList;
        try {

            FileInputStream fileInputStream = new FileInputStream(filepath);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet("genderDetail");
            empList = new ArrayList<>();
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                Employee employee = new Employee();
                employee.setName(row.getCell(0).getStringCellValue());
                employee.setAge((int) row.getCell(1).getNumericCellValue());
                empList.add(employee);
            }
//======================================>> Creating LineChart <<======================================

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for(Employee employee : empList)
            {
                dataset.addValue(employee.getAge(), "Age", employee.getName());
            }
            JFreeChart lineChart = ChartFactory.createLineChart("Employee Details",
                    "Name",
                    "Age",
                    dataset);

            ChartUtils.saveChartAsJPEG(new File("C://Users/shash/Shashank/lineChart.jpg"), lineChart, 800, 600);

//======================================>> Creating BarChart <<======================================

            JFreeChart barChart = ChartFactory.createBarChart("Employee Details",
                    "Name",
                    "Age",
                    dataset);
            ChartUtils.saveChartAsJPEG(new File("C://Users/shash/Shashank/barChart.jpg"), barChart, 800, 600);

            DefaultPieDataset<String> pieDataSet = new DefaultPieDataset<>();
            for(Employee employee : empList)
            {
                pieDataSet.setValue(employee.getName(), employee.getAge());
            }

//======================================>> Creating PieChart <<======================================

            JFreeChart pieChart = ChartFactory.createPieChart(
                    "Employee Details",
                    pieDataSet,
                    true,
                    true,
                    false);
            ChartUtils.saveChartAsJPEG(new File("C://Users/shash/Shashank/pieChart.jpg"), pieChart, 800, 600);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return empList;
    }

}

package be.business;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

@Service
public class GradeService {

    public byte[] processExcelToFG(InputStream excelStream) throws Exception {

        StringBuilder fgContent;
        try (Workbook workbook = WorkbookFactory.create(excelStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            fgContent = new StringBuilder();
            fgContent.append("STUDENT_GRADE_FILE\n");
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                
                String id = row.getCell(0).getStringCellValue();
                String name = row.getCell(1).getStringCellValue();
                double math = row.getCell(2).getNumericCellValue();
                double eng = row.getCell(3).getNumericCellValue();
                double pro = row.getCell(4).getNumericCellValue();
                
                double avg = (math + eng + pro) / 3;
                String rank = getRank(avg);
                
                fgContent.append(String.format(
                        "%s|%s|%.2f|%s\n",
                        id, name, avg, rank
                ));
            }
        }

        return fgContent.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String getRank(double avg) {
        if (avg >= 8) return "EXCELLENT";
        if (avg >= 6.5) return "GOOD";
        if (avg >= 5) return "PASS";
        return "FAIL";
    }
}

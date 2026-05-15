package be.api;


import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import be.business.GradeService;

@RestController
@RequestMapping("/grade")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> uploadExcel(
            @RequestParam("file") MultipartFile file) {
        try {
            InputStream is = file.getInputStream();
            byte[] fgFile = gradeService.processExcelToFG(is);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=grade.fg")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fgFile);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
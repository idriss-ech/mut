package com.example.mutuelle.controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UploaderController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importJob;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Save the file and get the path
            String filePath = saveFile(file);
            String uniqueJobParameter = UUID.randomUUID().toString(); // Create a unique job parameter
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("filePath", filePath)
                    .addString("jobId", uniqueJobParameter) // Add unique job parameter
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            // Launch the job
            JobExecution jobExecution = jobLauncher.run(importJob, jobParameters);

            // Collect validation errors from the execution context
            List<String> validationErrors = (List<String>) jobExecution.getExecutionContext().get("validationErrors");
            if (validationErrors != null && !validationErrors.isEmpty()) {
                // Log errors before sending response
                validationErrors.forEach(System.out::println);

                // Return validation errors as part of the response
                response.put("validationErrors", validationErrors);
            }

            response.put("message", "Batch job started successfully!");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (JobExecutionException e) {
            e.printStackTrace();
            response.put("message", "Failed to start batch job: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Une erreur s'est produite: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        }
        return tempFile.getAbsolutePath();
    }
}

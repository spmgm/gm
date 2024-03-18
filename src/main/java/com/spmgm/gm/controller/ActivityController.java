package com.spmgm.gm.controller;

import com.spmgm.gm.dto.ActivityDTO;
import com.spmgm.gm.exception.BadRequestException;
import com.spmgm.gm.service.ActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/activities")
@Slf4j
@RequiredArgsConstructor
public class ActivityController {
    private static final String FILE_TYPE = "text/csv";
    private final ActivityService service;

    @GetMapping
    public ResponseEntity<List<ActivityDTO>> getActivities() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{code}")
    public ResponseEntity<ActivityDTO> getActivityByCode(@PathVariable String code) {
        return new ResponseEntity<>(service.findByCode(code), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        validateFile(file);
        service.process(file.getInputStream());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<String> delete() {
        service.deleteAll();
        return ResponseEntity.noContent().build();
    }

    private static void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("File can not be empty");
        }
        if (!FILE_TYPE.equals(file.getContentType())) {
            throw new BadRequestException("File must be CSV");
        }
    }
}
package com.ead.course.controller;

import com.ead.course.dtos.CourseDto;
import com.ead.course.model.CourseModel;
import com.ead.course.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDto dto) {

        var courseModel = new CourseModel();
        BeanUtils.copyProperties(dto, courseModel);
        courseModel.setCreationDate(LocalDateTime.now());
        courseModel.setLastUpdate(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseModel));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> delete(@PathVariable(value = "courseId")UUID courseId) {
        Optional<CourseModel> modelOptional = courseService.findById(courseId);

        modelOptional.ifPresentOrElse( courseModel ->
                        courseService.delete(courseModel),
                () ->  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course doesn't exist.")
        );
        return ResponseEntity.status(HttpStatus.OK).body("The course has been deleted.");
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId") UUID courseId, @RequestBody @Valid CourseDto dto) {
        Optional<CourseModel> modelOptional = courseService.findById(courseId);

        modelOptional.ifPresentOrElse( courseModel -> {
                    courseModel.setLastUpdate(LocalDateTime.now());
                    courseModel.setName(dto.getName());
                    courseModel.setDescription(dto.getDescription());
                    courseModel.setCourseLevel(dto.getCourseLevel());
                    courseModel.setCourseStatus(dto.getCourseStatus());
                    courseModel.setImageUrl(dto.getImageUrl());
                    courseService.save(courseModel);
                },
                () ->  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course doesn't exist.")
        );
        return ResponseEntity.status(HttpStatus.OK).body("The course has been updated.");
    }

    @GetMapping
    public ResponseEntity<List<CourseModel>> getAllCourses() {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll());
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value = "courseId") UUID courseId) {
        Optional<CourseModel> modelOptional = courseService.findById(courseId);
        modelOptional.ifPresentOrElse( courseModel ->
        ResponseEntity.status(HttpStatus.OK).body(courseModel),
                () ->  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course doesn't exist.")
        );

        return ResponseEntity.status(HttpStatus.OK).body(modelOptional.get());

    }


}

package com.ead.course.service.impl;

import com.ead.course.model.CourseModel;
import com.ead.course.model.LessonModel;
import com.ead.course.model.ModuleModel;
import com.ead.course.repository.CourseRepository;
import com.ead.course.repository.LessonRepository;
import com.ead.course.repository.ModuleRepository;
import com.ead.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    LessonRepository lessonRepository;

    @Override
    @Transactional
    public void delete(CourseModel model) {

        List<ModuleModel> modulesIntoCourse = moduleRepository.findAllModulesIntoCourse(model.getId());

        if(!modulesIntoCourse.isEmpty()) {

            for (var module : modulesIntoCourse) {
                List<LessonModel> allLessonsIntoModule = lessonRepository.findAllLessonsIntoModule(module.getId());
                if(!allLessonsIntoModule.isEmpty()) {
                    lessonRepository.deleteAll(allLessonsIntoModule);
                }
            }
            moduleRepository.deleteAll(modulesIntoCourse);
        }
        courseRepository.delete(model);
    }

    @Override
    public CourseModel save(CourseModel courseModel) {
        return courseRepository.save(courseModel);
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public List<CourseModel> findAll() {
        return courseRepository.findAll();
    }
}

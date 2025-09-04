package com.ead.course.service.impl;

import com.ead.course.model.LessonModel;
import com.ead.course.model.ModuleModel;
import com.ead.course.repository.LessonRepository;
import com.ead.course.repository.ModuleRepository;
import com.ead.course.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    LessonRepository lessonRepository;

    @Override
    @Transactional
    public void delete(ModuleModel model) {
        List<LessonModel> allLessonsIntoModule = lessonRepository.findAllLessonsIntoModule(model.getId());
        if(!allLessonsIntoModule.isEmpty()) {
            lessonRepository.deleteAll(allLessonsIntoModule);
        }
        moduleRepository.delete(model);
    }
}

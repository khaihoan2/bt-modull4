package com.codegym.controller;
import com.codegym.model.Student;
import com.codegym.model.StudentForm;
import com.codegym.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private IStudentService studentService;

    @Value("${file-upload}")
    private String fileUpload;

    @GetMapping
    public ModelAndView showAll(@RequestParam(name = "q", required = false) String name) {
        ModelAndView modelAndView = new ModelAndView("/student/list");
        List<Student> students;
        if (name == null) {
            students = studentService.findAll();
        } else {
            students = studentService.findByName(name);
        }
        modelAndView.addObject("students", students);
        return modelAndView;
    }

    @GetMapping("/search")
    public ModelAndView findByName(@RequestParam(name = "q") Optional<String> name) {
        if (name.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/student/list");
            List<Student> students = studentService.findByName(name.get());
            modelAndView.addObject("students", students);
            return modelAndView;
        }
        return new ModelAndView("/error-404");
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/student/create");
        modelAndView.addObject("student", new StudentForm());
        return modelAndView;
    }

    @PostMapping("/save")
    public String createStudent(@ModelAttribute StudentForm studentForm) {
        MultipartFile multipartFile = studentForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(studentForm.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Student student = new Student();
        student.setId(studentForm.getId());
        student.setName(studentForm.getName());
        student.setEmail(studentForm.getEmail());
        student.setAddress(studentForm.getAddress());
        student.setImage(fileName);
        studentService.save(student);
        return "redirect:/student";
    }
}

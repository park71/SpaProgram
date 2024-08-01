package com.example.spa.controller;


import com.example.spa.dto.UserDTO;
import com.example.spa.service.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    private LoginService loginService;

    public LoginController(LoginService loginService){
        this.loginService=loginService;
    }
    @GetMapping("/save")
    public String joinP(Model model){
        model.addAttribute("user", new UserDTO());
        return "save";
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveProcess(@RequestBody UserDTO userDTO){
        System.out.println(userDTO.getName());
        boolean isSaved = loginService.save(userDTO);
        System.out.println("회원가입절차");
        if (isSaved) {
            System.out.println("Save Success");
            return new ResponseEntity<>("회원가입 성공", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("회원가입 실패: 이미 존재하는 사용자", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/login")
    public ResponseEntity<String> loginPage() {
        String response = "{\"message\": \"Please log in\"}";
        return ResponseEntity.ok(response);
    }

    @PostMapping("/loginProc")
    public ResponseEntity<?> loginProcess(@RequestBody UserDTO userDTO, HttpSession session){
        Optional<UserDTO> loginResult = loginService.sign_in(userDTO);
        if (loginResult.isPresent()) {
            session.setAttribute("loginName", loginResult.get().getName());
            System.out.println("login Success");
            return new ResponseEntity<>(loginResult.get(), HttpStatus.OK);
        } else {
            System.out.println("login error");
            return new ResponseEntity<>("로그인 실패", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session){
        System.out.println("로그아웃 성공");
        session.invalidate();
        return new ResponseEntity<>("로그아웃 성공", HttpStatus.OK);
    }


    @GetMapping("/list")// 리스트
    public List<UserDTO> findAll() {
        System.out.println("hi");
        return loginService.findAll();
    }
}

package com.example.spa.controller;

import com.example.spa.dto.FireDTO;
import com.example.spa.service.FireService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fire")
public class FireController {

    private final FireService fireService;

    @GetMapping(value = "/list") // GET = DB 값 조회
    public String getList(FireDTO fireDTO) {

        return fireService.findList();
    }

    @PostMapping("/save") // POST = DB 에 값 추가
    public String saveData(@RequestBody FireDTO fireDTO) throws ExecutionException, InterruptedException {
        Long fireDatabaseId = fireService.save(fireDTO);

        fireService.saveFD(fireDatabaseId);
        return fireDTO.toString();
    }
}

package com.example.spa.controller;

import com.example.spa.dto.BoardDTO;
import com.example.spa.entity.BoardEntity;
import com.example.spa.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/board")
public class BoardController {


    @Autowired
    private BoardService boardService;

    @PostMapping("/write")
    public ResponseEntity<Map<String, String>> boardWritePro(@ModelAttribute BoardDTO boardDTO) throws Exception {
        MultipartFile file = boardDTO.getFile();
        boardService.write(boardDTO.toEntity(), file);
        Map<String, String> response = new HashMap<>();
        response.put("message", "글 작성이 완료되었습니다.");
        response.put("searchUrl", "/api/board/list");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/list") //게시판 리스트
    public ResponseEntity<Map<String, Object>> boardList(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword) {

        Page<BoardEntity> list = null;

        if(searchKeyword == null) {
            list = boardService.boardList(pageable);
        } else {
            list = boardService.boardSearchList(searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        Map<String, Object> response = new HashMap<>();
        response.put("list", list.getContent());
        response.put("nowPage", nowPage);
        response.put("startPage", startPage);
        response.put("endPage", endPage);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<BoardEntity> boardView(@PathVariable("id") Integer id) {
        BoardEntity board = boardService.boardView(id);
        return new ResponseEntity<>(board, HttpStatus.OK);
    }

    // 게시글 가입 신청
    @PostMapping("/apply/{id}")
    public ResponseEntity<String> applyToBoard(@PathVariable("id") Integer id, @RequestBody String applicant) {
        boolean success = boardService.applyToBoard(id, applicant);
        if (success) {
            return new ResponseEntity<>("Application successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Board not found", HttpStatus.NOT_FOUND);
        }
    }


    // 게시물 이미지 다운로드
    @GetMapping("/download/{id}")
    public ResponseEntity<UrlResource> downloadImage(@PathVariable Integer id) throws Exception {
        BoardEntity board = boardService.boardView(id);
        if (board != null && board.getFilePath() != null) {
            Path filePath = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/files" + board.getFilePath());
            UrlResource resource = new UrlResource(filePath.toUri());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + board.getFileName() + "\"")
                    .body(resource);
        }
        return ResponseEntity.notFound().build();
    }

    /*// 게시물 신청하기
    @PostMapping("/apply/{id}")
    public ResponseEntity<Map<String, String>> applyForBoard(@PathVariable Integer id, @RequestParam String username) {
        boardService.applyForBoard(id, username);
        Map<String, String> response = new HashMap<>();
        response.put("message", "신청이 완료되었습니다.");
        return ResponseEntity.ok(response);
    }*/



    @PutMapping("/modify/{id}") //게시글 수정페이지
    public ResponseEntity<BoardEntity> boardModify(@PathVariable("id") Integer id, @RequestBody BoardDTO boardDTO, @RequestParam("file") MultipartFile file) throws Exception{
        BoardEntity board = boardService.boardView(id);
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        board.setFileName(boardDTO.getFileName());
        boardService.write(board, file);

        return new ResponseEntity<>(board, HttpStatus.OK);
    }

    @PostMapping("/update/{id}")  // 게시글 수정하기 버튼
    public ResponseEntity<BoardEntity> boardUpdate(@PathVariable("id") Integer id, @RequestBody BoardDTO boardDTO, @RequestParam("file") MultipartFile file) throws Exception{
        BoardEntity boardTemp = boardService.boardView(id);
        boardTemp.setTitle(boardDTO.getTitle());
        boardTemp.setContent(boardDTO.getContent());
        boardTemp.setFileName(boardDTO.getFileName());
        boardService.write(boardTemp, file);

        return new ResponseEntity<>(boardTemp, HttpStatus.OK);
    }

    // 특정 게시글에 대한 신청자의 요청을 승인하거나 거절하는 엔드포인트
    @PostMapping("/respond/{boardId}")
    public ResponseEntity<String> respondToApplication(
            @PathVariable("boardId") Integer boardId,
            @RequestParam("applicant") String applicant,
            @RequestParam("approve") boolean approve) {

        boolean success = boardService.respondToApplication(boardId, applicant, approve);
        if (success) {
            return new ResponseEntity<>("Response recorded", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Board or applicant not found", HttpStatus.NOT_FOUND);
        }
    }


    // 특정 게시글의 신청자 목록을 조회하는 엔드포인트
    @GetMapping("/applicants/{id}")
    public ResponseEntity<Map<String, String>> getApplicants(@PathVariable("id") Integer id) {
        Map<String, String> applicants = boardService.getApplicants(id);
        if (applicants != null) {
            return new ResponseEntity<>(applicants, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
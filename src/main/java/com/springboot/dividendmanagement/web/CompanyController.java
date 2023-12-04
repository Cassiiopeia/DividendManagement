package com.springboot.dividendmanagement.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController {
    //  배당금 조회 자성완성
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
        return null;
    }

    // 회사 검색
    @GetMapping()
    public ResponseEntity<?> searchCompany(){
        return null;
    }

    // 회사 추가
    @PostMapping()
    public ResponseEntity<?>  addCompany(){
        return null;
    }

    // 회사 삭제
    @DeleteMapping()
    public ResponseEntity<?> deleteCompany() {
        return null;
    }
}

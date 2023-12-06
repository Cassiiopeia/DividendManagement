package com.springboot.dividendmanagement.web;

import com.springboot.dividendmanagement.model.Company;
import com.springboot.dividendmanagement.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;

    //  배당금 조회 자성완성
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
        return null;
    }

    // 회사 검색
    @GetMapping()
    public ResponseEntity<?> searchCompany() {
        return null;
    }

    // 회사 추가
    @PostMapping()
    public ResponseEntity<?> addCompany(@RequestBody Company request) {
        String ticker = request.getTicker().trim();
        if(ObjectUtils.isEmpty(ticker)){
            throw new RuntimeException("ticker is empty");
        }
        Company company = companyService.save(ticker);
        return ResponseEntity.ok(company);
    }

    // 회사 삭제
    @DeleteMapping()
    public ResponseEntity<?> deleteCompany() {
        return null;
    }
}

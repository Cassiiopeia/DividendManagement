package com.springboot.dividendmanagement.web;

import com.springboot.dividendmanagement.model.MemberEntity;
import com.springboot.dividendmanagement.model.constants.Auth;

import com.springboot.dividendmanagement.security.TokenProvider;
import com.springboot.dividendmanagement.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Auth.SignUp request) {
        // 회원가입을 위한 API
        MemberEntity memberEntity = memberService.register(request);
        log.info("user signup -> " + memberEntity.getUsername());
        return ResponseEntity.ok(memberEntity);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody Auth.SignIn request) {
        // 로그인용 API
        MemberEntity memberEntity = memberService.authenticate(request);
        String token = tokenProvider.generateToken(memberEntity.getUsername(), memberEntity.getRoles());
        log.info("user login -> " + request.getUsername());
        return ResponseEntity.ok(token);
    }
}

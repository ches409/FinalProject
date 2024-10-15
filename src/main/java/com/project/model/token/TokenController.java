package com.project.model.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.MemberSO;

@RestController
public class TokenController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private MemberSO memberSo;
    
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRequest request) {
        // 리프레시 토큰 검증
        if (jwtTokenProvider.validateToken(request.getRefreshToken())) {
            // 토큰에서 member_id 추출
            int member_id = jwtTokenProvider.getMember_id(request.getRefreshToken());
            
            // 새로운 엑세스 토큰 생성
            String newAccessToken = jwtTokenProvider.createAccessToken(member_id);
            
            // 사용자 역할 확인
            int m_role = memberSo.checkM_role(member_id);
            
            // 새로운 엑세스 토큰과 기존 리프레시 토큰 반환
            return ResponseEntity.ok(new TokenResponse(newAccessToken, request.getRefreshToken(), m_role));
        } else {
            // 리프레시 토큰이 유효하지 않음
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }
}

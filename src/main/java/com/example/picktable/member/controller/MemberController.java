package com.example.picktable.member.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.picktable.friendship.repository.FriendshipRepository;
import com.example.picktable.global.domain.dto.MsgResponseDTO;
import com.example.picktable.member.domain.dto.JwtTokenDTO;
import com.example.picktable.member.domain.dto.MemberResponseDTO;
import com.example.picktable.member.domain.dto.login.LoginRequestDTO;
import com.example.picktable.member.domain.dto.signup.SignupRequestDTO;
import com.example.picktable.member.domain.dto.update.MemberUpdateRequestDTO;
import com.example.picktable.member.domain.dto.update.MemberUpdateResponseDTO;
import com.example.picktable.member.domain.entity.Member;
import com.example.picktable.member.repository.MemberRepository;
import com.example.picktable.member.security.util.SecurityUtil;
import com.example.picktable.member.service.MemberService;
import com.example.picktable.member.service.MemberStatusService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Member", description = "Member API")
public class MemberController {
    private final MemberService memberService;
    private final MemberStatusService memberStatusService;

    @PostMapping("/sign-up")
    @Operation(description = "회원가입")
    public ResponseEntity<MsgResponseDTO> signup(@Valid @RequestBody SignupRequestDTO requestDTO) throws BadRequestException {
        memberService.createMember(requestDTO);
        return ResponseEntity.ok(new MsgResponseDTO("회원가입 완료", HttpStatus.OK.value()));
    }

    @GetMapping("/confirmLoginId/{loginId}")
    @Operation(description = "아이디 중복 확인")
    public ResponseEntity<String> confirmId(@PathVariable("loginId") String loginId) throws BadRequestException {
        if(memberService.confirmId(loginId)) {
            throw new BadRequestException("이미 사용중인 아이디입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 아이디 입니다.");
        }
    }

    @GetMapping("/confirmNickname/{nickname}")
    @Operation(description = "닉네임 중복 확인")
    public ResponseEntity<String> confirmNickname(@PathVariable("nickname") String nickname) throws BadRequestException {
        if(memberService.confirmNickname(nickname)) {
            throw new BadRequestException("이미 사용중인 닉네임입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 닉네임 입니다.");
        }
    }

    @GetMapping("/mypage/update")
    @Operation(description = "회원 정보 수정 반환(마이페이지)")
    public ResponseEntity<MemberUpdateResponseDTO>  update() throws BadRequestException {
        Member member = memberService.findByLoginId(SecurityUtil.getLoginId()).orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));
        MemberUpdateResponseDTO responseDTO = MemberUpdateResponseDTO.builder()
                .nickname(member.getNickname())
                .age(member.getAge())
                .gender(member.getGender())
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PatchMapping("/member/update")
    @Operation(description = "회원정보 수정")
    public ResponseEntity<MsgResponseDTO> updateMember(@Valid @RequestBody MemberUpdateRequestDTO memberUpdateRequestDTO) throws BadRequestException {
        memberService.updateMember(memberUpdateRequestDTO, SecurityUtil.getLoginId());
        return ResponseEntity.ok(new MsgResponseDTO("회원 정보 수정 완료", HttpStatus.OK.value()));
    }

    @DeleteMapping("/member/delete")
    @Operation(description = "회원 탈퇴")
    public ResponseEntity<MsgResponseDTO> deleteMember(@Valid @RequestBody UserDeleteDTO userDeleteDto) throws Exception {
        memberService.deleteMember(userDeleteDto.checkPassword(), SecurityUtil.getLoginId());
        return ResponseEntity.ok(new MsgResponseDTO("회원 탈퇴 완료", HttpStatus.OK.value()));
    }

    public record UserDeleteDTO(@NotBlank(message = "비밀번호를 입력해주세요")
                                String checkPassword) {
    }

    @PostMapping("/sign-in")
    @Operation(description = "로그인")
    public JwtTokenDTO signIn(@RequestBody LoginRequestDTO requestDTO) {
        String username = requestDTO.getLoginId();
        String password = requestDTO.getLoginPw();
        JwtTokenDTO jwtToken = memberService.signIn(username, password);

        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());

        memberStatusService.broadcastUserStatus(username, "LOGIN");

        return jwtToken;
    }

    @GetMapping("/memberInfo")
    public ResponseEntity<MemberResponseDTO> getMemberInfo() throws BadRequestException {
        String loginId = SecurityUtil.getLoginId();
        Member findMember = memberService.findByLoginId(loginId).orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));
        MemberResponseDTO memberResponseDTO = MemberResponseDTO.builder()
                .id(findMember.getId())
                .loginId(findMember.getLoginId())
                .loginPw(findMember.getLoginPw())
                .nickname(findMember.getNickname())
                .gender(findMember.getGender())
                .age(findMember.getAge())
                .build();

        return ResponseEntity.ok(memberResponseDTO);
    }

    @GetMapping("/memberId")
    public ResponseEntity<Long> getMemberId() throws BadRequestException {
        String loginId = SecurityUtil.getLoginId();
        Member findMember = memberService.findByLoginId(loginId).orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));

        return ResponseEntity.ok(findMember.getId());
    }
}

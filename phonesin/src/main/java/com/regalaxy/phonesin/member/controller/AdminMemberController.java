package com.regalaxy.phonesin.member.controller;

import com.regalaxy.phonesin.member.model.MemberAdminDto;
import com.regalaxy.phonesin.member.model.MemberDto;
import com.regalaxy.phonesin.member.model.MemberSearchDto;
import com.regalaxy.phonesin.member.model.service.MemberService;
import com.regalaxy.phonesin.phone.model.PhoneDto;
import com.regalaxy.phonesin.phone.model.PhoneSearchDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/member")
@Api(tags = "멤버 또는 토큰 관리 API(관리자)", description = "멤버 또는 토큰 관리 Controller(관리자)")
public class AdminMemberController {

    private final MemberService memberService;

    @ApiOperation(value = "관리자가 직접 회원 정보 수정")
    @PutMapping("/update")
    public ResponseEntity<String> updateByAdmin(@RequestBody MemberAdminDto memberAdminDto) {
        Map<String, Object> resultMap = new HashMap<>();
        MemberDto updatedMemberDto = memberService.updateMemberByAdmin(memberAdminDto);
        resultMap.put("updatedMember", updatedMemberDto);
        return new ResponseEntity<String>("Success", HttpStatus.OK);
    }

    @ApiOperation(value = "회원 정보 상세 조회(관리자)")
    @PostMapping("/info/{memberId}")
    public ResponseEntity<Map<String, Object>> memberInfoByAdmin(@PathVariable("memberId") Long memberId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("member", memberService.UserInfoByAdmin(memberId));
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
    }

    @ApiOperation(value = "회원 목록 조회")
    @GetMapping("/list")
    public ModelAndView list(){
        MemberSearchDto memberSearchDto = new MemberSearchDto();
        List<MemberDto> list = memberService.list(memberSearchDto);
        ModelAndView mav = new ModelAndView();
        mav.addObject("list", list);
        mav.addObject("title", "회원");
        mav.setViewName("/list");
        return mav;
    }

    @ApiOperation(value = "휴대폰 목록 조회 검색")
    @PostMapping("/list")
    public ResponseEntity<?> listSearch(@RequestBody MemberSearchDto memberSearchDto, Model model){
        List<MemberDto> list = memberService.list(memberSearchDto);
        Map<String, Object> map = new HashMap<>();
        model.addAttribute("list", list);
        model.addAttribute("title", "휴대폰");
        map.put("list", list);
        map.put("title", "휴대폰");
        return ResponseEntity.ok(map);
    }
}

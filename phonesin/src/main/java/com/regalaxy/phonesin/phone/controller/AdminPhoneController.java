package com.regalaxy.phonesin.phone.controller;

import com.regalaxy.phonesin.phone.model.*;
import com.regalaxy.phonesin.phone.model.service.PhoneService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/phone")
@CrossOrigin("*")
public class AdminPhoneController {
    @Autowired
    private PhoneService phoneService;

    @ApiOperation(value = "휴대폰 목록 조회")
    @GetMapping("/list")
    public ModelAndView list(){
        PhoneSearchDto phoneSearchDto = new PhoneSearchDto();
        List<PhoneDto> list = phoneService.list(phoneSearchDto);
        ModelAndView mav = new ModelAndView();
        mav.addObject("list", list);
        mav.addObject("title", "휴대폰");
        mav.setViewName("list");
        return mav;
    }

    @ApiOperation(value = "휴대폰 목록 조회 검색")
    @PostMapping("/list")
    public ResponseEntity<?> listSearch(@RequestBody PhoneSearchDto phoneSearchDto, Model model){
        List<PhoneDto> list = phoneService.list(phoneSearchDto);
        Map<String, Object> map = new HashMap<>();
        model.addAttribute("list", list);
        model.addAttribute("title", "휴대폰");
        map.put("list", list);
        map.put("title", "휴대폰");
        return ResponseEntity.ok(map);
    }
    @ApiOperation(value = "휴대폰 정보 등록")
    @PostMapping("/apply")
    public ModelAndView apply(@RequestBody PhoneApplyDto phoneApplyDto){
        ModelAndView mav = new ModelAndView();
        System.out.println("???? : " + phoneApplyDto.isClimateHumidity());
        phoneService.apply(phoneApplyDto);
        mav.setViewName("");//어디로 이동할지 ex) rental/list
        System.out.println("Success");
        return null;
    }
    @ApiOperation(value = "휴대폰 정보 수정")
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody PhoneUpdateDto phoneUpdateDto, Model model){
        PhoneApplyDto phoneApplyDto = new PhoneApplyDto();
        phoneApplyDto.setPhoneId(phoneUpdateDto.getPhoneId());
        phoneApplyDto.setModelId(phoneUpdateDto.getModelId());
        phoneApplyDto.setSerialNumber(phoneUpdateDto.getSerialNumber());
        phoneApplyDto.setDonationId(phoneUpdateDto.getDonationId());
        phoneApplyDto.setHomecam(phoneUpdateDto.isHomecam());
        phoneApplyDto.setY2K(phoneUpdateDto.isY2K());
        phoneApplyDto.setClimateHumidity(phoneUpdateDto.isClimateHumidity());
        phoneService.update(phoneApplyDto);

        PhoneSearchDto phoneSearchDto = new PhoneSearchDto();
        phoneSearchDto.setRental(phoneUpdateDto.isSearschRental());
        phoneSearchDto.setY2K(phoneUpdateDto.isSearschY2K());
        phoneSearchDto.setHomecam(phoneUpdateDto.isSearschHomecam());
        phoneSearchDto.setClimateHumidity(phoneUpdateDto.isSearschClimateHumidity());
        List<PhoneDto> list = phoneService.list(phoneSearchDto);

        Map<String, Object> map = new HashMap<>();
        model.addAttribute("list", list);
        model.addAttribute("title", "휴대폰");
        map.put("list", list);
        map.put("title", "휴대폰");
        return ResponseEntity.ok(map);
    }
    @ApiOperation(value = "휴대폰 정보 삭제")
    @DeleteMapping("/delete/{phoneId}")
    public ResponseEntity<?> delete(@PathVariable("phoneId") Long phone_id, @RequestBody PhoneSearchDto phoneSearchDto, Model model){
        phoneService.delete(phone_id);
        List<PhoneDto> list = phoneService.list(phoneSearchDto);
        Map<String, Object> map = new HashMap<>();
        model.addAttribute("list", list);
        model.addAttribute("title", "휴대폰");
        map.put("list", list);
        map.put("title", "휴대폰");
        return ResponseEntity.ok(map);
    }

    @ApiOperation(value = "휴대폰 모델 리스트")
    @GetMapping("/model")
    public ResponseEntity<?> modelList(Model model){
        List<ModelDto> list = phoneService.modelList();
        Map<String, Object> map = new HashMap<>();
        model.addAttribute("list", list);
        map.put("list", list);
        return ResponseEntity.ok(map);
    }
}

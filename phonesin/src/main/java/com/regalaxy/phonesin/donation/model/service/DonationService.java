package com.regalaxy.phonesin.donation.model.service;

import com.regalaxy.phonesin.donation.model.*;
import com.regalaxy.phonesin.donation.model.entity.Donation;
import com.regalaxy.phonesin.donation.model.repository.DonationRepository;
import com.regalaxy.phonesin.member.model.entity.Member;
import com.regalaxy.phonesin.member.model.repository.MemberRepository;
import com.regalaxy.phonesin.phone.model.entity.Phone;
import com.regalaxy.phonesin.phone.model.repository.PhoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DonationService {
    private final DonationRepository donationRepository;
    private final MemberRepository memberRepository;
    private final PhoneRepository phoneRepository;

    @Transactional
    public void donationApply(DonationApplyRequestDto donationApplyRequestDto, Long memberId) throws Exception {
        Member member = memberRepository.findById(memberId).get();
        Donation donation = donationApplyRequestDto.toEntity(member);
        donation.setDonationStatus(1);
        donationRepository.save(donation);
    }

    public List<DonationResponseDto> donationList() throws Exception {
        List<DonationResponseDto> result = donationRepository.findAll()
                .stream()
                .map(donation -> new DonationResponseDto(donation))
                .collect(Collectors.toList());
        return result;
    }

    public List<DonationResponseDto> donationSearchList(DonationSearchDto donationSearchDto) throws Exception{
        boolean accept = donationSearchDto.isAccept();
        boolean receive = donationSearchDto.isReceive();
        List<DonationResponseDto> result;
        if(accept && receive) {//둘 다
            result = donationRepository.findByDonationStatusEqualsAndDonationStatusEquals(1,2)
                    .stream()
                    .map(donation -> new DonationResponseDto(donation))
                    .collect(Collectors.toList());
        }else if(accept){//하나만
            result = donationRepository.findByDonationStatusEquals(1)
                    .stream()
                    .map(donation -> new DonationResponseDto(donation))
                    .collect(Collectors.toList());
        }else if(receive){//하나만
            result = donationRepository.findByDonationStatusEquals(2)
                    .stream()
                    .map(donation -> new DonationResponseDto(donation))
                    .collect(Collectors.toList());
        }else{//아무것도
            result = donationRepository.findAll()
                    .stream()
                    .map(donation -> new DonationResponseDto(donation))
                    .collect(Collectors.toList());
        }
        return result;
    }

    public DonationDetailResponseDto donationInfo(Long donationId) throws Exception {
        Donation donation = donationRepository.findById(donationId).get();
        DonationDetailResponseDto result = new DonationDetailResponseDto(donation);

        return result;
    }

    @Transactional
    public void donationUpdate(DonationUpdateRequestDto donationUpdateRequestDto, Long donationId) throws Exception {
        Donation donation = donationRepository.findById(donationId).get();
        if (donationUpdateRequestDto.getDonationDeliveryLocation() != null) donation.setDonationDeliveryLocation(donationUpdateRequestDto.getDonationDeliveryLocation());
        if (donationUpdateRequestDto.getDonationDeliveryLocationType() != null) donation.setDonationDeliveryLocationType(donationUpdateRequestDto.getDonationDeliveryLocationType());
        if (donationUpdateRequestDto.getDonationDeliveryDate() != null) donation.setDonationDeliveryDate(donationUpdateRequestDto.getDonationDeliveryDate());
        if (donationUpdateRequestDto.getDonationStatus() != null) donation.setDonationStatus(donationUpdateRequestDto.getDonationStatus());
    }

    @Transactional
    public void donationDelete(Long donationId) throws Exception {
        donationRepository.deleteById(donationId);
    }

    public List<DonationKingDto> donationKing() throws Exception {
        List<DonationKingDto> kingDtoList = donationRepository.findDonationKing(LocalDateTime.now().withDayOfMonth(1), LocalDateTime.now());
        if (kingDtoList.size() > 5){
            kingDtoList = kingDtoList.subList(0, 5);
        }
        return kingDtoList;
    }

    public List<DonationResponseDto> donationlist(Long memberId) throws Exception {
        List<DonationResponseDto> result = donationRepository.findAllByMember_MemberId(memberId)
                .stream()
                .map(donation -> new DonationResponseDto(donation))
                .collect(Collectors.toList());
        return result;
    }

    @Transactional
    public void adminDonationApply(Long donationId, int status){
        Donation donation = donationRepository.findById(donationId).get();
        donation.setDonationStatus(status);
        Phone phone = new Phone();
        phone.setDonation(donation);
        phoneRepository.save(phone);
        System.out.println(donation.getDonationStatus());
        donationRepository.save(donation);
    }
}

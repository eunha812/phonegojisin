package com.regalaxy.phonesin.phone.model.service;

import com.regalaxy.phonesin.donation.model.entity.Donation;
import com.regalaxy.phonesin.donation.model.repository.DonationRepository;
import com.regalaxy.phonesin.member.model.SearchDto;
import com.regalaxy.phonesin.phone.model.PhoneApplyDto;
import com.regalaxy.phonesin.phone.model.PhoneDto;
import com.regalaxy.phonesin.phone.model.entity.Phone;
import com.regalaxy.phonesin.phone.model.repository.ModelRepository;
import com.regalaxy.phonesin.phone.model.repository.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneService {
    @Autowired
    private PhoneRepository phoneRepository;
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private ModelRepository modelRepository;

    public List<PhoneDto> list(SearchDto searchDto){
        phoneRepository.search(searchDto);
        return null;
    }

    public PhoneDto info(Long phone_id){
        Phone phone = phoneRepository.findById(phone_id).get();
        PhoneDto phoneDto = new PhoneDto();
        phoneDto.setPhoneId(phone_id);
        phoneDto.setModelName(phone.getModel().getModelName());
        phoneDto.setNickname(phone.getModel().getNickname());
        phoneDto.setRentalId(phone.getRentalId());
        return phoneDto;
    }

    public void save(PhoneApplyDto phoneApplyDto){
        Phone phone = new Phone();
        phone.setDonation(donationRepository.getReferenceById(phoneApplyDto.getDonation_id()));
        phone.setModel(modelRepository.getReferenceById(phoneApplyDto.getModel_id()));
        phone.setY2K(phoneApplyDto.isY2K());
        phone.setHomecam(phoneApplyDto.isHomecam());
        phone.setClimateHumidity(phoneApplyDto.isClimateHumidity());
        phoneRepository.save(phone);
    }

    public void delete(Long phone_id){
        phoneRepository.deleteById(phone_id);
    }
}

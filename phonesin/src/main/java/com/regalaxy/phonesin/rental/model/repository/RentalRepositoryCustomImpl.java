package com.regalaxy.phonesin.rental.model.repository;

import com.regalaxy.phonesin.member.model.SearchDto;
import com.regalaxy.phonesin.rental.model.ApplyDto;
import com.regalaxy.phonesin.rental.model.RentalDetailDto;
import com.regalaxy.phonesin.rental.model.RentalDto;
import com.regalaxy.phonesin.rental.model.entity.Rental;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RentalRepositoryCustomImpl implements RentalRepositoryCustom {

    @Autowired
    private final EntityManager em;
    @Override
    public List<RentalDto> search(SearchDto searchDto) {
//        String s = "select new com.regalaxy.phonesin.rental.model.RentalDto(r.rental_id, r.rental_start, r.rental_end, r.rental_status, r.rental_deliverylocation, r.fund) from rental r";
        String s = "select new com.regalaxy.phonesin.rental.model.RentalDto(r.rental_id, r.rental_start, r.rental_end, r.rental_status, r.rental_deliverylocation, r.fund, m.model_name, p.phone_id, r.waybill_number) "
            + "from rental r join phone p on r.rental_id = p.rental_id "
                + "join model m on p.model.model_id = m.model_id";
        int n = 0;
        if(!searchDto.getEmail().isEmpty()){//이메일 검색을 했을 경우
            if(n==0){
                s+=" where ";
            }
            s += "r.member.member_id = (select m.member_id from member m where m.email='%"+searchDto.getEmail()+"%')";//서브쿼리로 member_id 찾기
            n++;
        }
        if(searchDto.getIsBlack() == 2){
            if(n==0){
                s+=" where ";
            }
            if(n>0){
                s+= " and ";
            }
            s+= "r.isBlack = true";
            n++;
        }
        else if(searchDto.getIsBlack() == 3){
            if(n==0){
                s+=" where ";
            }
            if(n>0){
                s+= " and ";
            }
            s+="r.isBlack = false";
            n++;
        }

        if(searchDto.getIsCha() == 2){
            if(n==0){
                s+=" where ";
            }
            if(n>0){
                s+= " and ";
            }
            s+= "r.isCha = true";
            n++;
        }
        else if(searchDto.getIsCha() == 3){
            if(n==0){
                s+=" where ";
            }
            if(n>0){
                s+= " and ";
            }
            s+="r.isCha = false";
            n++;
        }

        System.out.println(s);

        return em.createQuery(s, RentalDto.class)
                .getResultList();
    }

    @Override
    public boolean extension(int rental_id) {
        return false;
    }

    @Override
    public boolean apply(ApplyDto applyDto) {
        return false;
    }

    @Override
    public RentalDetailDto detailInfo(int rental_id) {
//        String s = "select new com.regalaxy.phonesin.rental.model.RentalDetailDto(r.rental_id, r.member.member_id, r.isY2K, r.isClimateHumidity, r.isHomecam, r.count, r.rental_start, r.rental_end, r.apply_date, r.rental_status, r.rental_deliverylocation, r.fund, m.model_name, p.phone_id, p.donation_id, r.waybill_number) "
        String s = "select new com.regalaxy.phonesin.rental.model.RentalDetailDto(r.rental_id, r.member.member_id, r.isY2K, r.isClimateHumidity, r.isHomecam, r.count, r.rental_start, r.rental_end, r.apply_date, r.rental_status, r.rental_deliverylocation, r.fund, m.model_name, p.phone_id, p.donation.donation_id, r.using_date) "
            + "from rental r join phone p on r.rental_id = p.rental_id "
            + "join model m on p.model.model_id = m.model_id "
                + "where r.rental_id=" + rental_id;

        List<RentalDetailDto> list = em.createQuery(s, RentalDetailDto.class).getResultList();
        if(list.size()!=0){
            return list.get(0);
        }else{
            return new RentalDetailDto();
        }
    }
}

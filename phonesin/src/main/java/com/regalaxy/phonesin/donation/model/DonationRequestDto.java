package com.regalaxy.phonesin.donation.model;

import com.regalaxy.phonesin.donation.model.entity.Donation;
import com.regalaxy.phonesin.member.model.entity.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@ApiModel(value = "기기 기증서 DTO", description = "기기 기증서 DTO")
public class DonationRequestDto {
    @ApiModelProperty(value = "기증서 ID")
    private long donationId;

    @ApiModelProperty(value = "기증 멤버 ID")
    private long memberId;

    @ApiModelProperty(value = "기증 상태")
    private int donationStatus;

    @ApiModelProperty(value = "기증 날짜")
    private LocalDateTime donationCreatedAt;

    @ApiModelProperty(value = "기증 배송 신청 날짜")
    private String donationDeliveryDate;

    @ApiModelProperty(value = "기증 배송 장소 타입")
    private String donationDeliveryLocationType;

    @ApiModelProperty(value = "기증 배송 장소")
    private String donationDeliveryLocation;

    public Donation toEntity(Member member) throws Exception {
        return Donation.builder()
                .member(member)
                .donation_status(this.donationStatus)
                .donation_delivery_date(this.donationDeliveryDate)
                .donation_delivery_location_type(this.donationDeliveryLocationType)
                .donation_delivery_location(this.donationDeliveryLocation)
                .build();
    }
}

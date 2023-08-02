package com.regalaxy.phonesin.member.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto {
    private int pgno;//1 2 3
    private String email;// gmlwncl@
    private int isBlack;//1: 전체, 2:블랙인 사람, 3:블랙이 아닌 사람
    private int isCha;//1: 전체, 2:차상위 계층인 사람, 3:차상위 계층이 아닌 사람
}

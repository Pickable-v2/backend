package com.example.picktable.global.domain.dto;

import lombok.Getter;

@Getter
public class MsgResponseDTO {
    private String msg;
    private int statusCode;

    public MsgResponseDTO(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}

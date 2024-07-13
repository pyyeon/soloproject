package com.springboot.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "Member not found"),
    MEMBER_EXISTS(409, "Member exists"),
    COFFEE_NOT_FOUND(404, "Coffee not found"),
    COFFEE_CODE_EXISTS(409, "Coffee Code exists"),
    ORDER_NOT_FOUND(404, "Order not found"),
    CANNOT_CHANGE_ORDER(403, "Order can not change"),
    NOT_IMPLEMENTATION(501, "Not Implementation"),
    QUESTION_NOT_FOUND(404, "Question not found"),
    CANNOT_CHANGE_QUESTION(403, "Question can not change"),
    CANNOT_LIKE(403, "Can not Like"),
    CANNOT_CHANGE_MEMBER_STATUS(403, "MemberStatus cannot change" );

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}

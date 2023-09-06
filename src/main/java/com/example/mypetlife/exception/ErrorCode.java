package com.example.mypetlife.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /*
     * 400
     */
    WrongEmail(HttpStatus.BAD_REQUEST, "이메일을 다시 입력해주세요"),
    WrongPassword(HttpStatus.BAD_REQUEST, "패스워드를 다시 입력해주세요"),
    WrongTimeOfAlarm(HttpStatus.BAD_REQUEST, "현재 시간 이후에만 알림을 설정할 수 있습니다"),

    /*
     * 401
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다"),
    UNAUTHORIZED_CHATROOM(HttpStatus.UNAUTHORIZED, "해당 채팅방에 접근 권한이 없습니다."),

    /*
     * 404
     */
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    NOT_FOUND_PHONE(HttpStatus.NOT_FOUND, "사용자의 휴대폰 번호를 찾을 수 없습니다"),
    NOT_FOUND_DATE(HttpStatus.NOT_FOUND, "날짜를 입력하지 않았습니다"),
    NOT_FOUND_ARTICLE(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다"),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다"),
    NOT_FOUND_SCHEDULE(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다"),
    NOT_FOUND_SCHEDULE_ALARM(HttpStatus.NOT_FOUND, "일정에 알림이 설정되어 있지 않습니다"),
    NOT_FOUND_TAG(HttpStatus.NOT_FOUND, "태그를 찾을 수 없습니다"),
    NOT_FOUND_CHATROOM(HttpStatus.NOT_FOUND, "해당 채팅방을 찾을 수 없습니다."),

    /*
     * 409
     */
    ALREADY_EXIST_USER(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다"),
    ALREADY_EXIST_CHATROOM(HttpStatus.CONFLICT, "이미 채팅방을 생성하였습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}

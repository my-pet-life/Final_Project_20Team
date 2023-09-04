package com.example.mypetlife.controller.calendar;

import com.example.mypetlife.dto.MessageResponse;
import com.example.mypetlife.dto.calendar.alarm.AlarmScheduleListDto;
import com.example.mypetlife.dto.calendar.schedule.*;
import com.example.mypetlife.service.calendar.CalendarService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/calendar")
@Tag(name = "Calendar", description = "반려 동물 일정 관리 API")
public class CalendarController {
    private final CalendarService calendarService;

    @PostMapping
    @Operation(summary = "일정 등록하기", description = "반려 동물의 일정을 등록합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(
            responseCode = "200",
            description = "일정 조회 성공",
            content = @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(
                            value = "{\"message\": \"일정이 등록되었습니다.\"}"
                    )
            )
    )
    @RequestBody(
            description = "일정 등록을 위한 JSON 데이터",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = ScheduleRequestDto.class),
                    examples = @ExampleObject(
                            value = "{\"date\":\"2023-09-04\",\"startTime\":\"19:00\",\"endTime\":\"20:00\",\"title\":\"산책\",\"content\":\"오랜만에 나들이\",\"location\":\"집 앞\",\"alarm\":30}"
                    )
            )
    )
    public MessageResponse create(HttpServletRequest request, @Valid @RequestBody ScheduleRequestDto dto)
            throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        calendarService.create(request, dto);
        return responseDto("일정이 등록되었습니다.");
    }

    @GetMapping("/readList")
    public List<ScheduleListResponseDto> readDate(HttpServletRequest request, @RequestParam("date") String date) {
        return calendarService.readDateSchedule(request, date);
    }

    @GetMapping("/{scheduleId}")
    public ScheduleResponseDto readSchedule(HttpServletRequest request, @PathVariable("scheduleId") Long scheduleId){
        return calendarService.readSchedule(request, scheduleId);
    }

    @GetMapping("/readall")
    public List<ScheduleAllListResponseDto> readAll(HttpServletRequest request) {
        return calendarService.readAllSchedule(request);
    }

    @GetMapping("/readall/alarm")
    public List<AlarmScheduleListDto> readAllAlarmSchedule(HttpServletRequest request) {
        return calendarService.readAllAlarmSchedule(request);
    }

    @GetMapping("/read/message-status/{scheduleId}")
    public ResponseEntity<String> getReservedMessageStatus(HttpServletRequest request, @PathVariable("scheduleId") Long scheduleId)
            throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException {
        return calendarService.getReservedMessageStatus(request, scheduleId);
    }

    @PutMapping("/{scheduleId}")
    public MessageResponse updateSchedule(HttpServletRequest request, @PathVariable("scheduleId") Long scheduleId, @RequestBody UpdatedScheduleDto dto) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        calendarService.updateSchedule(request, scheduleId, dto);
        return responseDto("수정이 완료되었습니다.");
    }

    @DeleteMapping("/{scheduleId}")
    public MessageResponse deleteSchedule(HttpServletRequest request, @PathVariable("scheduleId") Long scheduleId)
            throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        calendarService.deleteSchedule(request, scheduleId);
        return responseDto("삭제가 완료되었습니다.");
    }

    public MessageResponse responseDto(String message) {
        MessageResponse responseDto = new MessageResponse(message);
        return responseDto;
    }
}
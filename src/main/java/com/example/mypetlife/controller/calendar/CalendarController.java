package com.example.mypetlife.controller.calendar;

import com.example.mypetlife.dto.MessageResponse;
import com.example.mypetlife.dto.calendar.alarm.AlarmScheduleListDto;
import com.example.mypetlife.dto.calendar.schedule.*;
import com.example.mypetlife.service.calendar.CalendarService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(summary = "일정 등록", description = "반려 동물의 일정을 등록합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleRequestDto.class))
    )
    public MessageResponseDto create(HttpServletRequest request, @Valid @RequestBody ScheduleRequestDto dto)
            throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        MessageResponseDto messageResponseDto = new MessageResponseDto();
        messageResponseDto.setId(calendarService.create(request, dto));
        messageResponseDto.setMessage("일정이 등록되었습니다.");
        return messageResponseDto;
    }

    @GetMapping("/readList")
    @Operation(summary = "일정 날짜 조회", description = "반려 동물의 일정을 날짜별로 조회합니다.")
    public List<ScheduleListResponseDto> readDate(HttpServletRequest request,
                                                  @Parameter(name = "date", description = "일정을 조회하고 싶은 날짜")
                                                  @RequestParam("date") String date) {
        return calendarService.readDateSchedule(request, date);
    }

    @GetMapping("/{scheduleId}")
    @Operation(summary = "일정 단일 조회", description = "반려 동물의 일정을 조회합니다.")
    public ScheduleResponseDto readSchedule(HttpServletRequest request,
                                            @Parameter(name = "scheduleId", description = "사용자가 작성한 일정 ID")
                                            @PathVariable("scheduleId") Long scheduleId){
        return calendarService.readSchedule(request, scheduleId);
    }

    @GetMapping("/readall")
    @Operation(summary = "일정 전체 조회", description = "반려 동물의 전체 일정을 최신순으로 조회합니다.")
    public List<ScheduleAllListResponseDto> readAll(HttpServletRequest request) {
        return calendarService.readAllSchedule(request);
    }

    @GetMapping("/readall/alarm")
    @Operation(summary = "알림 설정 일정 조회", description = "반려 동물의 일정 중, 알림 설정한 일정만 조회합니다.")
    public List<AlarmScheduleListDto> readAllAlarmSchedule(HttpServletRequest request) {
        return calendarService.readAllAlarmSchedule(request);
    }

    @GetMapping("/read/message-status/{scheduleId}")
    @Operation(summary = "알림 메세지 상태 조회", description = "반려 동물의 일정 중, 알림 설정된 일정의 메세지 상태를 조회합니다.")
    public ResponseEntity<String> getReservedMessageStatus(HttpServletRequest request,
                                                           @Parameter(name = "scheduleId", description = "사용자가 작성한 일정 ID")
                                                           @PathVariable("scheduleId") Long scheduleId)
            throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException {
        return calendarService.getReservedMessageStatus(request, scheduleId);
    }

    @PutMapping("/{scheduleId}")
    @Operation(summary = "일정 수정", description = "반려 동물의 일정을 수정합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdatedScheduleDto.class))
    )
    public MessageResponse updateSchedule(HttpServletRequest request,
                                          @Parameter(name = "scheduleId", description = "사용자가 수정하고 싶은 일정 ID")
                                          @PathVariable("scheduleId") Long scheduleId,
                                          @RequestBody UpdatedScheduleDto dto)
            throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        calendarService.updateSchedule(request, scheduleId, dto);
        return responseDto("수정이 완료되었습니다.");
    }

    @DeleteMapping("/{scheduleId}")
    @Operation(summary = "일정 삭제", description = "반려 동물의 일정을 삭제합니다.")
    public MessageResponse deleteSchedule(HttpServletRequest request,
                                          @Parameter(name = "scheduleId", description = "사용자가 삭제하고 싶은 일정 ID")
                                          @PathVariable("scheduleId") Long scheduleId)
            throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        calendarService.deleteSchedule(request, scheduleId);
        return responseDto("삭제가 완료되었습니다.");
    }

    public MessageResponse responseDto(String message) {
        MessageResponse responseDto = new MessageResponse(message);
        return responseDto;
    }
}
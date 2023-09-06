package com.example.mypetlife.controller.review;

import com.example.mypetlife.dto.review.request.ReviewModifyRequestDto;
import com.example.mypetlife.dto.review.request.ReviewWriteRequestDto;
import com.example.mypetlife.dto.review.response.ReviewCommonResponse;
import com.example.mypetlife.dto.review.response.ReviewWriteResponseDto;
import com.example.mypetlife.service.review.SeoulReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "2. 서울 동물병원 리뷰 API", description = "Swagger 테스트 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seoul/reviews")
public class SeoulReviewController {
    private final SeoulReviewService seoulGetHospitalReview;

    @Operation(summary = "서울시 지역구 동물병원 리뷰 조회", description = "서울시 지역구 동물병원의 등록된 리뷰를 조회합니다.")
    @GetMapping("/{city}/{hospitalDistrict}/{hospitalName}")
    public List<ReviewWriteResponseDto> seoulHospitalWriteReview(@Parameter(name = "city", description = "서울특별시") @PathVariable("city") final String selectCity,
                                                                 @Parameter(name = "hospitalDistrict", description = "xx구") @PathVariable("hospitalDistrict") final String selectDistrict,
                                                                 @Parameter(name = "hospitalName", description = "병원이름") @PathVariable final String hospitalName) {
        return seoulGetHospitalReview.seoulGetHospitalReview(selectCity, selectDistrict, hospitalName);
    }

    @Operation(summary = "서울시 지역구 동물병원 리뷰 등록", description = "서울시 지역구 동물병원의 등록된 리뷰를 등록합니다.")
    @PostMapping("{city}/{hospitalDistrict}/{hospitalName}")
    public ReviewCommonResponse seoulHospitalWriteReview(final HttpServletRequest request,
                                                         @Parameter(name = "city", description = "서울특별시") @PathVariable("city") final String selectCity,
                                                         @Parameter(name = "hospitalDistrict", description = "xx구") @PathVariable("hospitalDistrict") final String selectDistrict,
                                                         @Parameter(name = "hospitalName", description = "병원이름") @PathVariable final String hospitalName,
                                                         @RequestBody final ReviewWriteRequestDto reviewWriteRequestDto) {

        return seoulGetHospitalReview.seoulHospitalWriteReview(request, selectCity, selectDistrict, hospitalName, reviewWriteRequestDto);
    }

    @Operation(summary = "서울시 지역구 동물병원 리뷰 수정 등록", description = "서울시 지역구 동물병원의 등록된 리뷰를 수정합니다.")
    @PutMapping("{city}/{hospitalDistrict}/{hospitalName}/{reviewId}")
    public ReviewCommonResponse seoulHospitalModifyReview(@Parameter(name = "city", description = "서울특별시") @PathVariable("city") final String selectCity,
                                                          @Parameter(name = "hospitalDistrict", description = "xx구") @PathVariable("hospitalDistrict") final String selectDistrict,
                                                          @Parameter(name = "hospitalName", description = "병원이름") @PathVariable final String hospitalName,
                                                          @Parameter(name = "reviewId", description = "등록된 리뷰 번호") @PathVariable final Long reviewId,
                                                          @RequestBody final ReviewModifyRequestDto reviewModifyRequestDto) {

        return seoulGetHospitalReview.seoulHospitalModifyReview(selectCity, selectDistrict, hospitalName, reviewId, reviewModifyRequestDto);
    }

    @Operation(summary = "서울시 지역구 동물병원 리뷰 삭제", description = "서울시 지역구 동물병원의 등록된 리뷰를 삭제합니다.")
    @DeleteMapping("{city}/{hospitalDistrict}/{hospitalName}/{reviewId}")
    public ReviewCommonResponse seoulHospitalDeleteReview(@Parameter(name = "city", description = "서울특별시") @PathVariable("city") final String selectCity,
                                                          @Parameter(name = "hospitalDistrict", description = "xx구") @PathVariable("hospitalDistrict") final String selectDistrict,
                                                          @Parameter(name = "hospitalName", description = "병원이름") @PathVariable final String hospitalName,
                                                          @Parameter(name = "reviewId", description = "등록된 리뷰 번호") @PathVariable final Long reviewId) {

        return seoulGetHospitalReview.seoulHospitalDeleteReview(selectCity, selectDistrict, hospitalName, reviewId);
    }
}
package com.example.mypetlife.controller.review;

import com.example.mypetlife.dto.review.request.ReviewModifyRequestDto;
import com.example.mypetlife.dto.review.request.ReviewWriteRequestDto;
import com.example.mypetlife.dto.review.response.ReviewCommonResponse;
import com.example.mypetlife.dto.review.response.ReviewWriteResponseDto;
import com.example.mypetlife.service.review.GyeonggiReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "3. 경기도 동물병원 리뷰 API", description = "Swagger 테스트 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gyeonggi/reviews")
public class GyeonggiReviewController {
    private final GyeonggiReviewService gyeonggiReviewService;

    @Operation(summary = "경기도 XX시의 지역구 동물병원 리뷰 조회", description = "경기도 XX시의 지역구 동물병원 리뷰를 조회합니다.")
    @GetMapping("/{city}/{hospitalCountry}/{hospitalDistrict}/{hospitalName}")
    public List<ReviewWriteResponseDto> gyeonggiHospitalGetReview(@Parameter(name = "city", description = "경기도") @PathVariable("city") final String selectCity,
                                                                  @Parameter(name = "hospitalCountry", description = "xx시 or xx군") @PathVariable("hospitalCountry") final String selectCountry,
                                                                  @Parameter(name = "hospitalDistrict", description = "xx구 or 도로명") @PathVariable("hospitalDistrict") final String selectDistrict,
                                                                  @Parameter(name = "hospitalName", description = "병원이름") @PathVariable final String hospitalName) {
        return gyeonggiReviewService.gyeonggiGetHospitalReview(selectCity, selectCountry, selectDistrict, hospitalName);
    }

    @Operation(summary = "경기도 XX시의 지역구 동물병원 리뷰 등록", description = "경기도 XX시의 지역구 동물병원 리뷰를 등록합니다.")
    @PostMapping("{city}/{hospitalCountry}/{hospitalDistrict}/{hospitalName}")
    public ReviewCommonResponse gyeonggiHospitalWriteReview(final HttpServletRequest request,
                                                            @Parameter(name = "city", description = "경기도") @PathVariable("city") final String selectCity,
                                                            @Parameter(name = "hospitalCountry", description = "xx시 or xx군") @PathVariable("hospitalCountry") final String selectCountry,
                                                            @Parameter(name = "hospitalDistrict", description = "xx구 or 도로명") @PathVariable("hospitalDistrict") final String selectDistrict,
                                                            @Parameter(name = "hospitalName", description = "병원이름") @PathVariable final String hospitalName,
                                                            @RequestBody final ReviewWriteRequestDto reviewWriteRequestDto) {

        return gyeonggiReviewService.gyeonggiHospitalWriteReview(request, selectCity, selectCountry, selectDistrict, hospitalName, reviewWriteRequestDto);
    }

    @Operation(summary = "경기도 XX시의 지역구 동물병원 리뷰 수정", description = "경기도 XX시의 지역구 동물병원 리뷰를 수정합니다.")
    @PutMapping("{city}/{hospitalCountry}/{hospitalDistrict}/{hospitalName}/{reviewId}")
    public ReviewCommonResponse gyeonggiHospitalModifyReview(@Parameter(name = "city", description = "경기도") @PathVariable("city") final String selectCity,
                                                             @Parameter(name = "hospitalCountry", description = "xx시 or xx군") @PathVariable("hospitalCountry") final String selectCountry,
                                                             @Parameter(name = "hospitalDistrict", description = "xx구 or 도로명") @PathVariable("hospitalDistrict") final String selectDistrict,
                                                             @Parameter(name = "hospitalName", description = "병원이름") @PathVariable final String hospitalName,
                                                             @Parameter(name = "reviewId", description = "등록된 리뷰 번호") @PathVariable final Long reviewId,
                                                             @RequestBody final ReviewModifyRequestDto reviewModifyRequestDto) {

        return gyeonggiReviewService.gyeonggiHospitalModifyReview(selectCity, selectCountry, selectDistrict, hospitalName, reviewId, reviewModifyRequestDto);
    }

    @Operation(summary = "경기도 XX시의 지역구 동물병원 리뷰 삭제", description = "경기도 XX시의 지역구 동물병원 리뷰를 삭제합니다.")
    @DeleteMapping("{city}/{hospitalCountry}/{hospitalDistrict}/{hospitalName}/{reviewId}")
    public ReviewCommonResponse gyeonggiHospitalDeleteReview(@Parameter(name = "city", description = "경기도") @PathVariable("city") final String selectCity,
                                                             @Parameter(name = "hospitalCountry", description = "xx시 or xx군") @PathVariable("hospitalCountry") final String selectCountry,
                                                             @Parameter(name = "hospitalDistrict", description = "xx구 or 도로명") @PathVariable("hospitalDistrict") final String selectDistrict,
                                                             @Parameter(name = "hospitalName", description = "병원이름") @PathVariable final String hospitalName,
                                                             @Parameter(name = "reviewId", description = "등록된 리뷰 번호") @PathVariable final Long reviewId) {

        return gyeonggiReviewService.gyeonggiHospitalDeleteReview(selectCity, selectCountry, selectDistrict, hospitalName, reviewId);
    }
}

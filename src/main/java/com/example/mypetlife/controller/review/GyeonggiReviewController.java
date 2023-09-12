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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gyeonggi/reviews")
public class GyeonggiReviewController {
    private final GyeonggiReviewService gyeonggiReviewService;

    @GetMapping("/{city}/{hospitalCountry}/{hospitalDistrict}/{hospitalName}")
    public List<ReviewWriteResponseDto> gyeonggiHospitalGetReview(@PathVariable("city") final String selectCity,
                                                                  @PathVariable("hospitalCountry") final String selectCountry,
                                                                  @PathVariable("hospitalDistrict") final String selectDistrict,
                                                                  @PathVariable final String hospitalName) {
        return gyeonggiReviewService.gyeonggiGetHospitalReview(selectCity, selectCountry, selectDistrict, hospitalName);
    }

    @PostMapping("{city}/{hospitalCountry}/{hospitalDistrict}/{hospitalName}")
    public ReviewCommonResponse gyeonggiHospitalWriteReview(final HttpServletRequest request,
                                                            @PathVariable("city") final String selectCity,
                                                            @PathVariable("hospitalCountry") final String selectCountry,
                                                            @PathVariable("hospitalDistrict") final String selectDistrict,
                                                            @PathVariable final String hospitalName,
                                                            @RequestBody final ReviewWriteRequestDto reviewWriteRequestDto) {

        return gyeonggiReviewService.gyeonggiHospitalWriteReview(request, selectCity, selectCountry, selectDistrict, hospitalName, reviewWriteRequestDto);
    }

    @PutMapping("{city}/{hospitalCountry}/{hospitalDistrict}/{hospitalName}/{reviewId}")
    public ReviewCommonResponse gyeonggiHospitalModifyReview(@PathVariable("city") final String selectCity,
                                                             @PathVariable("hospitalCountry") final String selectCountry,
                                                             @PathVariable("hospitalDistrict") final String selectDistrict,
                                                             @PathVariable final String hospitalName,
                                                             @PathVariable final Long reviewId,
                                                             @RequestBody final ReviewModifyRequestDto reviewModifyRequestDto) {

        return gyeonggiReviewService.gyeonggiHospitalModifyReview(selectCity, selectCountry, selectDistrict, hospitalName, reviewId, reviewModifyRequestDto);
    }

    @DeleteMapping("{city}/{hospitalCountry}/{hospitalDistrict}/{hospitalName}/{reviewId}")
    public ReviewCommonResponse gyeonggiHospitalDeleteReview(@PathVariable("city") final String selectCity,
                                                             @PathVariable("hospitalCountry") final String selectCountry,
                                                             @PathVariable("hospitalDistrict") final String selectDistrict,
                                                             @PathVariable final String hospitalName,
                                                             @PathVariable final Long reviewId) {

        return gyeonggiReviewService.gyeonggiHospitalDeleteReview(selectCity, selectCountry, selectDistrict, hospitalName, reviewId);
    }
}
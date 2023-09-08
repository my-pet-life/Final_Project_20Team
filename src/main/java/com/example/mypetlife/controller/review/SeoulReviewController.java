package com.example.mypetlife.controller.review;

import com.example.mypetlife.dto.review.request.ReviewModifyRequestDto;
import com.example.mypetlife.dto.review.request.ReviewWriteRequestDto;
import com.example.mypetlife.dto.review.response.ReviewCommonResponse;
import com.example.mypetlife.dto.review.response.ReviewWriteResponseDto;
import com.example.mypetlife.service.review.SeoulReviewService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seoul/reviews")
public class SeoulReviewController {
    private final SeoulReviewService seoulGetHospitalReview;

    @GetMapping("/{city}/{hospitalDistrict}/{hospitalName}")
    public List<ReviewWriteResponseDto> seoulHospitalWriteReview(@PathVariable("city") final String selectCity,
                                                                 @PathVariable("hospitalDistrict") final String selectDistrict,
                                                                 @PathVariable final String hospitalName) {
        return seoulGetHospitalReview.seoulGetHospitalReview(selectCity, selectDistrict, hospitalName);
    }

    @PostMapping("{city}/{hospitalDistrict}/{hospitalName}")
    public ReviewCommonResponse seoulHospitalWriteReview(final HttpServletRequest request,
                                                         @Parameter(name = "city", description = "서울특별시") @PathVariable("city") final String selectCity,
                                                         @Parameter(name = "hospitalDistrict", description = "xx구") @PathVariable("hospitalDistrict") final String selectDistrict,
                                                         @Parameter(name = "hospitalName", description = "병원이름") @PathVariable final String hospitalName,
                                                         @RequestBody final ReviewWriteRequestDto reviewWriteRequestDto) {

        return seoulGetHospitalReview.seoulHospitalWriteReview(request, selectCity, selectDistrict, hospitalName, reviewWriteRequestDto);
    }

    @PutMapping("{city}/{hospitalDistrict}/{hospitalName}/{reviewId}")
    public ReviewCommonResponse seoulHospitalModifyReview(@PathVariable("city") final String selectCity,
                                                          @PathVariable("hospitalDistrict") final String selectDistrict,
                                                          @PathVariable final String hospitalName,
                                                          @PathVariable final Long reviewId,
                                                          @RequestBody final ReviewModifyRequestDto reviewModifyRequestDto) {

        return seoulGetHospitalReview.seoulHospitalModifyReview(selectCity, selectDistrict, hospitalName, reviewId, reviewModifyRequestDto);
    }

    @DeleteMapping("{city}/{hospitalDistrict}/{hospitalName}/{reviewId}")
    public ReviewCommonResponse seoulHospitalDeleteReview(@PathVariable("city") final String selectCity,
                                                          @PathVariable("hospitalDistrict") final String selectDistrict,
                                                          @PathVariable final String hospitalName,
                                                          @PathVariable final Long reviewId) {

        return seoulGetHospitalReview.seoulHospitalDeleteReview(selectCity, selectDistrict, hospitalName, reviewId);
    }
}
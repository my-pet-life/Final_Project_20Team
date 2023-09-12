package com.example.mypetlife.service.review;


import com.example.mypetlife.dto.review.request.ReviewModifyRequestDto;
import com.example.mypetlife.dto.review.request.ReviewWriteRequestDto;
import com.example.mypetlife.dto.review.response.ReviewCommonResponse;
import com.example.mypetlife.dto.review.response.ReviewWriteResponseDto;
import com.example.mypetlife.entity.hospital.GyeonggiDoHospital;
import com.example.mypetlife.entity.review.GyeonggiReview;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.repository.hospital.GyeonggiDoHospitalRepository;
import com.example.mypetlife.repository.review.GyeonggiReviewRepository;
import com.example.mypetlife.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GyeonggiReviewService {
    private final GyeonggiReviewRepository gyeonggiReviewRepository;
    private final GyeonggiDoHospitalRepository gyeonggiDoHospitalRepository;
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;

    @Transactional(readOnly = true)
    public List<ReviewWriteResponseDto> gyeonggiGetHospitalReview(final String selectCity, final String selectCountry,
                                                                  final String selectDistrict, final String hospitalName) {
        List<ReviewWriteResponseDto> reviewWriteResponseDtoList = new ArrayList<>();
        GyeonggiDoHospital gyeonggiDoHospital = validateExistHospital(selectCity, selectCountry, selectDistrict, hospitalName);

        List<GyeonggiReview> reviewEntities = gyeonggiDoHospital.getReviewEntities();

        for (GyeonggiReview gyeonggiReviewEntity : reviewEntities) {
            ReviewWriteResponseDto responseDto = new ReviewWriteResponseDto(
                    gyeonggiReviewEntity.getTitle(), gyeonggiReviewEntity.getContent(),
                    gyeonggiReviewEntity.getTherapy(), gyeonggiReviewEntity.getPrice(),
                    gyeonggiReviewEntity.getReviewDate(), gyeonggiReviewEntity.getStarRating()
            );
            reviewWriteResponseDtoList.add(responseDto);
        }
        return reviewWriteResponseDtoList;
    }

    public ReviewCommonResponse gyeonggiHospitalWriteReview(final HttpServletRequest request, final String selectCity,
                                                            final String selectCountry, final String selectDistrict,
                                                            final String hospitalName,
                                                            final ReviewWriteRequestDto reviewWriteRequestDto) {

        GyeonggiDoHospital gyeonggiDoHospital = validateExistHospital(selectCity, selectCountry, selectDistrict, hospitalName);

        // 다음 리뷰의 ID를 가져와서 리뷰를 생성할 때 사용
        Long nextReviewId = gyeonggiDoHospital.getNextReviewId();

        GyeonggiReview gyeonggiReview = new GyeonggiReview(nextReviewId, reviewWriteRequestDto.getTitle(), reviewWriteRequestDto.getContent(),
                reviewWriteRequestDto.getTherapy(), reviewWriteRequestDto.getPrice(), LocalDateTime.now(), reviewWriteRequestDto.getStarRating());

        // 리뷰 ID를 증가시켜 다음 리뷰를 위해 준비
        gyeonggiDoHospital.increaseReviewCount();
        gyeonggiDoHospital.increaseNextReviewCount();

        User user = userService.findByEmail(jwtTokenUtils.getEmailFromHeader(request));

        // 리뷰와 동물병원의 관계 설정
        gyeonggiReview.setGyeonggiDoHospital(gyeonggiDoHospital);
        gyeonggiReview.setUser(user);

        gyeonggiReviewRepository.save(gyeonggiReview);

        gyeonggiDoHospitalRepository.save(gyeonggiDoHospital);

        ReviewCommonResponse reviewCommonResponse = new ReviewCommonResponse();
        reviewCommonResponse.setMessage("리뷰 등록이 완료 되었습니다.");

        return reviewCommonResponse;
    }


    public ReviewCommonResponse gyeonggiHospitalModifyReview(final String selectCity, final String selectCountry,
                                                             final String selectDistrict, final String hospitalName,
                                                             final Long reviewId, final ReviewModifyRequestDto reviewModifyRequestDto) {

        GyeonggiDoHospital gyeonggiDoHospital = validateExistHospital(selectCity, selectCountry, selectDistrict, hospitalName);

        // TODO : 예외처리
        GyeonggiReview gyeonggiReview = gyeonggiReviewRepository.findByGyeonggiDoHospitalIdAndNextReviewId(
                gyeonggiDoHospital, reviewId
        ).orElseThrow();

        gyeonggiReview.modifyReview(reviewModifyRequestDto.getTitle(), reviewModifyRequestDto.getContent(),
                reviewModifyRequestDto.getTherapy(), reviewModifyRequestDto.getPrice(), LocalDateTime.now(), reviewModifyRequestDto.getStarRating());

        gyeonggiReviewRepository.save(gyeonggiReview);

        gyeonggiDoHospitalRepository.save(gyeonggiDoHospital);

        ReviewCommonResponse reviewCommonResponse = new ReviewCommonResponse();
        reviewCommonResponse.setMessage("리뷰 수정이 완료되었습니다.");

        return reviewCommonResponse;
    }

    public ReviewCommonResponse gyeonggiHospitalDeleteReview(final String selectCity, final String selectCountry,
                                                             final String selectDistrict, final String hospitalName,
                                                             final Long reviewId) {

        GyeonggiDoHospital gyeonggiDoHospital = validateExistHospital(selectCity, selectCountry, selectDistrict, hospitalName);

        // TODO : 예외처리
        GyeonggiReview gyeonggiReview = gyeonggiReviewRepository.findByGyeonggiDoHospitalIdAndNextReviewId(
                gyeonggiDoHospital, reviewId
        ).orElseThrow();

        gyeonggiReviewRepository.delete(gyeonggiReview);

        ReviewCommonResponse reviewCommonResponse = new ReviewCommonResponse();
        reviewCommonResponse.setMessage("리뷰 삭제가 완료되었습니다.");

        return reviewCommonResponse;
    }




    private GyeonggiDoHospital validateExistHospital(final String selectCity, final String selectCountry,
                                                     final String selectDistrict, final String hospitalName) {
        return gyeonggiDoHospitalRepository.
                findByGyeonggiDoAddress_CityAndGyeonggiDoAddress_CountryAndAndGyeonggiDoAddress_DistrictAndHospitalName
                        (selectCity, selectCountry, selectDistrict, hospitalName).orElseThrow();
//                .orElseThrow(() -> new NotExistHospital("해당 동물병원이 존재 하지 않습니다."));
    }
}
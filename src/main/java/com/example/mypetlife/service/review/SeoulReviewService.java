package com.example.mypetlife.service.review;

import com.example.mypetlife.dto.review.request.ReviewModifyRequestDto;
import com.example.mypetlife.dto.review.request.ReviewWriteRequestDto;
import com.example.mypetlife.dto.review.response.ReviewCommonResponse;
import com.example.mypetlife.dto.review.response.ReviewWriteResponseDto;
import com.example.mypetlife.entity.hospital.SeoulHospital;
import com.example.mypetlife.entity.review.SeoulReview;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.repository.hospital.SeoulHospitalRepository;
import com.example.mypetlife.repository.review.SeoulReviewRepository;
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
public class SeoulReviewService {
    private final SeoulReviewRepository seoulReviewRepository;
    private final SeoulHospitalRepository seoulHospitalRepository;

    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;

    @Transactional(readOnly = true)
    public List<ReviewWriteResponseDto> seoulGetHospitalReview(final String selectCity,
                                                               final String selectDistrict, final String hospitalName) {
        List<ReviewWriteResponseDto> reviewWriteResponseDtoList = new ArrayList<>();
        SeoulHospital seoulHospital = seoulHospitalRepository.
                findBySeoulAddress_CityAndSeoulAddress_DistrictAndHospitalName(selectCity, selectDistrict, hospitalName).orElseThrow();

        List<SeoulReview> reviewEntities = seoulHospital.getSeoulReviewEntities();

        for (SeoulReview seoulReviewEntity : reviewEntities) {
            ReviewWriteResponseDto responseDto = new ReviewWriteResponseDto(
                    seoulReviewEntity.getTitle(), seoulReviewEntity.getContent(),
                    seoulReviewEntity.getTherapy(), seoulReviewEntity.getPrice(),
                    seoulReviewEntity.getReviewDate(), seoulReviewEntity.getStarRating()
            );
            reviewWriteResponseDtoList.add(responseDto);
        }

        return reviewWriteResponseDtoList;
    }

    public ReviewCommonResponse seoulHospitalWriteReview(final HttpServletRequest request, final String selectCity,
                                                         final String selectDistrict, final String hospitalName,
                                                         final ReviewWriteRequestDto reviewWriteRequestDto) {
        SeoulHospital seoulHospital = seoulHospitalRepository.
                findBySeoulAddress_CityAndSeoulAddress_DistrictAndHospitalName(selectCity, selectDistrict, hospitalName).orElseThrow();
        // 다음 리뷰의 ID를 가져와서 리뷰를 생성할 때 사용
        Long nextReviewId = seoulHospital.getNextReviewId();

        SeoulReview seoulReviewEntity = new SeoulReview(nextReviewId, reviewWriteRequestDto.getTitle(), reviewWriteRequestDto.getContent(),
                reviewWriteRequestDto.getTherapy(), reviewWriteRequestDto.getPrice(), LocalDateTime.now(), reviewWriteRequestDto.getStarRating());

        seoulHospital.increaseReviewCount();
        seoulHospital.increaseNextReviewCount();

        User user = userService.findByEmail(jwtTokenUtils.getEmailFromHeader(request));

        seoulReviewEntity.setSeoulHospital(seoulHospital);
        seoulReviewEntity.setUser(user);

        seoulHospitalRepository.save(seoulHospital);

        seoulReviewRepository.save(seoulReviewEntity);

        ReviewCommonResponse reviewCommonResponse = new ReviewCommonResponse();
        reviewCommonResponse.setMessage("리뷰 등록이 완료 되었습니다.");

        return reviewCommonResponse;
    }

    public ReviewCommonResponse seoulHospitalModifyReview(final String selectCity, final String selectDistrict,
                                                          final String hospitalName, final Long reviewId,
                                                          final ReviewModifyRequestDto reviewModifyRequestDto) {
        SeoulHospital seoulHospital = seoulHospitalRepository.
                findBySeoulAddress_CityAndSeoulAddress_DistrictAndHospitalName(selectCity, selectDistrict, hospitalName).orElseThrow();

        SeoulReview seoulReviewEntity = seoulReviewRepository.findBySeoulHospitalIdAndNextReviewId(
                seoulHospital, reviewId
        ).orElseThrow();

        seoulReviewEntity.modifyReview(reviewModifyRequestDto.getTitle(), reviewModifyRequestDto.getContent(),
                reviewModifyRequestDto.getTherapy(), reviewModifyRequestDto.getPrice(), LocalDateTime.now(), reviewModifyRequestDto.getStarRating());

        seoulHospitalRepository.save(seoulHospital);

        seoulReviewRepository.save(seoulReviewEntity);

        ReviewCommonResponse reviewCommonResponse = new ReviewCommonResponse();
        reviewCommonResponse.setMessage("리뷰 수정이 완료 되었습니다.");

        return reviewCommonResponse;
    }

    public ReviewCommonResponse seoulHospitalDeleteReview(final String selectCity, final String selectDistrict,
                                                          final String hospitalName, final Long reviewId) {

        SeoulHospital seoulHospital = seoulHospitalRepository.
                findBySeoulAddress_CityAndSeoulAddress_DistrictAndHospitalName(selectCity, selectDistrict, hospitalName).orElseThrow();

        // TODO : 예외처리
        SeoulReview seoulReviewEntity = seoulReviewRepository.findBySeoulHospitalIdAndNextReviewId(
                seoulHospital, reviewId
        ).orElseThrow();


        seoulReviewRepository.delete(seoulReviewEntity);

        ReviewCommonResponse reviewCommonResponse = new ReviewCommonResponse();
        reviewCommonResponse.setMessage("리뷰 삭제가 완료되었습니다.");

        return reviewCommonResponse;
    }
}
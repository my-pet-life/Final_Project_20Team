package com.example.mypetlife.init.review;

import com.example.mypetlife.entity.review.GyeonggiReview;
import com.example.mypetlife.entity.review.SeoulReview;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.repository.UserRepository;
import com.example.mypetlife.repository.review.GyeonggiReviewRepository;
import com.example.mypetlife.repository.review.SeoulReviewRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitReview {
    private final InitReviewService initReviewService;

    @PostConstruct
    public void init() {
        initReviewService.postGyeonggiReview();
        initReviewService.postSeoulReview();

    }

    @Service
    @RequiredArgsConstructor
    @Transactional
    static class InitReviewService {

        private final GyeonggiReviewRepository gyeonggiReviewRepository;
        private final SeoulReviewRepository seoulReviewRepository;
        private final UserRepository userRepository;


        public void postGyeonggiReview() {
            User kim = userRepository.findByEmail("kim@naver.com").get();
            User lee = userRepository.findByEmail("lee@naver.com").get();

            GyeonggiReview gyeonggiReview1 = new GyeonggiReview(1L, "여기가 안양동물병원 중에 짱",
                    "안양동물병원중 여기가 제일 친절하네요", "예방 접종", 35000, LocalDateTime.now(), 5);

            GyeonggiReview gyeonggiReview2 = new GyeonggiReview(1L, "여기가 수원동물병원 중에 최고인것 같네요",
                    "수원동물병원중 대기 많이 안하고 빨리 진찰 봐주시는것 같네요", "예방 접종", 20000, LocalDateTime.now(), 5);

            gyeonggiReview1.setUser(kim);
            gyeonggiReview2.setUser(lee);


            gyeonggiReviewRepository.save(gyeonggiReview1);
            gyeonggiReviewRepository.save(gyeonggiReview2);

        }

        public void postSeoulReview() {
            User kim = userRepository.findByEmail("kim@naver.com").get();
            User park = userRepository.findByEmail("park@naver.com").get();


            SeoulReview seoulReview1 = new SeoulReview(1L, "여기가 성동구 동물병원 중에 짱",
                    "성동구 동물병원중 여기가 제일 싸고 잘해주시네요.", "예방 접종", 35000, LocalDateTime.now(), 5);

            SeoulReview seoulReview2 = new SeoulReview(1L, "중구 동물병원 중에 최고네요",
                    "중구 동물병원중 여기가 제일 싸고 잘해주시네요.", "예방 접종", 25000, LocalDateTime.now(), 5);

            seoulReview1.setUser(kim);
            seoulReview2.setUser(park);

            seoulReviewRepository.save(seoulReview1);
            seoulReviewRepository.save(seoulReview2);

        }
    }


}

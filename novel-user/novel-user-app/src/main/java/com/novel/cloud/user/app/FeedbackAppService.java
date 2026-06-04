package com.novel.cloud.user.app;

import cn.dev33.satoken.stp.StpUtil;
import com.novel.cloud.user.domain.entity.UserFeedback;
import com.novel.cloud.user.domain.repository.UserFeedbackRepository;
import com.novel.cloud.user.dto.SubmitFeedbackDto;
import com.novel.cloud.user.dto.SubmitFeedbackVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Feedback application service
 */
@Service
@RequiredArgsConstructor
public class FeedbackAppService {

    private final UserFeedbackRepository userFeedbackRepository;

    public Mono<SubmitFeedbackVo> submitFeedback(SubmitFeedbackDto params) {
        return Mono.fromCallable(() -> {
            Long userId = StpUtil.getLoginIdAsLong();

            UserFeedback feedback = new UserFeedback();
            feedback.setUserId(userId);
            feedback.setCategory(params.getCategory());
            feedback.setContent(params.getContent());
            feedback.setContact(params.getContact());
            feedback.setImages(params.getImages());
            feedback.setStatus(0); // Pending

            userFeedbackRepository.save(feedback);

            SubmitFeedbackVo vo = new SubmitFeedbackVo();
            vo.setSuccess(true);
            vo.setMessage("Feedback submitted successfully. Thank you!");
            return vo;
        });
    }
}

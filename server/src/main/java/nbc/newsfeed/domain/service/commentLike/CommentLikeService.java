package nbc.newsfeed.domain.service.commentLike;

import lombok.RequiredArgsConstructor;
import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.entity.CommentEntity;
import nbc.newsfeed.domain.entity.UserEntity;
import nbc.newsfeed.domain.repository.comment.CommentRepository;
import nbc.newsfeed.domain.repository.commentLike.CommentLikeRepository;
import nbc.newsfeed.domain.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public void toggleLike(Long commentId, Long userId) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        commentEntity.toggleLike(user, commentLikeRepository);
    }

    @Transactional(readOnly = true)
    public int getLikeCount(Long commentId) {
        Optional<CommentEntity> commentEntity = Optional.ofNullable(commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND)));

        return commentLikeRepository.countByComment(commentEntity);
    }
}


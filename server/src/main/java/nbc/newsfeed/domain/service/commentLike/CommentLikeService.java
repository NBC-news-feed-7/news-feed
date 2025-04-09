package nbc.newsfeed.domain.service.commentLike;

import lombok.RequiredArgsConstructor;
import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.dto.newsfeedLike.LikeUserResponseDto;
import nbc.newsfeed.domain.entity.CommentEntity;
import nbc.newsfeed.domain.entity.CommentLikeEntity;
import nbc.newsfeed.domain.entity.UserEntity;
import nbc.newsfeed.domain.repository.comment.CommentRepository;
import nbc.newsfeed.domain.repository.commentLike.CommentLikeRepository;
import nbc.newsfeed.domain.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public void addLike(Long commentId, Long userId) {
        CommentEntity comment = commentRepository.getByIdOrThrow(commentId);
        UserEntity user = userRepository.getByIdOrThrow(userId);

        commentLikeRepository.validateNotAlreadyLiked(comment, user);

        CommentLikeEntity like = CommentLikeEntity.of(comment, user);
        commentLikeRepository.save(like);
    }

    public void removeLike(Long commentId, long userId) {
        CommentEntity comment = commentRepository.getByIdOrThrow(commentId);
        UserEntity user = userRepository.getByIdOrThrow(userId);

        CommentLikeEntity like = commentLikeRepository.getByCommentAndUserOrThrow(comment, user);

        commentLikeRepository.delete(like);
    }

    @Transactional(readOnly = true)
    public int getLikeCount(Long commentId) {
        Optional<CommentEntity> commentEntity = Optional.ofNullable(commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND)));

        return commentLikeRepository.countByComment(commentEntity);
    }

    public List<LikeUserResponseDto> getLikeUsers(Long commentId) {
        return commentLikeRepository.findLikeUsersByCommentId(commentId);
    }

}


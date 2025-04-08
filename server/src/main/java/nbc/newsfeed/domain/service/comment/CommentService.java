package nbc.newsfeed.domain.service.comment;

import lombok.RequiredArgsConstructor;
import nbc.newsfeed.common.error.CustomException;
import nbc.newsfeed.common.error.ErrorCode;
import nbc.newsfeed.domain.dto.comment.request.CreateCommentRequestDTO;
import nbc.newsfeed.domain.dto.comment.request.UpdateCommentRequestDTO;
import nbc.newsfeed.domain.dto.comment.response.CommentResponseDTO;
import nbc.newsfeed.domain.dto.comment.response.PutCommentResponseDTO;
import nbc.newsfeed.domain.entity.CommentEntity;
import nbc.newsfeed.domain.entity.NewsFeedEntity;
import nbc.newsfeed.domain.entity.UserEntity;
import nbc.newsfeed.domain.repository.comment.CommentRepository;
import nbc.newsfeed.domain.repository.commentLike.CommentLikeRepository;
import nbc.newsfeed.domain.repository.newsfeed.NewsFeedRepository;
import nbc.newsfeed.domain.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final NewsFeedRepository newsFeedRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;

    public List<CommentResponseDTO> getCommentsByNewsFeedId(Long feedId) {
        NewsFeedEntity newsFeed = newsFeedRepository.findById(feedId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEWSFEED_NOT_FOUND));
        List<CommentEntity> comments = commentRepository.findAllByNewsFeed(newsFeed)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        return comments.stream()
                .map(comment -> new CommentResponseDTO(
                        comment.getId(),
                        comment.getUser().getNickname(),
                        comment.getContent(),
                        comment.getParentComment() != null ? comment.getParentComment().getId() : null,
                        newsFeed.getId(),
                        getLikeCount(comment),
                        comment.getCreatedAt(),
                        comment.getUpdatedAt()
                )).collect(Collectors.toList());
    }
    public int getLikeCount(CommentEntity comment) {
        return commentLikeRepository.countByComment(Optional.ofNullable(comment));
    }

    public CommentResponseDTO createComment(Long userId, CreateCommentRequestDTO createCommentRequestDTO) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        NewsFeedEntity newsFeed = newsFeedRepository.findById(createCommentRequestDTO.getFeedId())
                .orElseThrow(() -> new CustomException(ErrorCode.NEWSFEED_NOT_FOUND));
        CommentEntity parentComment = null;
        if (createCommentRequestDTO.getParentCommentId() != null) {
            parentComment = commentRepository.findById(createCommentRequestDTO.getParentCommentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        }

        CommentEntity comment = CommentEntity.builder()
                .user(user)
                .newsFeed(newsFeed)
                .content(createCommentRequestDTO.getContent())
                .parentComment(parentComment)
                .build();
        CommentEntity saved = commentRepository.save(comment);

        return new CommentResponseDTO(
                saved.getId(),
                saved.getUser().getNickname(),
                saved.getContent(),
                saved.getParentComment() != null ? saved.getParentComment().getId() : null,
                saved.getNewsFeed().getId(),
                0,
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    public PutCommentResponseDTO updateComment(Long userId, UpdateCommentRequestDTO updateCommentRequestDTO) {
        CommentEntity comment = commentRepository.findById(updateCommentRequestDTO.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        System.out.println("test" + comment);
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN); // 본인이 작성한 댓글만 수정 가능
        }
        comment.updateContent(updateCommentRequestDTO.getContent());

        return PutCommentResponseDTO.builder()
                .newsFeedId(comment.getNewsFeed().getId())
                .id(comment.getId())
                .nickname(comment.getUser().getNickname())
                .content(comment.getContent())
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .likeCount(getLikeCount(comment))
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    public void deleteComment(Long commentId, Long userId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        comment.delete();
        commentRepository.save(comment);
    }
}



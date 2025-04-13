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

import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final NewsFeedRepository newsFeedRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;

    // 좋아요 개수
    public int getLikeCount(CommentEntity comment) {
        return commentLikeRepository.countByComment(Optional.ofNullable(comment));
    }

    public List<CommentResponseDTO> getCommentsByNewsFeedId(Long feedId) {
        // 피드 찾기 delete_at is not null 인 것만
        NewsFeedEntity newsFeed = newsFeedRepository.findById(feedId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEWSFEED_NOT_FOUND));
        // 댓글 찾기 use_yn 1인 댓글만
        List<CommentEntity> comments = commentRepository.findAllByNewsFeedAndUseYn(newsFeed,1)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        Map<Long, CommentResponseDTO> dtoMap = new HashMap<>();
        List<CommentResponseDTO> rootComments = new ArrayList<>();

        // 댓글 리스트 돌면서 댓글을 맵에 저장
        for (CommentEntity comment : comments) {
            CommentResponseDTO dto = CommentResponseDTO.builder()
                    .id(comment.getId())
                    .nickname(comment.getUser().getNickname())
                    .content(comment.getContent())
                    .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                    .newsFeedId(newsFeed.getId())
                    .likeCount(getLikeCount(comment))
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .build();

            dtoMap.put(dto.getId(), dto);
        }

        // 맵 순회하면서 트리구조 변환
        for (CommentResponseDTO dto : dtoMap.values()) {
            // 부모 없으면 루트
            if (dto.getParentCommentId() == null) {
                rootComments.add(dto);
            } else {
                // 있으면 칠드런
                CommentResponseDTO parent = dtoMap.get(dto.getParentCommentId());
                if (parent != null) {
                    parent.getChildren().add(dto);
                }
            }
        }
        // 루트만 반환해도 루트 안에 칠드런이 있어서 대댓글 반환 가능
        return rootComments;
    }


    public CommentResponseDTO createComment(Long userId, CreateCommentRequestDTO createCommentRequestDTO) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        NewsFeedEntity newsFeed = newsFeedRepository.findById(createCommentRequestDTO.getFeedId())
                .orElseThrow(() -> new CustomException(ErrorCode.NEWSFEED_NOT_FOUND));
        CommentEntity parentComment = null;
        if (createCommentRequestDTO.getParentCommentId() != null) {
            parentComment = commentRepository.findByIdAndUseYn(createCommentRequestDTO.getParentCommentId(),1)
                    .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        }

        CommentEntity comment = CommentEntity.builder()
                .user(user)
                .newsFeed(newsFeed)
                .content(createCommentRequestDTO.getContent())
                .parentComment(parentComment)
                .build();
        CommentEntity saved = commentRepository.save(comment);
        return CommentResponseDTO.builder()
                .id(saved.getId())
                .nickname(saved.getUser().getNickname())
                .content(saved.getContent())
                .parentCommentId(saved.getParentComment() != null ? saved.getParentComment().getId() : null)
                .newsFeedId(saved.getNewsFeed().getId())
                .likeCount(0)
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    public PutCommentResponseDTO updateComment(Long userId, UpdateCommentRequestDTO updateCommentRequestDTO) {
        CommentEntity comment = commentRepository.findByIdAndUseYn(updateCommentRequestDTO.getId(),1)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN); // 본인이 작성한 댓글만 수정 가능
        }
        comment.updateContent(updateCommentRequestDTO.getContent()); // test

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

    // 소프트 삭제, 댓글을 삭제하면 하위 댓글에는 영향을 안미침
    public void deleteComment(Long commentId, Long userId) {
        CommentEntity comment = commentRepository.findByIdAndUseYn(commentId,1)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        comment.delete();
        commentRepository.save(comment);
    }
}



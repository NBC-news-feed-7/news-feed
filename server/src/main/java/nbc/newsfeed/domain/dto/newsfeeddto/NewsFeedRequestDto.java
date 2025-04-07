package nbc.newsfeed.domain.dto.newsfeeddto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewsFeedRequestDto{

    private String title;
    private String content;
}

package nbc.newsfeed.domain.dto.newsfeed;

public enum NewsFeedUseYn {
    Y("y"), N("N");
    private String yn;

    NewsFeedUseYn(String yn){
        this.yn = yn;
    }

    public String getYn() {
        return yn;
    }
}

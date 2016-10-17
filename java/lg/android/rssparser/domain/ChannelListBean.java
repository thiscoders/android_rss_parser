package lg.android.rssparser.domain;

/**
 * Created by ye on 10/9/16.
 */

public class ChannelListBean {
    private String title;
    private String href;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        return "ChannelListBean{" +
                "title='" + title + '\'' +
                ", href='" + href + '\'' +
                '}';
    }
}

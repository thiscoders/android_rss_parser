package lg.android.rssparser.domain;

/**
 * Created by ye on 10/9/16.
 */

public class Image {
    private String title;
    private String link;
    private String url;

    public Image() {
    }

    public Image(String title, String link, String url) {
        this.title = title;
        this.link = link;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Image{" +
                "url='" + url + '\'' +
                ", link='" + link + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}


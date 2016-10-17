package lg.android.rssparser.domain;

/**
 * Created by ye on 10/9/16.
 */

public class Item {
    private String title;
    private String link;
    private String author;
    private String category;
    private String pubDate;
    private String comments;
    private String description;

    public Item() {
    }

    public Item(String title, String link, String author, String category, String pubDate, String comments, String description) {
        this.title = title;
        this.link = link;
        this.author = author;
        this.category = category;
        this.pubDate = pubDate;
        this.comments = comments;
        this.description = description;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", comments='" + comments + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}


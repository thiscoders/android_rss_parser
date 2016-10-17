package lg.android.rssparser.domain;

import java.util.List;

/**
 * Created by ye on 10/9/16.
 */

public class Channel {
    private String title;
    private Image image;
    private String description;
    private String link;
    private String copyright;
    private String language;
    private String generator;
    private List<Item> items;

    public Channel() {
    }

    public Channel(String title, Image image, String description, String link, String copyright, String language, String generator, List<Item> items) {
        this.title = title;
        this.image = image;
        this.description = description;
        this.link = link;
        this.copyright = copyright;
        this.language = language;
        this.generator = generator;
        this.items = items;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", copyright='" + copyright + '\'' +
                ", language='" + language + '\'' +
                ", generator='" + generator + '\'' +
                '}';
    }
}


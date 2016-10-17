package lg.android.rssparser.utils;

/**
 * Created by ye on 10/9/16.
 */


import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import lg.android.rssparser.domain.Channel;
import lg.android.rssparser.domain.Image;
import lg.android.rssparser.domain.Item;



public class StreamUtils {
    private final static String tag = "StreamUtils";

    /**
     * 将inputstraem转化为String返回
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String stream2String(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int len;
        byte[] buffer = new byte[1024 * 1024];
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }

        inputStream.close();

        return new String(outputStream.toByteArray(), "GBK");
    }

    /**
     * 将xml转化为channel对象返回
     *
     * @param inputStream
     * @return
     */
    public static Channel xml2Bean(InputStream inputStream,String encoding) throws XmlPullParserException, IOException {
        if (inputStream == null) {
            Log.i(tag, "inputstream为null!");
            return null;
        }

        Channel channel = null;
        Image image = null;
        Item item = null;

        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, encoding);

        int eType = parser.getEventType();
        int flag = 0;
        //循环解析xml
        while (eType != XmlPullParser.END_DOCUMENT) {
            switch (eType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    switch (parser.getName()) {
                        case "channel":
                            channel = new Channel();
                            channel.setItems(new ArrayList<Item>());
                            break;
                        case "image":
                            image = new Image();
                            break;
                        case "item":
                            item = new Item();
                            break;
                        case "title":
                            if (flag == 0) {
                                channel.setTitle(parser.nextText());
                                flag++;
                            } else if (flag == 1) {
                                image.setTitle(parser.nextText());
                                flag++;
                            } else if (flag == 5) {
                                item.setTitle(parser.nextText());
                            }
                            break;
                        case "link":
                            if (flag == 2) {
                                image.setLink(parser.nextText());
                                flag++;
                            } else if (flag == 4) {
                                channel.setLink(parser.nextText());
                                flag++;
                            } else if (flag == 5) {
                                item.setLink(parser.nextText());
                            }
                            break;
                        case "url":
                            image.setUrl(parser.nextText());
                            break;
                        case "description":
                            if (flag == 3) {
                                channel.setDescription(parser.nextText());
                                flag++;
                            } else if (flag == 5) {
                                item.setDescription(parser.nextText());
                            }
                            break;
                        case "copyright":
                            channel.setCopyright(parser.nextText());
                            break;
                        case "language":
                            channel.setLanguage(parser.nextText());
                            break;
                        case "generator":
                            channel.setGenerator(parser.nextText());
                            break;
                        case "author":
                            item.setAuthor(parser.nextText());
                            break;
                        case "category":
                            item.setCategory(parser.nextText());
                            break;
                        case "pubDate":
                            //prolem
                            item.setPubDate(parser.nextText());
                            break;
                        case "comments":
                            item.setComments(parser.nextText());
                            break;
                    }
                    break;
                case XmlPullParser.END_TAG:
                    switch (parser.getName()) {
                        case "channel":
                            break;
                        case "image":
                            channel.setImage(image);
                            break;
                        case "item":
                            channel.getItems().add(item);
                            break;
                    }
                    break;
                case XmlPullParser.END_DOCUMENT:
                    break;
            }

            eType = parser.next();
        }

        return channel;
    }

}

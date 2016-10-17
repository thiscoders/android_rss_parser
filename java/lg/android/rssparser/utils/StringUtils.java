package lg.android.rssparser.utils;

/**
 * Created by ye on 10/17/16.
 */

public class StringUtils {

    public static String getEncoding(String title, String channeltitle) {
        String encoding = "UTF-8";

        if (title.equals("国内新闻")) {
            encoding = "GBK";
        }

        if (title.equals("行 情")) {
            encoding = "GBK";
        }

        if (channeltitle.equals("科技频道")) {
            encoding = "GB2312";
        }

        if (title.equals("滚动新闻")) {
            encoding = "GBK";
        }

        if (title.equals("大渝家居")) {
            encoding = "GBK";
        }
        if (title.equals("大渝房产")) {
            encoding = "GBK";
        }

        if (channeltitle.equals("湖北地方站")) {
            encoding = "GB2312";
        }


        return encoding;
    }

}

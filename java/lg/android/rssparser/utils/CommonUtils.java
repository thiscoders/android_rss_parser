package lg.android.rssparser.utils;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import lg.android.rssparser.activity.NewsContentActivity;

/**
 * 项目常规工具
 * Created by ye on 10/18/16.
 */

public class CommonUtils {

    /**
     * 下载网页源码
     * @param dir 上下文，获取下载目录
     * @param link 下载链接
     * @return 下载信息
     */
    public static String downWebResCode(final File dir, final String link) {
        //初始化返回信息
        String info = null;
        try {
            //创建url
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);

            int code = connection.getResponseCode();

            if (code != 200) {
                info = "资源请求失败，响应码:" + code;
                return info;
            }

            String codes = StreamUtils.stream2String(connection.getInputStream());
            String title = getDownTitle(link);
            File dest = new File(dir, title);
            FileWriter writer = new FileWriter(dest);
            writer.write(codes);
            writer.close();

            info = "源码下载完成！文件标题:" + title;
        } catch (Exception e) {
            info = "源码下载失败！" + e.toString();
        }
        return info;
    }

    private static String getDownTitle(String link) {
        return link.substring(link.lastIndexOf("/") + 1);
    }
}

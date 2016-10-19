package lg.android.rssparser.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import lg.android.rssparser.R;
import lg.android.rssparser.utils.StreamUtils;

/**
 * Created by ye on 10/18/16.
 */

public class ParserWebContentActivity extends AppCompatActivity {
    private final String tag = ParserWebContentActivity.class.getSimpleName();

    private LinearLayout ll_parent;

    private String fileTitle;
    private String link;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parserwebcontent);


        Intent intent = getIntent();
        link = intent.getStringExtra("link");

        fileTitle = getFileTitle(link);

        ll_parent = (LinearLayout) findViewById(R.id.ll_parent);

        Log.i(tag, link);

        execParser();

    }

    /**
     * jsoup开始解析htm文件
     */
    public void execParser() {
        new Thread() {
            @Override
            public void run() {
                try {
                    File dir = ParserWebContentActivity.this.getExternalFilesDir("download");
                    File res = new File(dir, fileTitle);
                    Document document = Jsoup.parse(res, "utf-8");

                    Elements elements = document.getElementsByTag("title");

                    if (elements!=null&&elements.size() > 0) {
                        Element title = elements.get(0);
                        TextView tv = new TextView(ParserWebContentActivity.this);
                        tv.setText("\t" + title.text().substring(0, title.text().indexOf("_")));
                        ll_parent.addView(tv);
                    }

                    Element element = document.getElementById("Cnt-Main-Article-QQ");
                    if(element==null){
                        throw new IOException("没有找到Cnt-Main-Article-QQ为id的标签！");
                    }
                    int i = 0;
                    for (final Element ele : element.getAllElements()) {
                        i++;
                        if (i == 1) {
                            continue;
                        }

                        if (ele.tagName().equals("img")) {
                            String imgurl = ele.attr("src");
                            URL url = new URL(imgurl);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(5000);

                            final int code = connection.getResponseCode();

                            if (code != 200) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ParserWebContentActivity.this, "图片获取失败，响应码：" + code, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            InputStream inputStream = connection.getInputStream();

                            final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ImageView iv = new ImageView(ParserWebContentActivity.this);
                                    iv.setImageBitmap(bitmap);
                                    ll_parent.addView(iv);
                                }
                            });
                        }

                        if (ele.tagName().equals("p") && !ele.text().isEmpty()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView tv = new TextView(ParserWebContentActivity.this);
                                    tv.setText("\t" + ele.text());
                                    ll_parent.addView(tv);
                                }
                            });
                        }


                    }


                } catch (final IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ParserWebContentActivity.this, "获取图片或者文字失败，" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }


            }
        }.start();
    }

    public String getFileTitle(String links) {
        return links.substring(links.lastIndexOf("/") + 1);
    }

}

package lg.android.rssparser.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

                    Element element = document.getElementById("Cnt-Main-Article-QQ");
                    int i = 0;
                    for (final Element ele : element.getAllElements()) {
                        i++;
                        if (i == 1) {
                            continue;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tv = new TextView(ParserWebContentActivity.this);
                                tv.setText(ele.tagName() + "..." + ele.text());
                                ll_parent.addView(tv);
                            }
                        });
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }.start();
    }

    public String getFileTitle(String links) {
        return links.substring(links.lastIndexOf("/") + 1);
    }

}

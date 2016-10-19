package lg.android.rssparser.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import lg.android.rssparser.R;
import lg.android.rssparser.utils.StreamUtils;

/**
 * Created by ye on 10/10/16.
 */

public class NewsContentActivity extends AppCompatActivity {
    private final String tag = NewsContentActivity.class.getSimpleName();

    private WebView wv_content;

    private String link;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newscontent);

        wv_content = (WebView) findViewById(R.id.wv_content);

        Intent intent = getIntent();
        link = intent.getStringExtra("link");

        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "在浏览器打开");
        menu.add(0, 2, 0, "解析资源");
        menu.add(0, 3, 0, "下载网页源码");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //在浏览器打开
        if (id == 1) {
            //意图
            Intent intent = new Intent();
            //意图的行为，隐式意图
            intent.setAction(Intent.ACTION_VIEW);
            //意图的数据
            intent.setData(Uri.parse(link));
            //启动
            this.startActivity(intent);
        }
        if (id == 2) {
            //判断文件是否下载
            File dir = this.getExternalFilesDir("download");
            File dest = new File(dir, getDownTitle());
            if (!dest.exists()) {
                Toast.makeText(this, "请先下载资源文件！", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }
            //打开解析界面，并传递参数
            Intent intent = new Intent(NewsContentActivity.this, ParserWebContentActivity.class);
            intent.putExtra("link", this.link);
            NewsContentActivity.this.startActivity(intent);
        }
        if (id == 3) {
            //下载源码
            downFile();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initData() {

        wv_content.loadUrl(link);

        wv_content.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });
    }


    private void downFile() {
        new Thread() {
            @Override
            public void run() {
                try {
                    final String title = getDownTitle();
                    File dest = new File(NewsContentActivity.this.getExternalFilesDir("download"), title);
                    if (dest.exists()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(NewsContentActivity.this, "源码已存在，" + title, Toast.LENGTH_LONG).show();
                                return;
                            }
                        });
                        return;
                    }

                    //创建url
                    URL url = new URL(link);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);

                    final int code = connection.getResponseCode();

                    if (code != 200) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(NewsContentActivity.this, "响应失败，响应码:" + code, Toast.LENGTH_LONG).show();
                            }
                        });
                        return;
                    }

                    String codes = StreamUtils.stream2String(connection.getInputStream());


                    FileWriter writer = new FileWriter(dest);
                    writer.write(codes);

                    writer.close();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewsContentActivity.this, "源码下载完成,文件名:" + title, Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewsContentActivity.this, "下载源码失败:" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                    return;
                }
            }
        }.start();
    }

    public String getDownTitle() {
        return this.link.substring(link.lastIndexOf("/") + 1);
    }
}

package lg.android.rssparser.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;

import lg.android.rssparser.R;
import lg.android.rssparser.utils.CommonUtils;

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
            //打开解析界面，并传递参数
            Intent intent = new Intent(NewsContentActivity.this, ParserWebContentActivity.class);
            intent.putExtra("link", this.link);
            NewsContentActivity.this.startActivity(intent);
        }
        if (id == 3) {
            new Thread() {
                String info;

                @Override
                public void run() {
                    File dir = NewsContentActivity.this.getExternalFilesDir("download");
                    //下载源码
                    info = CommonUtils.downWebResCode(dir, NewsContentActivity.this.link);
                    //通报结果
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewsContentActivity.this, info + "..." + NewsContentActivity.this.link, Toast.LENGTH_LONG).show();
                            return;
                        }
                    });
                }
            }.start();

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
}

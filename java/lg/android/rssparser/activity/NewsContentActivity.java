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

import lg.android.rssparser.R;

/**
 * Created by ye on 10/10/16.
 */

public class NewsContentActivity extends AppCompatActivity {
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
        menu.add(0, 2, 0, "复制链接");
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

package lg.android.rssparser.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.List;

import lg.android.rssparser.R;
import lg.android.rssparser.adapter.ChannelListAdapter;
import lg.android.rssparser.domain.ChannelListBean;
import lg.android.rssparser.utils.StreamUtils;

/**
 * Created by ye on 10/9/16.
 */

public class ChannelListActivity extends AppCompatActivity {
    private ListView lv_channellist;
    private List<ChannelListBean> channelList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channellist);

        lv_channellist = (ListView) findViewById(R.id.lv_channellist);

        initData();

        lv_channellist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                ChannelListBean bean = channelList.get(position);
                intent.putExtra("cont", bean.getTitle() + "..." + bean.getHref());
                setResult(10, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "刷新频道");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(20, intent);
        finish();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        new Thread() {
            @Override
            public void run() {
                try {
                    // TODO: 10/11/16 此处代码可以抽取
                    File file = new File(ChannelListActivity.this.getExternalCacheDir(), "index.shtml");

                    if (!file.exists()) {
                        URL url = new URL("http://rss.qq.com/index.shtml");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(5000);
                        final int code = connection.getResponseCode();
                        if (code != 200) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ChannelListActivity.this, "获取频道列表失败！请稍后再试！响应码:" + code, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                            return;
                        }
                        InputStream inputStream = connection.getInputStream();
                        // TODO: 10/11/16 此处可以优化
                        String content = StreamUtils.stream2String(inputStream);

                        //向file中写入内容
                        FileWriter writer = new FileWriter(file);
                        writer.write(content);
                        writer.close();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChannelListActivity.this, "频道列表下载成功！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    //html下载完成
                    channelList = new ArrayList<ChannelListBean>();
                    //开始解析本地html文档
                    Document document = Jsoup.parse(file, "UTF-8");
                    Element table = document.getElementById("rss_nav");
                    Elements as = table.getElementsByTag("a");
                    ChannelListBean channelListBean;
                    int i = 0;
                    for (Element a : as) {
                        if (i == 0) {
                            i++;
                            continue;
                        }
                        channelListBean = new ChannelListBean();
                        channelListBean.setTitle(a.text());
                        channelListBean.setHref(a.attr("href"));
                        channelList.add(channelListBean);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ChannelListAdapter adapter = new ChannelListAdapter(ChannelListActivity.this, channelList);
                            lv_channellist.setAdapter(adapter);
                        }
                    });
                } catch (final IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ChannelListActivity.this, "获取频道列表异常，请稍后再试！" + e.toString(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                    return;
                }

            }
        }.start();
    }
}

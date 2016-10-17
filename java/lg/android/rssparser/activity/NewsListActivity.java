package lg.android.rssparser.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
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
import lg.android.rssparser.adapter.NewsListAdapter;
import lg.android.rssparser.domain.NewsListBean;
import lg.android.rssparser.fragment.NewsListContentFragment;
import lg.android.rssparser.utils.StreamUtils;
import lg.android.rssparser.utils.StringUtils;

/**
 * Created by ye on 10/9/16.
 */

public class NewsListActivity extends AppCompatActivity {
    private final String tag = NewsListActivity.class.getSimpleName();
    private GridView gv_shownews;
    private List<NewsListBean> newslist;

    private float density;
    private int itemWidth;

    private String channeltitle = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newslist);

        gv_shownews = (GridView) findViewById(R.id.gv_shownews);

        //获取屏幕密度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        density = dm.density;
        //横向滚动1
        itemWidth = (int) (110 * density);
        gv_shownews.setColumnWidth(itemWidth);   //重点
        gv_shownews.setStretchMode(GridView.NO_STRETCH);

        gv_shownews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NewsListBean showbean = newslist.get(position);

                if (!showbean.getHref().endsWith("xml")) {
                    Toast.makeText(NewsListActivity.this, "本条目不是以xml结尾，解析失败！", Toast.LENGTH_SHORT).show();
                    return;
                }

                String encoding = StringUtils.getEncoding(showbean.getTitle(), channeltitle);

//                Log.i(tag, showbean.toString() + "..." + channeltitle);

                //获取manager
                FragmentManager manager = getFragmentManager();
                //开启事务
                FragmentTransaction transaction = manager.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putString("title", showbean.getTitle());
                bundle.putString("href", showbean.getHref());
                bundle.putString("encoding", encoding);
                NewsListContentFragment fragment = new NewsListContentFragment();
                fragment.setArguments(bundle);

                transaction.replace(R.id.ll_fragnewslist, fragment, "func");

                transaction.commit();

            }
        });

        getNewsList("http://rss.qq.com/news.htm");
    }


    /**
     * 创建菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        menu.add(0, 1, 0, "频道列表");
        menu.add(0, 2, 0, "收藏本页");
        menu.add(0, 3, 0, "关于软件");
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 开启频道选择页面
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == 1) {
            Intent intent = new Intent(NewsListActivity.this, ChannelListActivity.class);
            startActivityForResult(intent, 1);
        } else if (id == 3) {
            Intent intent = new Intent(NewsListActivity.this, AboutActivity.class);
            NewsListActivity.this.startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 处理返回结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10) {
            String cont = data.getStringExtra("cont");
            String[] part = cont.split("\\.\\.\\.");
            channeltitle = part[0];
            String href = part[1];
            if (href == null || href.isEmpty()) {
                return;
            }
            getNewsList(href);
        } else if (resultCode == 20) {
            Toast.makeText(NewsListActivity.this, "你尚未选择频道！", Toast.LENGTH_SHORT).show();
        }
    }

    //此处内容在横向滚动条显示
    public void getNewsList(final String href) {
        new Thread() {
            @Override
            public void run() {
                try {
                    int index = href.lastIndexOf("/");
                    File dir = new File(NewsListActivity.this.getExternalCacheDir() + "/horititles/");
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File newstitlecache = new File(dir, href.substring(index));
                    // TODO: 10/11/16 此处代码可以抽取
                    URL url = new URL(href);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    final int code = connection.getResponseCode();
                    if (code != 200) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(NewsListActivity.this, "获取横向新闻标题失败！请稍后再试！响应码:" + code, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });
                        return;
                    }
                    InputStream inputStream = connection.getInputStream();
                    // TODO: 10/11/16 此处可以优化
                    String content = StreamUtils.stream2String(inputStream);

                    //向file中写入内容
                    FileWriter writer = new FileWriter(newstitlecache);
                    writer.write(content);
                    writer.close();
                    //html下载完成


                    //jsoup获取html并进行解析
                    Document document = Jsoup.parse(newstitlecache, "UTF-8");
                    if (document == null) {
                        Toast.makeText(NewsListActivity.this, "未检测到横向新闻标题的html文件！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Elements h4s = document.getElementsByTag("h4");

                    newslist = new ArrayList<NewsListBean>();
                    NewsListBean newsListBean = null;
                    for (Element h4 : h4s) {
                        newsListBean = new NewsListBean();
                        Elements spans = h4.getElementsByTag("span");
                        for (Element span : spans) {
                            if (span.attr("id").equals("titile1")) {
                                Elements elements = span.getElementsByTag("a");
                                Element a = elements.get(0);
                                newsListBean.setTitle(a.text());
                            }
                            if (span.attr("id").equals("xml1")) {
                                Elements elements = span.getElementsByTag("a");
                                Element a = elements.get(0);
                                newsListBean.setHref(a.attr("href"));
                            }
                        }
                        newslist.add(newsListBean);
                    }

                    //设置横向滚动新闻标题条的数据适配器
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int gridviewWidth = (int) (110 * newslist.size() * density);
                            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    gridviewWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
                            gv_shownews.setLayoutParams(params);   //重点
                            gv_shownews.setNumColumns(newslist.size());   //重点

                            //gridview设置adapter
                            NewsListAdapter adapter = new NewsListAdapter(NewsListActivity.this, newslist);
                            gv_shownews.setAdapter(adapter);

                            //显示竖向新闻列表
                            NewsListBean showbean = newslist.get(0);

                            if (!showbean.getHref().endsWith("xml")) {
                                Toast.makeText(NewsListActivity.this, "本条目不是以xml结尾，解析失败！", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String encoding = StringUtils.getEncoding(showbean.getTitle(), channeltitle);

//                            Log.i(tag, showbean.toString() + "..." + channeltitle);
                            //获取manager
                            FragmentManager manager = getFragmentManager();
                            //开启事务
                            FragmentTransaction transaction = manager.beginTransaction();

                            Bundle bundle = new Bundle();
                            bundle.putString("title", showbean.getTitle());
                            bundle.putString("href", showbean.getHref());
                            bundle.putString("encoding", encoding);
                            NewsListContentFragment fragment = new NewsListContentFragment();
                            fragment.setArguments(bundle);

                            transaction.replace(R.id.ll_fragnewslist, fragment, "func");

                            transaction.commit();

                        }
                    });
                } catch (final IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewsListActivity.this, "解析htlm失败，请稍后再试！错误：" + e.toString(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                    return;
                }
            }
        }.start();
    }

}

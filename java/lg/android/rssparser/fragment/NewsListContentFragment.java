package lg.android.rssparser.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import lg.android.rssparser.R;
import lg.android.rssparser.activity.NewsContentActivity;
import lg.android.rssparser.adapter.NewsAdapter;
import lg.android.rssparser.domain.Channel;
import lg.android.rssparser.domain.Item;
import lg.android.rssparser.utils.StreamUtils;

/**
 * Created by ye on 10/9/16.
 */

public class NewsListContentFragment extends Fragment {
    private final String tag=NewsListContentFragment.class.getSimpleName();
    private ListView lv_newslist;

    private Context context;

    private String title;
    private String href;
    private String encoding;

    private NewsAdapter adapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String cont = (String) msg.obj;
            Toast.makeText(NewsListContentFragment.this.context,cont,Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context=getActivity();
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.title=args.getString("title");
        this.href=args.getString("href");
        this.encoding=args.getString("encoding");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listcontent, null);
        lv_newslist = (ListView) view.findViewById(R.id.lv_newslist);

        initUI();

        lv_newslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) adapter.getItem(position);
                Intent intent = new Intent(NewsListContentFragment.this.getActivity(), NewsContentActivity.class);
                intent.putExtra("link", item.getLink());
                startActivity(intent);
            }
        });


        return view;
    }

    public void initUI() {
        new Thread() {
            //像it之家那样展示新闻列表
            @Override
            public void run() {
                try {
                    URL url = new URL(href);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setRequestProperty("Accept-Charset", "UTF-8");

                    int code = connection.getResponseCode();
                    if (code != 200) {
                        Message message = Message.obtain();
                        message.obj = NewsListContentFragment.this.title+" 获取竖向新闻列表失败，请稍后再试！响应码:" + code;
                        handler.sendMessage(message);
                        return;
                    }

                    String encode = connection.getContentEncoding();
                    InputStream inputStream = connection.getInputStream();

                    final Channel channel = StreamUtils.xml2Bean(inputStream, NewsListContentFragment.this.encoding);

                    NewsListContentFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new NewsAdapter(NewsListContentFragment.this.getActivity(), channel);
                            lv_newslist.setAdapter(adapter);
                        }
                    });
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.obj = NewsListContentFragment.this.title+" 新闻列表解析失败，请稍后再试！" + e.toString();
                    handler.sendMessage(message);
                    return;
                }
            }
        }.start();
    }


}

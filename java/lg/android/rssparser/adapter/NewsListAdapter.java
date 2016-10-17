package lg.android.rssparser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import lg.android.rssparser.R;
import lg.android.rssparser.domain.NewsListBean;

/**
 * Created by ye on 10/9/16.
 */

public class NewsListAdapter extends BaseAdapter {
    private Context context;
    private List<NewsListBean> list;
    private TextView tv_newstitle;
    /**
     * @param context 上下问信息
     * @param list    新闻横向列表
     */
    public NewsListAdapter(Context context, List<NewsListBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return tv_newstitle;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_newslist, null);
        } else {
            view = convertView;
        }

        tv_newstitle = (TextView) view.findViewById(R.id.tv_newstitle);
        NewsListBean bean = this.list.get(position);
        tv_newstitle.setText(bean.getTitle());

        return view;
    }
}

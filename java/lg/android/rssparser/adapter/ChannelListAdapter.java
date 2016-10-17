package lg.android.rssparser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import lg.android.rssparser.R;
import lg.android.rssparser.domain.ChannelListBean;

/**
 * 频道列表的适配器
 * Created by ye on 10/9/16.
 */

public class ChannelListAdapter extends BaseAdapter {
    private Context context;
    private List<ChannelListBean> list;

    /**
     *
     * @param context 上下问信息
     * @param list 频道列表
     */
    public ChannelListAdapter(Context context,List<ChannelListBean> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.item_channellist,null);
        }else {
            view=convertView;
        }

        TextView tv_channeltitle= (TextView) view.findViewById(R.id.tv_channeltitle);
        ChannelListBean bean=this.list.get(position);


        tv_channeltitle.setText(bean.getTitle());

        return view;
    }
}

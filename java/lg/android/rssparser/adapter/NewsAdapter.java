package lg.android.rssparser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import lg.android.rssparser.R;
import lg.android.rssparser.domain.Channel;
import lg.android.rssparser.domain.Item;

/**
 * Created by ye on 10/10/16.
 */

public class NewsAdapter extends BaseAdapter{
    private Context context;
    private Channel channel;

    public NewsAdapter(Context context, Channel channel) {
        this.context = context;
        this.channel = channel;
    }


    @Override
    public int getCount() {
        return this.channel.getItems().size();
    }

    @Override
    public Object getItem(int position) {
        return this.channel.getItems().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_news, null);
        } else {
            view = convertView;
        }

        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_desc = (TextView) view.findViewById(R.id.tv_desc);
        TextView tv_pubdate = (TextView) view.findViewById(R.id.tv_pubdate);

        Item item = this.channel.getItems().get(position);

        //  System.out.println(item.toString());

        tv_title.setText(item.getTitle());
        tv_desc.setText(item.getDescription());
        tv_pubdate.setText(item.getPubDate());

        return view;
    }

}

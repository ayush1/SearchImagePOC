package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import example.com.searchimagepoc.R;


public class GridAdapter extends BaseAdapter {

    ArrayList<String> imageUrls = new ArrayList();
    Context context;

    public GridAdapter(ArrayList arrayList, Context context) {
        this.imageUrls = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if(view == null){
            view  = LayoutInflater.from(context).inflate(R.layout.row_layout, null);
        }

        ImageView imageView = (ImageView) view.findViewById(R.id.iv);

        Picasso.with(context)
                .load(imageUrls.get(i))
                .into(imageView);

        return view;
    }
}

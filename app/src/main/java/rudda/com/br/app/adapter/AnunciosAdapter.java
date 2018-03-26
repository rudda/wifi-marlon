package rudda.com.br.app.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import rudda.com.br.app.R;
import rudda.com.br.app.domain.AccessPoint;
import rudda.com.br.app.domain.Anuncio;

/**
 * Created by Rudda Beltrao on 25/03/2018.
 */

public class AnunciosAdapter extends RecyclerView.Adapter<AnunciosAdapter.holder> {

    private List<Anuncio> wifiList;
    private Context context;
    onWifiClick listener;

    public interface  onWifiClick{

        public void wifiConnect(AccessPoint w);

    }

    public AnunciosAdapter(List<Anuncio> wifiList, Context context, onWifiClick listener) {
        this.wifiList = wifiList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(this.context).inflate(R.layout.layout_wifi_item, null, false);

        return new holder(v);
    }

    @Override
    public void onBindViewHolder(holder holder, final int position) {

        holder.tv_wifi_name.setText(wifiList.get(position).getAp().getSSID());


        Uri uri = Uri.parse(this.wifiList.get(position).getPhoto());

        holder.simpleDraweeView.setImageURI(uri);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.wifiConnect(wifiList.get(position).getAp());

            }
        });

    }

    @Override
    public int getItemCount() {
        return this.wifiList.size();
    }

    class holder extends RecyclerView.ViewHolder{


        public TextView tv_wifi_name;
        public SimpleDraweeView simpleDraweeView;

        public holder(View itemView) {
            super(itemView);

            simpleDraweeView= (SimpleDraweeView) itemView.findViewById(R.id.my_image_view);
            tv_wifi_name = (TextView) itemView.findViewById(R.id.tvf_wifi_name);

        }
    }

}

package com.example.retriving_data.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retriving_data.R;
import com.example.retriving_data.models.Product;
import com.example.retriving_data.ui.activities.ProductDisplayActivity;
import com.example.retriving_data.ui.fragments.DashboardFragment;
import com.example.retriving_data.ui.fragments.ProductsFragment;
import com.example.retriving_data.utils.Constants;
import com.example.retriving_data.utils.GlideLoader;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DasboardListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private ArrayList<Product> list;
    private DashboardFragment fragment;
    public DasboardListAdapter(Context context, ArrayList<Product> list, DashboardFragment fragment) {
        this.context = context;
        this.list = list;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_item_list_layout, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object model = list.get(position);
        if (holder instanceof DasboardListAdapter.MyViewHolder) {
            new GlideLoader(context).loadProductListPicture(((Product) model).image, holder.itemView.findViewById(R.id.iv_dashboard_item_image));
            ((TextView)holder.itemView.findViewById(R.id.dashboard_item_name)).setText(((Product) model).title);
            ((TextView)holder.itemView.findViewById(R.id.dashboard_item_price)).setText("₹"+((Product) model).price + " per kg");
            ((RelativeLayout)holder.itemView.findViewById(R.id.dasboard_RL)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent doneActivity = new Intent(context, ProductDisplayActivity.class);

                    doneActivity.putExtra(Constants.Extra_Product_ID, ((Product) model).product_id);
                    context.startActivity(doneActivity);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NotNull View view) {
            super(view);
        }
    }
}

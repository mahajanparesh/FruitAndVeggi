package com.example.retriving_data.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retriving_data.R;
import com.example.retriving_data.models.Product;
import com.example.retriving_data.ui.fragments.ProductsFragment;
import com.example.retriving_data.utils.GlideLoader;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

import kotlin.jvm.internal.Intrinsics;

public class MyProductsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private ArrayList<Product> list;
    private ProductsFragment fragment;
    public MyProductsListAdapter(Context context, ArrayList<Product> list, ProductsFragment fragment) {
        this.context = context;
        this.list = list;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_layout, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object model = list.get(position);
        if (holder instanceof MyViewHolder) {
            new GlideLoader(context).loadProductListPicture(((Product) model).image, holder.itemView.findViewById(R.id.iv_item_image));
            ((TextView)holder.itemView.findViewById(R.id.tv_item_name)).setText(((Product) model).title);
            ((TextView)holder.itemView.findViewById(R.id.tv_item_price)).setText("₹"+((Product) model).price+ " per kg");
            ((ImageButton)holder.itemView.findViewById(R.id.delete_icon)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.deleteProduct(((Product) model).product_id);
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

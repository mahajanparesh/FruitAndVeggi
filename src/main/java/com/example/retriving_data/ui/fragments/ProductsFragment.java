package com.example.retriving_data.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retriving_data.R;
import com.example.retriving_data.firestore.FirestoreClass;
import com.example.retriving_data.models.Product;
import com.example.retriving_data.ui.activities.AddProductActivity;
import com.example.retriving_data.ui.activities.SettingsActivity;
import com.example.retriving_data.ui.adapters.MyProductsListAdapter;
import com.example.retriving_data.ui.home.HomeViewModel;
import com.example.retriving_data.utils.Extras;

import java.util.ArrayList;

public class ProductsFragment extends Fragment {

    //private HomeViewModel homeViewModel;
    public ProgressDialog progressDialog;
    RecyclerView rv_my_product_items;
    TextView tv_no_products_found;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel =new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_products, container, false);
        rv_my_product_items = root.findViewById(R.id.rv_my_product_items);
        tv_no_products_found = root.findViewById(R.id.tv_no_products_found);
        /*homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_product_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_add_product) {
            Intent settingsActivity = new Intent(ProductsFragment.this.getActivity(), AddProductActivity.class);
            startActivity(settingsActivity);
        }
        return super.onOptionsItemSelected(item);
    }

    public void successProductsListFromFireStore(ArrayList<Product> productsList){
        progressDialog.dismiss();
        for(Product i : productsList){
            System.out.println("Title: "+ i.title);
        }
        if(productsList.size() > 0){
            rv_my_product_items.setVisibility(View.VISIBLE);
            tv_no_products_found.setVisibility(View.GONE);

            rv_my_product_items.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv_my_product_items.setHasFixedSize(true);
            MyProductsListAdapter adapterProducts = new MyProductsListAdapter(requireActivity(),productsList,this);
            rv_my_product_items.setAdapter(adapterProducts);
        }
        else{
            rv_my_product_items.setVisibility(View.GONE);
            tv_no_products_found.setVisibility(View.VISIBLE);
        }
    }

    public void getProductsListFromFireStore(){
        progressDialog = new Extras().showProgressBar(ProductsFragment.this);
        new FirestoreClass().getProductList(this);
    }
    public void deleteProduct(String productID){
        progressDialog = new Extras().showProgressBar(ProductsFragment.this);
        new FirestoreClass().deleteProduct(this, productID);
    }
    public void productDeleteSuccess(String productID){
        progressDialog.dismiss();
        Toast.makeText(getActivity(), "Product Deleted Successfully..."+productID, Toast.LENGTH_LONG).show();
        onResume();
    }
    @Override
    public void onResume() {
        super.onResume();
        getProductsListFromFireStore();
    }
}
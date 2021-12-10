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
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retriving_data.R;
import com.example.retriving_data.firestore.FirestoreClass;
import com.example.retriving_data.models.Product;
import com.example.retriving_data.ui.activities.ProductDisplayActivity;
import com.example.retriving_data.ui.activities.SettingsActivity;
import com.example.retriving_data.ui.activities.UserProfileActivity;
import com.example.retriving_data.ui.activities.login;
import com.example.retriving_data.ui.activities.signup;
import com.example.retriving_data.ui.adapters.DasboardListAdapter;
import com.example.retriving_data.ui.adapters.MyProductsListAdapter;
import com.example.retriving_data.ui.dashboard.DashboardViewModel;
import com.example.retriving_data.utils.Constants;
import com.example.retriving_data.utils.Extras;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    //private DashboardViewModel dashboardViewModel;
    public ProgressDialog progressDialog;
    RecyclerView rv_dashboard_items;
    TextView text_dashboard;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        rv_dashboard_items = root.findViewById(R.id.rv_dashboard_items);
        text_dashboard = root.findViewById(R.id.text_dashboard);
        /*dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings) {
            Intent settingsActivity = new Intent(DashboardFragment.this.getActivity(), SettingsActivity.class);
            startActivity(settingsActivity);
        }
        return super.onOptionsItemSelected(item);
    }

    public void successDashboardListFromFireStore(ArrayList<Product> dashboardItemList){
        progressDialog.dismiss();
        if(dashboardItemList.size() > 0){
            rv_dashboard_items.setVisibility(View.VISIBLE);
            text_dashboard.setVisibility(View.GONE);

            rv_dashboard_items.setLayoutManager(new GridLayoutManager(getActivity(),2));
            rv_dashboard_items.setHasFixedSize(true);
            DasboardListAdapter adapterProducts = new DasboardListAdapter(requireActivity(),dashboardItemList, this);
            rv_dashboard_items.setAdapter(adapterProducts);
        }
        else{
            rv_dashboard_items.setVisibility(View.GONE);
            text_dashboard.setVisibility(View.VISIBLE);
        }
    }
    public void getDashboardItemFromFireStore(){
        progressDialog = new Extras().showProgressBar(DashboardFragment.this);
        new FirestoreClass().getDashboardItemsList(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDashboardItemFromFireStore();
    }
}
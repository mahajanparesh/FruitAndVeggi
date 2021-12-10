package com.example.retriving_data.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retriving_data.R;
import com.example.retriving_data.firestore.FirestoreClass;
import com.example.retriving_data.models.Product;
import com.example.retriving_data.ui.fragments.DashboardFragment;
import com.example.retriving_data.utils.Constants;
import com.example.retriving_data.utils.Extras;
import com.example.retriving_data.utils.GlideLoader;

public class ProductDisplayActivity extends AppCompatActivity {
    ImageView back_btn, productImage;
    String productId;
    public ProgressDialog progressDialog;
    Product productDetails = new Product();
    TextView title, price, instock, description, sellerName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);
        getSupportActionBar().hide();

        back_btn = findViewById(R.id.DisplayProduct_back);
        title = findViewById(R.id.Product_Title);
        price = findViewById(R.id.Product_Price);
        instock = findViewById(R.id.Product_stock);
        description = findViewById(R.id.Product_Description);
        sellerName = findViewById(R.id.Product_SellerName);
        productImage = findViewById(R.id.Product_Image);
        if (getIntent().hasExtra(Constants.Extra_Product_ID)) {
            productId = getIntent().getStringExtra(Constants.Extra_Product_ID);
            /*title.setText(productDetails.title);
            price.setText("₹"+ productDetails.price+" per Kg");
            description.setText(productDetails.description);*/
            getProductDetailsFromFireStore(productId);
        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void getProductDetailsFromFireStore(String productId){
        progressDialog = new Extras().showProgressBar(ProductDisplayActivity.this);
        new FirestoreClass().getProductDetails(this, productId);
    }
    public void productDetailsSuccess(Product product){
        progressDialog.dismiss();
        title.setText(product.title);
        price.setText("₹"+ product.price+" per Kg");
        description.setText(product.description);
        if(Integer.parseInt(product.stock_quantity) > 0 ){
            instock.setText("Product InStock");
        }
        else{
            instock.setText("Product Out of Stock");
        }
        sellerName.setText("Seller Id:"+ product.user_id);
        new GlideLoader(this).loadProductPicture(product.image, productImage);
        Toast.makeText(getApplicationContext(), product.title , Toast.LENGTH_SHORT).show();

    }
}
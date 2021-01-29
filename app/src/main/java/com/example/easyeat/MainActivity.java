package com.example.easyeat;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.easyeat.Api.Holders.CategoryHolder;
import com.example.easyeat.Api.Holders.ItemHolder;
import com.example.easyeat.Api.Holders.SubCategoryHolder;
import com.example.easyeat.Api.Tasks.RestTaskCategories;
import com.example.easyeat.Api.Tasks.RestTaskItems;
import com.example.easyeat.Api.Tasks.RestTaskSubCategories;
import com.example.easyeat.adapter.CategoryPagerAdapter;
import com.example.easyeat.adapter.ItemAdapter;
import com.example.easyeat.adapter.OrderAdapter;
import com.example.easyeat.login.Login;
import com.example.easyeat.menuactivity.AboutusActivity;
import com.example.easyeat.menuactivity.ProfileActivity;
import com.example.easyeat.model.Category;
import com.example.easyeat.model.Item;
import com.example.easyeat.model.Order;
import com.example.easyeat.model.Solution;
import com.example.easyeat.model.SubCategory;
import com.example.easyeat.model.User;
import com.example.easyeat.util.CircleAnimationUtil;
import com.steelkiwi.library.IncrementProductView;
import com.steelkiwi.library.listener.OnStateListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements ItemAdapter.IItemAdapterCallback, OrderAdapter.IOrderAdapterCallback
{

    private DrawerLayout drawer;

    private RelativeLayout rlCart;

    private TextView txtCount,txtTotal,txtClearAll;

    private RecyclerView rvOrder;

    private Button btnCompleteOrder;

    private ProgressDialog dialog;

    private OrderAdapter orderAdapter;


    /*
    * Holds all categories
    */
    private ArrayList<Category> categoryList;

    /*
    * Holds all sub-categories
    */
    private ArrayList<SubCategory> subCategoryList;

    /*
    * Holds all items
    */
    private ArrayList<Item> itemList;

    /*
    * Holds all solutions
    */
    private ArrayList<Solution> solutionList;

    /*
    * Holds the data that are added to cart
    */
    private ArrayList<Order> orderList;

    private User user;
    private CategoryHolder[] categories;
    /**
     * The callback to increase or decrease the quantity
     * of the related item at cart.
     */
    @Override
    public void onIncreaseDecreaseCallback()
    {
        updateOrderTotal();
        updateBadge();
    }

    /**
     * The callback to see the detail of the item.
     */
    @Override
    public void onItemCallback(Item item)
    {
        dialogItemDetail(item);
    }

    /**
     * The callback to add item to cart with animation.
     */
    @Override
    public void onAddItemCallback(ImageView imageView, Item item)
    {
        addItemToCartAnimation(imageView, item, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        prepareData();

        // Find views
        drawer = findViewById(R.id.dlMain);
        txtTotal = findViewById(R.id.txtTotal);
        rvOrder = findViewById(R.id.rvOrder);
        txtClearAll = findViewById(R.id.txtClearAll);
        btnCompleteOrder = findViewById(R.id.btnCompleteOrder);

        // set
        rvOrder.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        orderAdapter = new OrderAdapter(MainActivity.this, orderList);
        rvOrder.setAdapter(orderAdapter);

        // Get the ViewPager and set it's CategoryPagerAdapter so that it can display items
        ViewPager vpItem = (ViewPager) findViewById(R.id.vpItem);
        CategoryPagerAdapter categoryPagerAdapter = new CategoryPagerAdapter(getSupportFragmentManager(), MainActivity.this, solutionList);
        vpItem.setOffscreenPageLimit(categoryPagerAdapter.getCount());
        vpItem.setAdapter(categoryPagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabCategory = findViewById(R.id.tabCategory);
        tabCategory.setupWithViewPager(vpItem);

        for (int i = 0; i < tabCategory.getTabCount(); i++)
        {
            TabLayout.Tab tab = tabCategory.getTabAt(i);
            tab.setCustomView(categoryPagerAdapter.getTabView(i));
        }

        btnCompleteOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (orderList.size() > 0)
                {
                    dialogCompleteOrder();
                }
            }
        });

        txtClearAll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (orderList.size() > 0)
                {
                    dialogClearAll();
                }
            }
        });

    }



    @Override
    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.END))
        {
            drawer.closeDrawer(GravityCompat.END);
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cart, menu);

        final View actionCart = menu.findItem(R.id.actionCart).getActionView();

        txtCount = (TextView) actionCart.findViewById(R.id.txtCount);
        rlCart = (RelativeLayout) actionCart.findViewById(R.id.rlCart);

        rlCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                handleOrderDrawer();
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id ==R.id.profile)
        {
           Intent intent=new Intent(MainActivity.this, ProfileActivity.class);
           intent.putExtra("user",user);
           startActivity(intent);
           return true;
        }
        else if(id ==R.id.aboutus){
                Intent intent=new Intent(MainActivity.this, AboutusActivity.class);
                startActivity(intent);

            return true;
        }
        else if(id == R.id.logout){
                    Intent intent=new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                   return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    * Prepares sample data to be used within the application
    */
    private void prepareData()
    {
        categoryList = new ArrayList<Category>();
        subCategoryList = new ArrayList<SubCategory>();
        itemList = new ArrayList<Item>();
        solutionList = new ArrayList<Solution>();
        orderList = new ArrayList<Order>();

        CategoryHolder[] categoryHolders = null;
        SubCategoryHolder[] subCategoryHolders = null;
        ItemHolder[] itemHolders = null;
        try {
            categoryHolders = new RestTaskCategories().execute().get();
            subCategoryHolders = new RestTaskSubCategories().execute().get();
            itemHolders = new RestTaskItems().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        int[] categoryListImages = {R.drawable.chef,R.drawable.mollysdiner,R.drawable.starbucks,R.drawable.mcddo,R.drawable.pizzahutlogo};
        int[] itemListImages = {R.drawable.humbur,R.drawable.cheeseburger,R.drawable.pizzapepperoni,R.drawable.mixedpizza,R.drawable.pizza,
                R.drawable.kebab,R.drawable.shishkebab,R.drawable.gateauchocolat,R.drawable.gateaucerise,R.drawable.gateaucaramel,
                R.drawable.coffeeamer,R.drawable.cappuccino,R.drawable.miiiiiiiiiisto,R.drawable.latte,R.drawable.hottea,
                R.drawable.citrusminttea,R.drawable.icedtea,R.drawable.bigmac,R.drawable.bigtasty,R.drawable.wrapchicken,
                R.drawable.mcchicken,R.drawable.cheeseburger,R.drawable.triple,R.drawable.hamburgerluxe,R.drawable.bigmacdouble,
                R.drawable.chocoshake,R.drawable.vanillashake,R.drawable.stawberryshake,R.drawable.mcflurryoreo,R.drawable.nugget,
                R.drawable.carnivore,R.drawable.margheerita1,R.drawable.lasagnaclassico,R.drawable.jamaica,R.drawable.kitkatpops,R.drawable.strawberry,R.drawable.chocolatewaffle};
        fillCategoryList(categoryHolders,categoryListImages);
        fillSubCategoryList(subCategoryHolders);
        fillItemList(itemHolders,itemListImages);



        /*
         * Populates solutionList.
         * For each category, gets sub-categories, items and a map
         * showing the connection between the sub-category and its items.
         */
        for (Category categoryItem : categoryList)
        {
            // Temporary list of the current sıb-categories
            ArrayList<SubCategory> tempSubCategoryList = new ArrayList<>();

            // Temporary list of the current items
            ArrayList<Item> tempItemList = new ArrayList<>();

            // Temporary map
            Map<SubCategory, ArrayList<Item>> itemMap = new HashMap<SubCategory, ArrayList<Item>>();

            // categoryId == 1 means category with all items and sub-categories.
            // That's why i add all the sub-categories and items directly.
            if (categoryItem.id == 1)
            {
                itemMap = getItemMap(subCategoryList);

                solutionList.add(new Solution(categoryItem, subCategoryList, itemList, itemMap));
            }
            else
            {
                tempSubCategoryList = getSubCategoryListByCategoryId(categoryItem.id);
                tempItemList = getItemListByCategoryId(categoryItem.id);
                itemMap = getItemMap(tempSubCategoryList);

                solutionList.add(new Solution(categoryItem, tempSubCategoryList, tempItemList, itemMap));
            }
        }
    }

    private void fillItemList(ItemHolder[] itemHolders, int[] itemListImages) {
        for (int i=0;i< itemListImages.length;i++){
            itemList.add(new Item(itemHolders[i], itemListImages[i]));
        }
    }

    private void fillSubCategoryList(SubCategoryHolder[] subCategoryHolders) {
        for (SubCategoryHolder subCategoryHolder : subCategoryHolders){
            subCategoryList.add(new SubCategory(subCategoryHolder));
        }
    }

    private void fillCategoryList(CategoryHolder[] categoryHolders, int[] categoryListImages) {
        for (int i=0;i< categoryListImages.length;i++){
            categoryList.add(new Category(categoryHolders[i], categoryListImages[i]));
        }
    }

    /**
     * Gets the sub-categories belonging to a category
     *
     * @param categoryId The id of the current category.
     *
     * Returns the sub-categories belonging to that category as a list using the id of the current category.
     * @return a list of sub-category
     */
    private ArrayList<SubCategory> getSubCategoryListByCategoryId(int categoryId)
    {
        ArrayList<SubCategory> tempSubCategoryList = new ArrayList<SubCategory>();

        for (SubCategory subCategory : subCategoryList)
        {
            if (subCategory.categoryId == categoryId)
            {
                tempSubCategoryList.add(new SubCategory(subCategory));
            }
        }

        return tempSubCategoryList;
    }

    /**
     * Gets the items belonging to a category
     *
     * @param categoryId The id of the current category.
     *
     * Returns the items belonging to that category as a list using the id of the current category.
     * @return a list of items
     */
    private ArrayList<Item> getItemListByCategoryId(int categoryId)
    {
        ArrayList<Item> tempItemList = new ArrayList<Item>();

        for (Item item : itemList)
        {
            if (item.categoryId == categoryId)
            {
                tempItemList.add(item);
            }
        }

        return tempItemList;
    }

    /**
     * Gets the items belonging to a sub-category
     *
     * @param subCategoryId The id of the current sub-category.
     *
     * Returns the item belonging to that sub-category as a list
     * using the id of the current sub-category.
     * @return a list of items
     */
    private ArrayList<Item> getItemListBySubCategoryId(int subCategoryId)
    {
        ArrayList<Item> tempItemList = new ArrayList<Item>();

        for (Item item : itemList)
        {
            if (item.subCategoryId == subCategoryId)
            {
                tempItemList.add(item);
            }
        }

        return tempItemList;
    }

    /**
     * Gets a Map which has the key is the sub-category
     * and the value is the items of that sub-category.
     * That Map will also be used in the Sectioned RecyclerView.
     *
     * @param subCategoryList sub-categories which is owned by a category
     *
     * @return a Map
     */
    private Map<SubCategory, ArrayList<Item>> getItemMap(ArrayList<SubCategory> subCategoryList)
    {
        Map<SubCategory, ArrayList<Item>> itemMap = new HashMap<SubCategory, ArrayList<Item>>();

        for (SubCategory subCategory : subCategoryList)
        {
            itemMap.put(subCategory, getItemListBySubCategoryId(subCategory.id));
        }

        return itemMap;
    }

    /*
    * Shows the detail of item
    */
    private void dialogItemDetail(final Item item)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_item_detail, null);

        final IncrementProductView incrementProductView = (IncrementProductView) view.findViewById(R.id.productView);
        TextView txtItemName = (TextView) view.findViewById(R.id.txtItemName);
        final TextView txtUnitPrice = (TextView) view.findViewById(R.id.txtUnitPrice);
        final TextView txtExtendedPrice = (TextView) view.findViewById(R.id.txtExtendedPrice);
        final TextView txtQuantity = (TextView) view.findViewById(R.id.txtQuantity);
        final ImageView imgThumbnail = (ImageView) view.findViewById(R.id.imgThumbnail);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        Button btnOk = (Button) view.findViewById(R.id.btnOk);

        txtItemName.setText(item.name);
        txtUnitPrice.setText(String.format("%.2f", item.unitPrice));
        txtQuantity.setText("1");
        txtExtendedPrice.setText(String.format("%.2f", item.unitPrice * 1));

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DetailDialogAnimation;
        dialog.show();

        Glide.with(MainActivity.this)
                .load(item.url)
                .into(imgThumbnail);

        incrementProductView.getAddIcon();

        incrementProductView.setOnStateListener(new OnStateListener()
        {
            @Override
            public void onCountChange(int count)
            {
                txtQuantity.setText(String.valueOf(count));
                txtExtendedPrice.setText(String.format("%.2f", item.unitPrice * count));
            }

            @Override
            public void onConfirm(int count)
            {

            }

            @Override
            public void onClose()
            {

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addItemToCartAnimation(imgThumbnail, item, Integer.parseInt(txtQuantity.getText().toString()));

                dialog.dismiss();
            }
        });
    }

    /**
     * Animates when item is added to the cart.
     * The animation will start at item's ImageView,
     * continue up to cart MenuItem at ToolBar.
     * This will take 0.3 seconds. When the animation is over,
     * the item will be added to the cart in quantities.
     *
     * @see CircleAnimationUtil
     * @see com.example.easyeat.util.CircleImageView
     *
     * @param targetView
     * @param item element wanted to add to cart
     * @param quantity the amount of how many item you want to add to cart
     */
    private void addItemToCartAnimation(ImageView targetView, final Item item, final int quantity)
    {
        RelativeLayout destView = (RelativeLayout) findViewById(R.id.rlCart);

        new CircleAnimationUtil().attachActivity(this).setTargetView(targetView).setMoveDuration(300).setDestView(destView).setAnimationListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                addItemToCart(item, quantity);
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {
            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {
            }
        }).startAnimation();
    }

    /*
    * Adds the item to order list.
    */
    private void addItemToCart(Item item, int quantity)
    {
        boolean isAdded = false;

        for (Order order : orderList)
        {
            if (order.item.id == item.id)
            {
                //if item already added to cart, dont add new order
                //just add the quantity
                isAdded = true;
                order.quantity += quantity;
                order.extendedPrice += item.unitPrice;
                break;
            }
        }

        //if item's not added yet
        if (!isAdded)
        {
            orderList.add(new Order(item, quantity));
        }

        orderAdapter.notifyDataSetChanged();
        rvOrder.smoothScrollToPosition(orderList.size() - 1);
        updateOrderTotal();
        updateBadge();
    }

    /*
    * Updates the value of the badge
    */
    private void updateBadge()
    {
        if (orderList.size() == 0)
        {
            txtCount.setVisibility(View.INVISIBLE);
        } else
        {
            txtCount.setVisibility(View.VISIBLE);
            txtCount.setText(String.valueOf(orderList.size()));
        }
    }

    /*
    * Gets the total price of all products added to the cart
    */
    private double getOrderTotal()
    {
        double total = 0.0;

        for (Order order : orderList)
        {
            total += order.extendedPrice;
        }

        return total;
    }

    /*
    * Updates the total price of all products added to the cart
    */
    private void updateOrderTotal()
    {
        double total = getOrderTotal();
        txtTotal.setText(String.format("%.2f", total));
    }

    /*
    * Closes or opens the drawer
    */
    private void handleOrderDrawer()
    {
        if (drawer != null)
        {
            if (drawer.isDrawerOpen(GravityCompat.END))
            {
                drawer.closeDrawer(GravityCompat.END);
            } else
            {
                drawer.openDrawer(GravityCompat.END);
            }
        }
    }

    /*
    * Makes the cart empty
    */
    private void dialogClearAll()
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else
        {
            builder = new AlertDialog.Builder(MainActivity.this);
        }
        builder.setTitle(R.string.clear_all)
                .setMessage(R.string.delete_all_orders)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        clearAll();
                        showMessage(true, getString(R.string.cart_clean));
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // do nothing
                    }
                })
                .show();
    }

    /*
    * Completes the order
    */
    private void dialogCompleteOrder()
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else
        {
            builder = new AlertDialog.Builder(MainActivity.this);
        }
        builder.setTitle(getString(R.string.complete_order))
                .setMessage(getString(R.string.complete_order_question))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
//                        showProgress(true);
//                        new CompleteOrderTask().execute();
                        Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
                        intent.putExtra("orderList",orderList);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // do nothing
                    }
                })
                .show();
    }

    /**
     * Represents an asynchronous task used to complete
     * the order.
     */
    public class CompleteOrderTask extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... params)
        {
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            showProgress(false);
            clearAll();
            showMessage(true, getString(R.string.sent_order));
        }

        @Override
        protected void onCancelled()
        {
            showProgress(false);
            showMessage(false, getString(R.string.failed_order));
        }
    }

    /**
     * Shows the progress
     */
    private void showProgress(boolean show)
    {
        if (dialog == null)
        {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage(getString(R.string.sending_order));
        }

        if (show)
        {
            dialog.show();
        } else
        {
            dialog.dismiss();
        }
    }

    /*
    * Clears all orders from the cart
    */
    private void clearAll()
    {
        orderList.clear();
        orderAdapter.notifyDataSetChanged();

        updateBadge();
        updateOrderTotal();
        handleOrderDrawer();
    }

    /*
    * Shows a message by using Snackbar
    */
    private void showMessage(Boolean isSuccessful, String message)
    {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);

        if (isSuccessful)
        {
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));
        } else
        {
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPomegranate));
        }

        snackbar.show();
    }
}

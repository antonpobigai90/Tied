package com.tied.android.tiedapp.ui.activities.sales;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Revenue;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.RevenueApi;
import com.tied.android.tiedapp.ui.activities.GeneralSelectObjectActivity;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.dialogs.DatePickerFragment;
import com.tied.android.tiedapp.ui.fragments.sales.AddSalesFragment;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by femi on 8/4/2016.
 */
public class ActivityAddSales extends AppCompatActivity implements  View.OnClickListener{
    private Bundle bundle;
    private User user;
    Client client;
    String dateString="";
    Line line;
    ImageView clientPhoto;
    TextView clientNameTV, dateTV;
    EditText saleAmountET, titleET;
    float salesAmount=0.00f;
    String title="";
    Revenue revenue=new Revenue();

   // AddSalesFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_sales);

        bundle = getIntent().getExtras();
        user=MyUtils.getUserLoggedIn();
        //user = MyUtils.getUserFromBundle(bundle);
       // fragment=AddSalesFragment.newInstance(bundle);
        View getFocus=findViewById(R.id.getFocus);
        getFocus.requestFocusFromTouch();
        getFocus.setFocusableInTouchMode(true);
        getFocus.setNextFocusRightId(R.id.sale_amount);
        line = (Line) bundle.getSerializable(Constants.LINE_DATA);

        initComponent();
    }
    private void initComponent() {
      /*  FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, fragment);
        ft.commit();*/
        clientPhoto=(ImageView)findViewById(R.id.client_photo);
        saleAmountET=(EditText)findViewById(R.id.sale_amount);
        clientNameTV=(TextView)findViewById(R.id.client_name);
        dateTV=(TextView)findViewById(R.id.date_sold);
        titleET=(EditText)findViewById(R.id.title);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.green_color1));
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_client:
                selectClient(client);
                break;

            case R.id.select_date:
                DialogFragment dateFragment = new DatePickerFragment() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String month_name = MONTHS_LIST[view.getMonth()];
                        GregorianCalendar gregorianCalendar = new GregorianCalendar(view.getYear(), view.getMonth(), view.getDayOfMonth() - 1);

                        int dayOfWeek = gregorianCalendar.get(gregorianCalendar.DAY_OF_WEEK);
                        String dayOfWeekName = WEEK_LIST[dayOfWeek];

                        dateTV.setText("" + dayOfWeekName + " " + view.getDayOfMonth() + " " + month_name + " , " + view.getYear());

                       dateString = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day);
                       // TextView tv2 = (TextView) getActivity().findViewById(R.id.date_selected);
                       // tv2.setText(selected);
                    }
                };
                Bundle data = new Bundle();
                data.putString("date", dateString); //
                dateFragment.setArguments(data);
                dateFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.add:
                title=titleET.getText().toString().trim();
                if(title.isEmpty()) {
                    MyUtils.showAlert(this, "You must enter the title for this sale");
                    return;
                }
                if(client==null) {
                    MyUtils.showAlert(this, "You must choose a client");
                    return;
                }
                if(saleAmountET.getText().toString().trim().isEmpty() ) {
                    MyUtils.showAlert(this, "You must enter the sales amount");
                    return;
                }
                try {
                    salesAmount = Float.parseFloat(saleAmountET.getText().toString().trim());

                }catch (Exception e) {
                    MyUtils.showAlert(this, "You must enter a valid sales amount");
                    return;
                }
                if(dateString==null || dateString.isEmpty()) {
                    MyUtils.showAlert(this, "You must enter the sales date");
                    return;
                }

                revenue.setClient_id(client.getId());
                revenue.setValue(salesAmount);
                revenue.setTitle(title);
                revenue.setUser_id(user.getId());
                revenue.setLine_id(line.getId());
                revenue.setDate_sold(dateString);

                if(revenue.getId()!=null && !revenue.getId().isEmpty()) {
                    //update
                }else{
                    saveRevenue(revenue);
                }

                break;
        }
    }

    private void saveRevenue(final Revenue revenue) {
        RevenueApi revenueApi = MainApplication.createService(RevenueApi.class, user.getToken());
        DialogUtils.displayProgress(this);
        Call<ResponseBody> response = revenueApi.createRevenue(revenue);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                try {
                    GeneralResponse response =new GeneralResponse(resResponse.body());
                    if (response.isAuthFailed()) {
                        User.LogOut(ActivityAddSales.this);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==201) {

                        Revenue revenue2 = response.getData(Constants.REVENUE_DATA, Revenue.class);
                        Logger.write("the_line: "+revenue2.toString());
                        final Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.REVENUE_DATA, revenue2);
                        MainApplication.linesList.clear();
                        MyUtils.showMessageAlert(ActivityAddSales.this, "Revenue added!");
                        titleET.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                DialogUtils.closeProgress();
                                Intent intent = new Intent();
                                Bundle b =new Bundle();
                                 b.putSerializable("revenue", revenue);

                                intent.putExtras(b);
                                Logger.write("finishginnnnn.");
                                ActivityAddSales.this.setResult(RESULT_OK, intent);
                                ActivityAddSales.this.finishActivity(Constants.ADD_SALES);
                                ActivityAddSales.this.finish();
                            }
                        }, 2000);

                    }else{
                        MyUtils.showToast("Error encountered");
                        DialogUtils.closeProgress();
                    }

                }catch (IOException ioe) {
                    DialogUtils.closeProgress();
                    MyUtils.showToast("Error encountered. Please check your internet connection.");

                    Logger.write(ioe);
                }
                catch (Exception jo) {
                    DialogUtils.closeProgress();
                    Logger.write(jo);
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> ClientResponseCall, Throwable t) {
                Logger.write("Request failed: "+t.getCause());
                MyUtils.showConnectionErrorToast(ActivityAddSales.this);
                DialogUtils.closeProgress();
            }
        });
    }
    private void selectClient(Client client) {
        //GeneralSelectObjectActivity.setType(GeneralSelectObjectActivity.SELECT_CLIENT_TYPE, false);
       // MyUtils.startRequestActivity(this, GeneralSelectObjectActivity.class, Constants.SELECT_CLIENT);

        MyUtils.initiateClientSelector(this, client, false);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.write(requestCode+" "+resultCode);
        if(requestCode==Constants.SELECT_CLIENT && resultCode==RESULT_OK) {
            client = (Client)(data.getSerializableExtra("selected"));
            clientNameTV.setText(MyUtils.getClientName(client));
            MyUtils.Picasso.displayImage(client.getLogo(), clientPhoto);
            Logger.write(client.toString());
        }
    }
    public void goBack(View v) {
        onBackPressed();
    }
}
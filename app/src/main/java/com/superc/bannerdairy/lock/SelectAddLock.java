package com.superc.bannerdairy.lock;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.superc.bannerdairy.BR;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.adapter.AddressListAdapter;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.base.RecycleViewDivider;
import com.superc.bannerdairy.databinding.ActivitySelectAddressBinding;
import com.superc.bannerdairy.model.MAddress;
import com.superc.bannerdairy.ui.order.SelectAddressActivity;
import com.superc.bannerdairy.widget.OptionsPopupWindow;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Amorr on 2017/11/17.
 * 下订单后选择地址界面
 */

public class SelectAddLock extends BaseLock<ActivitySelectAddressBinding> implements OptionsPopupWindow.OnOptionsSelectListener {
    private List<MAddress> mGoodsList;
    public AddressListAdapter mAdapter;
    // 城区数组
    ArrayList<String> ProvinceList = new ArrayList<String>();
    ArrayList<ArrayList<String>> CityList = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<ArrayList<String>>> CountyList = new ArrayList<ArrayList<ArrayList<String>>>();
    private String area;
    private String province;
    private String city;
    private String district;
    private int state;
    private TextView cityText;
    public MAddress mMAddress;
    private int pospos=-1;
    private int mPos3=0;
    private int mPos2=0;
    private int mPos1=0;

    public SelectAddLock(Context context, ActivitySelectAddressBinding binding) {
        super(context, binding);
    }

    public SelectAddLock(Context context, ActivitySelectAddressBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        AppBarLock appBarLock = new AppBarLock(mContext, R.string.order_selctAdd);
        appBarLock.barData.isLeft = true;
        appBarLock.barData.titleLeft = "返回";
        appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);

        GridLayoutManager ms = new GridLayoutManager(mContext, 1);
        mBinding.rvAddress.setLayoutManager(ms);
        mBinding.rvAddress.addItemDecoration(new RecycleViewDivider(mContext, R.drawable.recycle_view_line));
        mGoodsList = new ArrayList<MAddress>();
        mAdapter = new AddressListAdapter(mContext, mGoodsList, R.layout.item_selectlist_address, BR.mAddress);
        mBinding.rvAddress.setAdapter(mAdapter); // 这里或者在xml里设置adapter
        if (mBundle != null) {
            String isP = mBundle.getString("isPerfect","");
            if(mBundle.getInt("pospos")!=-1&&!isP.equals("y")){
                pospos=mBundle.getInt("pospos",-1);
                mAdapter.setOnItemClickListener(new AddressListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(ViewDataBinding binding, int position) {
                        //选择地址。不是设置默认
                        for (int i = 0; i < mGoodsList.size(); i++) {
                            if (i == position) {
                                mMAddress = mGoodsList.get(i);
                                mMAddress.setSelect(true);
                                Intent intent=new Intent();
                                intent.putExtra("name_mob", mMAddress.getContact() + "     " + mMAddress.mobile);
                                intent.putExtra("position","收货地址:" + mMAddress.province+""+ mMAddress.city+ mMAddress.area+ mMAddress.address);
                                intent.putExtra("posid", mMAddress.user_address_id);
                                intent.putExtra("express_fee_e", mMAddress.getExpressFee()==null?"0":mMAddress.getExpressFee().getExpressFee());
                                intent.putExtra("expree_fee", mMAddress.getExpressFee()==null?"0":mMAddress.getExpressFee().getExpressNumFee());
                                intent.putExtra("pospos",position);
                                ((SelectAddressActivity) mContext).setResult(Activity.RESULT_OK, intent);
                                ((SelectAddressActivity)mContext).finish();
                            } else {
                                mGoodsList.get(i).setSelect(false);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
            //获取用户收货地址列表
            httpGetAddress();

        } else {
            mAdapter.setOnItemClickListener(new AddressListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ViewDataBinding binding, int position) {
                    //选择地址。不是设置默认
                    for (int i = 0; i < mGoodsList.size(); i++) {
                        if (i == position) {
                            mMAddress = mGoodsList.get(i);
                            mMAddress.setSelect(true);
                            Intent intent=new Intent();
                            intent.putExtra("name_mob", mMAddress.getContact() + "     " + mMAddress.mobile);
                            intent.putExtra("position","收货地址:" + mMAddress.province+""+ mMAddress.city+ mMAddress.area+ mMAddress.address);
                            intent.putExtra("posid", mMAddress.user_address_id);
                            intent.putExtra("pospos",position);
                            ((SelectAddressActivity) mContext).setResult(Activity.RESULT_OK, intent);
                        } else {
                            mGoodsList.get(i).setSelect(false);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            });
            //获取用户收货地址列表
            httpGetAddress();
        }
    }

    //新增收货地址
    public void addadd() {
        initDialog();
    }

    // 新增收货地址的dialog
    private Dialog initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.selectaddress_dialog, null);
        // 添加
        TextView success = (TextView) v.findViewById(R.id.success);
        //省市区
        cityText = (TextView) v.findViewById(R.id.city);
        //名字
        final EditText name = (EditText) v.findViewById(R.id.add_name);
        // 电话
        final EditText phone = (EditText) v.findViewById(R.id.add_phone);
        // 详细地址
        final EditText addree = (EditText) v.findViewById(R.id.add_add);

        builder.setView(v);
//        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        //设置背景为透明色
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        HashMap hashMap = new HashMap();
        dialog.show();
        cityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //选择省市县
                getWheelList();
                OptionsPopupWindow popupWindow = new OptionsPopupWindow(mContext);
                popupWindow.showAtLocation(cityText, Gravity.BOTTOM, 0, 0);
                popupWindow.setPicker(ProvinceList, CityList, CountyList, true);
//                popupWindow.setOnoptionsSelectListener();
                popupWindow.setOnoptionsSelectListener(new OptionsPopupWindow.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        mPos1 = options1;
                        mPos2 = option2;
                        mPos3 = options3;
                        province = ProvinceList.get(options1);
                        city = CityList.get(options1).get(option2);
                        district = CountyList.get(options1).get(option2).get(options3);
                        area = ProvinceList.get(options1) + CityList.get(options1).get(option2) + CountyList.get(options1).get(option2).get(options3);
                        cityText.setText(province + city + district);
                    }
                });
                popupWindow.setSelectOptions(mPos1,mPos2,mPos3);
                popupWindow.setCyclic(false);
            }
        });
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加收货地址
                if (name.getText().toString() == null || name.getText().toString().equals("")) {
                    showToast("请输入姓名");
                    return;
                }
                if (phone.getText().toString() == null || phone.getText().toString().equals("")) {
                    showToast("请输入电话");
                    return;
                }
                if (addree.getText().toString() == null || addree.getText().toString().equals("")) {
                    showToast("请输入详细地址");
                    return;
                }
                if (cityText.getText().toString() == null || cityText.getText().toString().equals("")) {
                    showToast("请选择地址");
                    return;
                }
                dialog.dismiss();
                httpAddAddress(phone.getText().toString(), name.getText().toString(), addree.getText().toString());

            }
        });
        return dialog;
    }

    //新增收货地址
    private void httpAddAddress(String mobile, String contact, String address) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.POSTADDRESS, RequestMethod.POST);
        Gson gson = new Gson();
        request.add("mobile", mobile);
        request.add("contact", contact);
        request.add("address", address);
        request.add("province", province);
        request.add("city", city);
        request.add("area", district);
        //设为默认地址 0是 1不是
        request.add("default", 0);
        if(MyApplication.getUser().user_id!=null) {
            request.add("creator", MyApplication.getUser().user_id);
        }

        mRequestQueue.add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 2) {
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            httpGetAddress();
                        }
                        if (show) {
                            showToast(jsonObject.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                showToast("网络异常");
            }

            @Override
            public void onFinish(int what) {
            }
        });
    }

    //获取收货地址
    private void httpGetAddress() {
        pospos=mBundle.getInt("pospos",-1);
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.GETADDRESS, RequestMethod.GET);
        Gson gson = new Gson();
        request.add("act", "list");
        if (MyApplication.getUser() != null) {
            request.add("creator", MyApplication.getUser().user_id);
        }
        mRequestQueue.add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 2) {
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            mGoodsList.clear();
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = jsonObject1.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                MAddress item = new Gson().fromJson(jsonArray.get(i).toString(), MAddress.class);
                                item.setSelect(false);
                                if(pospos!=-1&&i==pospos){
                                    item.setSelect(true);
                                }
                                mGoodsList.add(item);
                            }
                            for(MAddress m:mGoodsList) {
                                Log.e("地址信息的所有数据", m.toString()+"\n");
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                        if (show) {
                            showToast(jsonObject.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                showToast("网络异常");
            }

            @Override
            public void onFinish(int what) {
            }
        });
    }

    //省市区联动
    private void getWheelList() {
        try {
            // 获取json文件输入流
            InputStream is = mContext.getResources().getAssets().open("china_address.json");
            // 将json文件读入为一个字符串
            byte[] bytearray = new byte[is.available()];
            is.read(bytearray);
            String address_json = new String(bytearray, "UTF-8");

            // 将json转化为JSONArray对象,这是所有省的JSONArray
            JSONArray jsonArraySheng = new JSONArray(address_json);

            // 遍历这个JSONArray对象
            for (int i = 0; i < jsonArraySheng.length(); i++) {
                // 取出第i个省对象，并将其转化为JSONObject对象
                JSONObject jsonObjectSheng = jsonArraySheng.getJSONObject(i);
                // 将省的名字存入一维数组
                StringBuffer provincename = new StringBuffer(jsonObjectSheng.getString("areaName"));
                ProvinceList.add(provincename.toString());
                // 存储第i个省的城市名的数组
                ArrayList<String> tempj = new ArrayList<String>();
                // 存储第i个省的所有城市的城区名的二维数组
                ArrayList<ArrayList<String>> tempk = new ArrayList<ArrayList<String>>();
                // 取出第i个省对象中的城市数组，并将其转化为JSONArray对象
                JSONArray jsonArrayShi = jsonObjectSheng.getJSONArray("cities");
                // 遍历第i个省的城市JSONArray
                for (int j = 0; j < jsonArrayShi.length(); j++) {
                    // 取出第i个省的第j个市，并将其转化为JSONObject对象
                    JSONObject jsonObjectShi = jsonArrayShi.getJSONObject(j);
                    // 将市的名字存入第i个省的城市名数组
                    StringBuffer cityname = new StringBuffer(jsonObjectShi.getString("areaName"));
                    tempj.add(cityname.toString());
                    // 存储第i个省第j个市的城区名的数组
                    ArrayList<String> tempkk = new ArrayList<String>();
                    // 取出第i个省第j个市中的城区数组，并将其转化为JSONArray对象
                    JSONArray jsonArrayQu = jsonObjectShi.getJSONArray("counties");
                    // 遍历第i个省第j个市的城区JSONArray
                    for (int k = 0; k < jsonArrayQu.length(); k++) {
                        // 第i个省第j个市第k个区
                        JSONObject jsonObjectQu = jsonArrayQu.getJSONObject(k);
                        // 名字存入数组
                        StringBuffer countyname = new StringBuffer(jsonObjectQu.getString("areaName"));
                        tempkk.add(countyname.toString());
                    }
                    // 第i个省第j个市的城区名的数组添加到第i个省的所有城市的城区名的二维数组
                    tempk.add(tempkk);
                }
                CityList.add(tempj);
                CountyList.add(tempk);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * province   //省
     * city          //市
     * district    //区县
     *
     * @param options1
     * @param option2
     * @param options3
     */
    @Override
    public void onOptionsSelect(int options1, int option2, int options3) {
        province = ProvinceList.get(options1);
        city = CityList.get(options1).get(option2);
        district = CountyList.get(options1).get(option2).get(options3);
        area = ProvinceList.get(options1) + CityList.get(options1).get(option2)
                + CountyList.get(options1).get(option2).get(options3);
        cityText.setText(province + city + district);
    }


    //设置为默认收货地址
    private void httpSetAddAddress(String id, final int position) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.SETADDRESS, RequestMethod.GET);
        Gson gson = new Gson();
        request.add("user_address_id", id);
        if (MyApplication.getUser() != null) {
            request.add("creator", MyApplication.getUser().user_id);
        }
        mRequestQueue.add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }
            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 2) {
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            for (int i = 0; i < mGoodsList.size(); i++) {
                                mGoodsList.get(i).setSelect(false);
                                mGoodsList.get(i).setDefaultX("0");
                            }
                            mGoodsList.get(position).setDefaultX("1");
                            mGoodsList.get(position).setSelect(true);
                            mAdapter.notifyDataSetChanged();
                        }
                        if (show) {
                            showToast(jsonObject.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                showToast("网络异常");
            }

            @Override
            public void onFinish(int what) {
            }
        });
    }
}

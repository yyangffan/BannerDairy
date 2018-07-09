package com.superc.bannerdairy.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ItemSelectlistAddressBinding;
import com.superc.bannerdairy.model.MAddress;
import com.superc.bannerdairy.service.OnSomeThingClickListener;
import com.superc.bannerdairy.service.ShowRemindDialog;
import com.superc.bannerdairy.widget.OptionsPopupWindow;
import com.superc.cframework.base.ui.CBaseViewHolder;
import com.superc.cframework.utils.ToastUtil;
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
 * Created by Amorr on 2017/11/30.
 */

public class AddressListAdapter extends RecyclerView.Adapter implements OptionsPopupWindow.OnOptionsSelectListener {
    protected List<MAddress> mDataList;// 数据列表
    protected Context mContext; // 上下文
    private int layoutId; // 单布局
    private int variableId; // DataBinding的BR
    private OnItemClickListener mListener; // ItemClick监听
    private  Boolean allIsCb=false;
    private int buyNum = 1; //购买数量
    private  double price=0.00;//z总价钱
    private Boolean isSelect=false;
    private TextView cityText;
    // 城区数组
    ArrayList<String> ProvinceList = new ArrayList<String>();
    ArrayList<ArrayList<String>> CityList = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<ArrayList<String>>> CountyList = new ArrayList<ArrayList<ArrayList<String>>>();
    private String area;
    private String province;
    private String city;
    private String district;
    private int state;
    /**
     * 构造函数
     *
     * @param context 上下文
     * @param dataList 数据列表
     * @param layoutId 单布局
     * @param variableId DataBinding的BR
     */
    public AddressListAdapter(Context context, List<MAddress> dataList, int layoutId, int variableId) {
        this.mContext = context;
        this.mDataList = dataList;
        this.layoutId = layoutId;
        this.variableId = variableId;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding mBinding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                layoutId,
                parent,
                false);
        return new CBaseViewHolder(mContext, mBinding.getRoot(), mBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewDataBinding binding = DataBindingUtil.findBinding(holder.itemView);
        final ItemSelectlistAddressBinding itemBinding = (ItemSelectlistAddressBinding) binding;
        binding.setVariable(variableId, mDataList.get(position));
        itemBinding.tvName.setText(mDataList.get(position).getContact()+"   "+mDataList.get(position).getMobile());
        itemBinding.tvAddress.setText("收货地址："+mDataList.get(position).getProvince()+mDataList.get(position).getCity()+mDataList.get(position).getArea()+mDataList.get(position).getAddress());

        //判断是否默认地址
        if(mDataList.get(position).getDefaultX().equals("1")){
            // TODO: 2018/1/25  现在默认先把对勾去掉即不显示(gone)--之后需要修改的话在放出来
            itemBinding.moren.setVisibility(View.GONE);
//            itemBinding.moren.setVisibility(View.VISIBLE);
            itemBinding.checkbox.setImageDrawable(mContext.getResources().getDrawable(R.drawable.sheweimoren));

        }else {
            itemBinding.moren.setVisibility(View.GONE);
            itemBinding.checkbox.setImageDrawable(mContext.getResources().getDrawable(R.drawable.sheweimoren2));

        }
        //判断选择哪个
        isSelect=mDataList.get(position).getSelect();
        if(isSelect){
            // TODO: 2018/1/25  现在默认先把对勾去掉即不显示(gone)--之后需要修改的话在放出来
            itemBinding.moren.setVisibility(View.GONE);
//            itemBinding.moren.setVisibility(View.VISIBLE);
        }else {
            itemBinding.moren.setVisibility(View.GONE);
        }
        // 编辑
        itemBinding.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDialog(position);
            }
        });
        //删除
        itemBinding.tvDelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowRemindDialog.getInstance().showDialog(mContext,R.mipmap.babycry ,"确定删除？" ,false, new OnSomeThingClickListener() {
                    @Override
                    public void OnDoSomeThingClickListener(int view_id) {
                        httpdeleteAddAddress(position);
                    }
                });

            }
        });
        //设置默认地址
        itemBinding.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpSetAddAddress(mDataList.get(position).getUser_address_id(),position);
            }
        });
        //设置默认地址
        itemBinding.tvMoren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpSetAddAddress(mDataList.get(position).getUser_address_id(),position);
            }
        });


        subTask(binding, position);

    }

    /**
     * 其它操作
     *
     * @param binding 绑定
     * @param position 列表位置
     */
    private void subTask(final ViewDataBinding binding, final int position) {
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(binding, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 0;
        }
        return mDataList.size();
    }

    /**
     * 设置监听Item点击事件
     *
     * @param onItemClickListener 监听
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    /**
     * Item点击事件接口
     */
    public interface OnItemClickListener {
        void onItemClick(ViewDataBinding binding, int position);
    }

    // 编辑收货地址的dialog
    private Dialog initDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.selectaddress_dialog, null);
        TextView mtv_title=v.findViewById(R.id.dialog_select_title);
        mtv_title.requestFocus();
        // 修改
        TextView success= (TextView) v.findViewById(R.id.success);
        //省市区
        cityText= (TextView) v.findViewById(R.id.city);
        cityText.setText(mDataList.get(position).getProvince()+mDataList.get(position).getCity()+mDataList.get(position).getArea());
        province=mDataList.get(position).getProvince();
        city=mDataList.get(position).getCity();
        district=mDataList.get(position).getArea();
        //名字
        final EditText name= (EditText) v.findViewById(R.id.add_name);
        name.setText(mDataList.get(position).getContact());
        // 电话
        final EditText phone= (EditText) v.findViewById(R.id.add_phone);
        phone.setText(mDataList.get(position).getMobile());

        // 详细地址
        final EditText addree= (EditText) v.findViewById(R.id.add_add);
        addree.setText(mDataList.get(position).getAddress());

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
                        province = ProvinceList.get(options1);
                        city = CityList.get(options1).get(option2);
                        district = CountyList.get(options1).get(option2).get(options3);
                        area = ProvinceList.get(options1) + CityList.get(options1).get(option2)
                                + CountyList.get(options1).get(option2).get(options3);
                        cityText.setText(province + city + district);
                    }
                });
                popupWindow.setCyclic(false);
            }
        });
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //添加收货地址
                if(name.getText().toString()==null||name.getText().toString().equals("")){
                    ToastUtil.showShort(mContext, "请输入姓名");
                    return;
                }
                if(phone.getText().toString()==null||phone.getText().toString().equals("")){
                    ToastUtil.showShort(mContext, "请输入电话");

                    return;
                }
                if(addree.getText().toString()==null||addree.getText().toString().equals("")){
                    ToastUtil.showShort(mContext, "请输入详细地址");

                    return;
                }
                if(cityText.getText().toString()==null||cityText.getText().toString().equals("")){
                    ToastUtil.showShort(mContext, "请选择地址");

                    return;
                }
                dialog.dismiss();
                //修改地址
                httpAddAddress(phone.getText().toString(),name.getText().toString(),addree.getText().toString(),mDataList.get(position).getUser_address_id(),position);

            }
        });
        return dialog;
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

    //修改收货地址
    private void httpAddAddress(final String mobile, final String contact, final String address, String address_id, final int pos) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.FIXADDRESS, RequestMethod.POST);
        Gson gson = new Gson();
        request.add("mobile",mobile );
        request.add("contact",contact );
        request.add("address", address);
        request.add("province",province );
        request.add("city", city);
        request.add("area", district);
        //设为默认地址 0是 1不是
//        if(mDataList.get(pos).getDefaultX().equals("1")){
//            request.add("default", 1);
//        }else {
//            request.add("default", 0);
//        }
        if(MyApplication.getUser()!=null) {
            request.add("creator", MyApplication.getUser().user_id);
        }
        request.add("user_address_id",address_id);
        mRequestQueue.add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {


            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 2) {
                    String resultCode = null;
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
//                        resultCode = jsonObject.getString("code");
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");

//                        if (resultCode.equals("200")) {
                        if (success) {
                            ToastUtil.showShort(mContext, "修改成功");
                            MAddress mAddress = mDataList.get(pos);
                            mAddress.setMobile(mobile);
                            mAddress.setContact(contact);
                            mAddress.setAddress(address);
                            mAddress.setProvince(province);
                            mAddress.setCity(city);
                            mAddress.setArea(district);
                            AddressListAdapter.this.notifyDataSetChanged();

                        }
                        if (show) {
                            ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
//                hideLoading();
                ToastUtil.showShort(mContext, "网络异常！");


            }

            @Override
            public void onFinish(int what) {
//                hideLoading();

            }
        });
    }

    //删除收货地址
    private void httpdeleteAddAddress(final int position) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.DELETEADDRESS, RequestMethod.GET);
        Gson gson = new Gson();
        request.add("user_address_id",mDataList.get(position).getUser_address_id() );

        mRequestQueue.add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {


            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 2) {
                    String resultCode = null;
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
//                        resultCode = jsonObject.getString("code");
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");

//                        if (resultCode.equals("200")) {
                        if (success) {
                            mDataList.remove(position);
                            notifyDataSetChanged();
                        }
                        if (show) {
                            ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
//                hideLoading();
                ToastUtil.showShort(mContext, "网络异常！");


            }

            @Override
            public void onFinish(int what) {
//                hideLoading();

            }
        });
    }

    //设置为默认收货地址
    private void httpSetAddAddress(String id, final int position) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.SETADDRESS, RequestMethod.GET);
        Gson gson = new Gson();
        request.add("user_address_id",id);
        if(MyApplication.getUser()!=null){
            request.add("creator", MyApplication.getUser().user_id);
        }
        mRequestQueue.add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {


            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 2) {
                    String resultCode = null;
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
//                        resultCode = jsonObject.getString("code");
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");

//                        if (resultCode.equals("200")) {
                        if (success) {
                            for(int i=0;i<mDataList.size();i++){
                                mDataList.get(i).setDefaultX("0");
                            }
                            mDataList.get(position).setDefaultX("1");
                            notifyDataSetChanged();
                        }
                        if (show) {
                            ToastUtil.showShort(mContext, jsonObject.getString("msg"));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
//                hideLoading();
                ToastUtil.showShort(mContext, "网络异常！");

            }

            @Override
            public void onFinish(int what) {
//                hideLoading();

            }
        });
    }
}

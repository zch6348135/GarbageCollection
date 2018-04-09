package com.maruonan.garbagecollection.ui;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.maruonan.garbagecollection.R;
import com.maruonan.garbagecollection.bean.TextBean;
import com.maruonan.garbagecollection.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AppointmentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "AppointmentFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<TextBean> typeList = new ArrayList<>();
    private ArrayList<TextBean> weightList = new ArrayList<>();

    private TextView mTvType;
    private TextView mTvTime;
    private TextView mTvWeight;

    private OnFragmentInteractionListener mListener;

    public AppointmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppointmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentFragment newInstance(String param1, String param2) {
        AppointmentFragment fragment = new AppointmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        initJsonData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        LinearLayout mLLType = view.findViewById(R.id.ll_type);
        mLLType.setOnClickListener(this);
        LinearLayout mLLTime = view.findViewById(R.id.ll_time);
        mLLTime.setOnClickListener(this);
        LinearLayout mLLWeight = view.findViewById(R.id.ll_weight);
        mLLWeight.setOnClickListener(this);
        mTvType = view.findViewById(R.id.tv_type);
        mTvTime = view.findViewById(R.id.tv_time);
        mTvWeight = view.findViewById(R.id.tv_weight);
        Button mBtnSubmit = view.findViewById(R.id.btn_submit);
        mBtnSubmit.setOnClickListener(this);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //WheelView wheelView = getView().findViewById(R.id.wheelview);
            //wheelView = getActivity().findViewById(R.id.wheelview);

//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_type:
                showPickerView( mTvType, typeList, "垃圾类型选择");
                break;
            case R.id.ll_time:
                showTimeView();
                break;
            case R.id.ll_weight:
                showPickerView(mTvWeight, weightList, "重量选择");
                break;
            case R.id.btn_submit:
                if (mTvType.getText().equals("")){
                    showTip("请选择垃圾类型");
                }else if (mTvWeight.getText().equals("")){
                    showTip("请选择垃圾重量");
                }else if (mTvTime.getText().equals("")){
                    showTip("请选择预约时间");
                }else {
                    showTip("请求提交中...");
                    showTip("提交成功,请保留下方二维码，稍后会有工作人员联系您");
                    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                        fragmentTransaction.addToBackStack(null);
                    Fragment currentFragment = new QRCode();
                    fragmentTransaction.replace(R.id.frame_qrcode, currentFragment).commitAllowingStateLoss();
                }

                break;
            case R.id.btn_clear:
                mTvType.setText("");
                mTvTime.setText("");
                mTvWeight.setText("");
                break;
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void showPickerView(final TextView textView, final ArrayList<TextBean> optionsItem, String title) {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this.getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = optionsItem.get(options1).getText();
                textView.setText(tx);
            }
        })

                .setTitleText(title)
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(optionsItem);//一级选择器
        //pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        //pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }
    private void showTimeView() {
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                mTvTime.setText(getTime(date));
            }
        })
                .build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }
    private void initJsonData() {//解析数据
        String TypeData = new Utils().getJson(this.getContext(), "type.json");//获取assets目录下的json文件数据
        typeList =  new Utils().parseData(TypeData);
        String weightData = new Utils().getJson(this.getContext(), "weight.json");//获取assets目录下的json文件数据
        weightList =  new Utils().parseData(weightData);
    }
    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",java.util.Locale.getDefault());
        return format.format(date);
    }
    public void showTip(final String str) {
        Toast.makeText(this.getContext(), str, Toast.LENGTH_SHORT).show();
    }
}

package com.maruonan.garbagecollection.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.maruonan.garbagecollection.CommonValues;
import com.maruonan.garbagecollection.LoginActivity;
import com.maruonan.garbagecollection.R;
import com.maruonan.garbagecollection.bean.UserBean;

import org.litepal.crud.DataSupport;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText etName;
    private EditText etTel;
    private EditText etAddr;
    private EditText etCard;
    private UserBean DBUser;

    private Button mBtnSubmit;
    private Button mBtnLogoff;
    private OnFragmentInteractionListener mListener;

    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        etName = view.findViewById(R.id.et_info_name);
        etTel = view.findViewById(R.id.et_info_tel);
        etAddr = view.findViewById(R.id.et_info_addr);
        etCard = view.findViewById(R.id.et_info_card);
        mBtnSubmit = view.findViewById(R.id.btn_info_submit);
        mBtnSubmit.setOnClickListener(this);
        mBtnLogoff = view.findViewById(R.id.btn_logout);
        mBtnLogoff.setOnClickListener(this);
        DBUser = DataSupport.find(UserBean.class, 1);
        etName.setText(DBUser.getUsername());
        etTel.setText(DBUser.getTelNum());
        etAddr.setText(DBUser.getAddress());
        etCard.setText(DBUser.getCardNumber());
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
    public void onClick(final View view) {
        switch (view.getId()){
            case R.id.btn_info_submit:
                final String name = etName.getText().toString();
                final String tel = etTel.getText().toString();
                final String addr = etAddr.getText().toString();
                if (validate()){
                    mBtnSubmit.setEnabled(false);
                    final ProgressDialog progressDialog = new ProgressDialog(view.getContext(),
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("提交中...");
                    progressDialog.show();

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    DBUser.setUsername(name);
                                    DBUser.setTelNum(tel);
                                    DBUser.setAddress(addr);
                                    if (DBUser.save()){
                                        showTip("保存成功");
                                    }else {
                                        showTip("保存失败");
                                    }
                                    mBtnSubmit.setEnabled(true);
                                    progressDialog.dismiss();
                                }
                            }, CommonValues.DELAYMILLIS);


                }
                break;
            case R.id.btn_logout:
                mBtnLogoff.setEnabled(false);
                final ProgressDialog progressDialog = new ProgressDialog(view.getContext(),
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("注销中...");
                progressDialog.show();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                                mBtnLogoff.setEnabled(true);
                                progressDialog.dismiss();
                            }
                        }, CommonValues.DELAYMILLIS);


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
    public void showTip(final String str) {
        Toast.makeText(this.getContext(), str, Toast.LENGTH_SHORT).show();
    }
    public boolean validate() {
        boolean valid = true;

        String name = etName.getText().toString();
        String address = etAddr.getText().toString();
        String mobile = etTel.getText().toString();
        String cardNumber = etCard.getText().toString();

        if (name.isEmpty() || name.length() < 2) {
            etName.setError("至少两个字符");
            valid = false;
        } else {
            etName.setError(null);
        }

        if (address.isEmpty()) {
            etAddr.setError("住址不合法");
            valid = false;
        } else {
            etAddr.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=11) {
            etTel.setError("手机号不合法");
            valid = false;
        } else {
            etTel.setError(null);
        }

        if (cardNumber.isEmpty() || cardNumber.length()!=19) {
            etCard.setError("银行卡长度必须为19位");
            valid = false;
        } else {
            etCard.setError(null);
        }
        return valid;
    }
}

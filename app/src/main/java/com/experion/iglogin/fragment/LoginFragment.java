package com.experion.iglogin.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.experion.iglogin.R;

import com.experion.iglogin.databinding.FragmentLoginBinding;

import com.experion.iglogin.interfaces.OnButtonClickCallBack;


public class LoginFragment extends Fragment {

    private View rootView;
    private FragmentLoginBinding loginBinding;

    private Context mContext;
    private String TAG = null;


    private ClickHandler clickHandler;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loginBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_login, container, false);
        rootView = loginBinding.getRoot();
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBasics();

    }


    private void initBasics() {
        TAG = LoginFragment.class.getSimpleName();
        mContext = getActivity().getBaseContext();
        clickHandler = new ClickHandler(getActivity().getBaseContext());
        loginBinding.setHandler(clickHandler);

    }


    public class ClickHandler {
        private Context mContext;

        public ClickHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void onLoginButtonClicked(View view) {
            OnButtonClickCallBack buttonClickCallBack = (OnButtonClickCallBack) getActivity();
            buttonClickCallBack.onButtonClicked();

        }

    }


}

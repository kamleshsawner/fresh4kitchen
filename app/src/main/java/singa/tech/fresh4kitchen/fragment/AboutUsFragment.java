package singa.tech.fresh4kitchen.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import singa.tech.fresh4kitchen.R;

@SuppressLint("ValidFragment")
public class AboutUsFragment extends Fragment {

    Context ctx;
    View view = null;

    @SuppressLint("ValidFragment")
    public AboutUsFragment(Context ctx) {
        this.ctx = ctx;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, null);
        initXml();
        return view;
    }

    private void initXml() {
    }
}

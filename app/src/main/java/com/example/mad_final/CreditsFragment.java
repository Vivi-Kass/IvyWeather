package com.example.mad_final;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class CreditsFragment extends Fragment {

    private TextView tvIsaacTitle;

    private TextView tvVivianTitle;

    public CreditsFragment() {
        // Required empty public constructor
    }

    public static CreditsFragment newInstance(String param1, String param2) {
        return new CreditsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_credits, container, false);

        tvIsaacTitle = rootView.findViewById(R.id.tvIsaacTitle);
        tvVivianTitle = rootView.findViewById(R.id.tvVivianTitle);
//        tvVivianTitle.setMovementMethod(LinkMovementMethod.getInstance());
        tvIsaacTitle.setOnClickListener(new View.OnClickListener() {
            final String link = "https://github.com/IribeiroLeao2003";
            @Override
            public void onClick(View v) {
                openGitHub(v,link);
            }
        });

        tvVivianTitle.setOnClickListener(new  View.OnClickListener(){

            final String link = "https://github.com/vivi-kiwi";
            @Override
            public void onClick(View v) {
                openGitHub(v,link);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    public void openGitHub(View view, String link) {

        if (!link.startsWith("http://") && !link.startsWith("https://")) {

            link = "http://" + link;
        }

        Uri uri = Uri.parse(link);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(browserIntent);
    }
}
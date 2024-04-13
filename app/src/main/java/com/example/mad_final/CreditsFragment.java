package com.example.mad_final;

import android.content.Intent;
import android.media.Image;
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
import android.widget.ImageView;
import android.widget.TextView;

public class CreditsFragment extends Fragment {
    private final String isaacLink = "https://github.com/IribeiroLeao2003";
    private final String vivianLink = "https://github.com/vivi-kiwi";
    private final String iconCreditLink = "https://www.vecteezy.com/free-vector/weather-icons";
    private final String repoLink = "https://github.com/vivi-kiwi/MAD-Final";
    private final String APILink = "https://open-meteo.com";


    public CreditsFragment() {
        // Required empty public constructor
    }

    public static CreditsFragment newInstance() {
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

        TextView tvIsaacTitle = rootView.findViewById(R.id.tvIsaacTitle);
        TextView tvVivianTitle = rootView.findViewById(R.id.tvVivianTitle);

        ImageView appLogo = rootView.findViewById(R.id.ivHammock);
        ImageView vivianIcon = rootView.findViewById(R.id.ivVivian);
        ImageView isaacIcon = rootView.findViewById(R.id.ivIsaac);

        TextView iconCredit = rootView.findViewById(R.id.icon_credit_text);

        TextView apiCredit = rootView.findViewById(R.id.api_credits_text);

        tvIsaacTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLink(v,isaacLink);
            }
        });

        isaacIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLink(v,isaacLink);
            }
        });

        tvVivianTitle.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openLink(v,vivianLink);
            }
        });

        vivianIcon.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openLink(v,vivianLink);
            }
        });

        appLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLink(v,repoLink);
            }
        });


        iconCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLink(v,iconCreditLink);
            }
        });

        apiCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLink(v,APILink);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    public void openLink(View view, String link) {

        if (!link.startsWith("http://") && !link.startsWith("https://")) {

            link = "http://" + link;
        }

        Uri uri = Uri.parse(link);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(browserIntent);
    }
}
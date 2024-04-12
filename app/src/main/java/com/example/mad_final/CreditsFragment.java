package com.example.mad_final;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CreditsFragment extends Fragment {
    private WebView myWebView;

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
        myWebView = (WebView) rootView.findViewById(R.id.webviewGitHub);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_credits, container, false);
    }

    public void openGitHub(@NonNull View view) {

        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("https://github.com/IribeiroLeao2003");
        myWebView.setVisibility(view.VISIBLE);

    }

    public void openGitHubVivian(View view) {
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                view.setVisibility(View.VISIBLE);
            }
        });
        myWebView.loadUrl("https://github.com/vivi-kiwi");
        myWebView.setVisibility(View.VISIBLE);
    }
}
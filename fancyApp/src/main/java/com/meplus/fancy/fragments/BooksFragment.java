package com.meplus.fancy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meplus.fancy.R;
import com.meplus.fancy.adapter.BookAdapter;
import com.meplus.fancy.model.entity.Book;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BooksFragment extends BaseFragment {
    private static final String TAG = BooksFragment.class.getSimpleName();
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private final List<Book> mDatas = new ArrayList<>();
    private BookAdapter mAdapter;

    public static BooksFragment newInstance() {
        final BooksFragment fragment = new BooksFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recycler_view, null, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        final Context context = getContext();
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context).build());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter = new BookAdapter(mDatas));
    }

    public void updateBooks(List<Book> books) {
        mAdapter.setBooks(books);
    }
}

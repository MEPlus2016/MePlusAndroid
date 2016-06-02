package com.meplus.fancy.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.meplus.fancy.dataholder.DataHolder;
import com.meplus.fancy.model.entity.Book;
import com.meplus.fancy.viewholder.BookViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.common.util.ListUtils;

/**
 * 图书列表对应的Adapter
 */
public class BookAdapter extends RecyclerView.Adapter<BookViewHolder> {
    private List<DataHolder<Book>> mDataHolders = new ArrayList<>();

    public BookAdapter(List<Book> books) {
        addBooks(books);
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final BookViewHolder holder = new BookViewHolder();
        return holder;
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        holder.bindViewHolder(mDataHolders.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataHolders.size();
    }

    public void setBooks(List<Book> books) {
        mDataHolders.clear();
        addBooks(books);
    }

    public void addBooks(List<Book> books) {
        if (!ListUtils.isEmpty(books)) {
            final int size = ListUtils.getSize(books);
            for (int i = 0; i < size; i++) {
                mDataHolders.add(new DataHolder<Book>(books.get(i)));
            }
        }
        notifyDataSetChanged();
    }
}
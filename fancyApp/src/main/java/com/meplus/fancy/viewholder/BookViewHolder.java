package com.meplus.fancy.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.meplus.fancy.R;
import com.meplus.fancy.app.FancyApplication;
import com.meplus.fancy.dataholder.DataHolder;
import com.meplus.fancy.model.entity.Book;
import com.meplus.fancy.utils.ImageUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.trinea.android.common.util.TimeUtils;

public class BookViewHolder extends RecyclerView.ViewHolder {

    public static View generateItemView() {
        final LayoutInflater inflater = FancyApplication.getInstance().getLayoutInflater();
        final View itemView = inflater.inflate(R.layout.layout_item_book, null);
        return itemView;
    }

    @Bind(R.id.icon)
    ImageView mIcon;
    @Bind(R.id.author_text)
    TextView mAuthorText;
    @Bind(R.id.name_text)
    TextView mNameText;
    @Bind(R.id.isbn_text)
    TextView mISBNText;
    @Bind(R.id.date_text)
    TextView mDateText;
    @Bind(R.id.state_text)
    TextView mStateText;
    @Bind(R.id.days_text)
    TextView mDaysText;

    public BookViewHolder() {
        this(BookViewHolder.generateItemView());
    }

    public BookViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bindViewHolder(DataHolder<Book> dataHolder) {
        final Book book = dataHolder.getData();
        final Context context = itemView.getContext();
        ImageUtils.withFragmentInto(context, book.getBookCoverId(), mIcon);
        mAuthorText.setText(book.getAuthor());
        mNameText.setText(book.getBookName());
        mISBNText.setText(book.getBookISBN());
        mDateText.setText(TimeUtils.DATE_FORMAT_DATE.format(book.getActionDate()));
        mStateText.setText(book.getStatus() == 1 ? "预借" : book.getStatus() == 512 ? "已借阅" : "未知状态： " + book.getStatus());
        mDaysText.setText(String.format("过期 %1$d 天", book.getExpiredDay()));
    }
}
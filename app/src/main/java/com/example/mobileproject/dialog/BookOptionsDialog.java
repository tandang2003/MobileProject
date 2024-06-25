package com.example.mobileproject.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mobileproject.R;
import com.squareup.picasso.Picasso;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class BookOptionsDialog extends BottomSheetDialog {
    private String bookTitle;
    private String bookAuthor;
    private String bookImageUrl;
    private OnOptionClickListener onOptionClickListener;

    public BookOptionsDialog(@NonNull Context context, String bookTitle, String bookAuthor, String bookImageUrl) {
        super(context);
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookImageUrl = bookImageUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_option_dialog);

        TextView titleTextView = findViewById(R.id.bookTitle);
        TextView authorTextView = findViewById(R.id.bookAuthor);
        ImageView bookImageView = findViewById(R.id.bookImageButton);
        ImageButton aboutButton = findViewById(R.id.aboutButton);
        ImageButton likeButton = findViewById(R.id.likeButton);
        ImageButton downloadButton = findViewById(R.id.download);

        titleTextView.setText(bookTitle);
        authorTextView.setText(bookAuthor);
        Picasso.get().load(bookImageUrl).into(bookImageView);

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOptionClickListener != null) {
                    onOptionClickListener.onAboutClicked();
                }
                dismiss();
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOptionClickListener != null) {
                    onOptionClickListener.onLikeClicked();
                }
                dismiss();
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOptionClickListener != null) {
                    onOptionClickListener.onDownloadClicked();
                }
                dismiss();
            }
        });
    }

    public void setOnOptionClickListener(OnOptionClickListener listener) {
        this.onOptionClickListener = listener;
    }

    public interface OnOptionClickListener {
        void onAboutClicked();
        void onLikeClicked();
        void onDownloadClicked();
    }
}

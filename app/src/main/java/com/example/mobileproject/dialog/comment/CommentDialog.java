package com.example.mobileproject.dialog.comment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mobileproject.R;
import com.example.mobileproject.api.ApiComment;
import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.dto.request.CommentRequest;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.CommentResponse;
import com.example.mobileproject.sharedPreference.GetData;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentDialog extends DialogFragment {

    public interface CommentSubmitListener {
        void onCommentSubmit();
    }

    private CommentSubmitListener listener;
    private TextInputEditText commentEditText;
    private ImageView sendButton;

    public void setCommentSubmitListener(CommentSubmitListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        View view = inflater.inflate(R.layout.comment_bottom, container, false);
        commentEditText = view.findViewById(R.id.comment);
        sendButton = view.findViewById(R.id.sendComment);

        sendButton.setOnClickListener(v -> {
            String commentText = commentEditText.getText().toString().trim();
            if (!commentText.isEmpty()) {
                submitComment(commentText);
            }
        });

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);

            // Remove default padding
            dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
        }
    }

    private void submitComment(String commentText) {
        String token = GetData.getInstance().getToken();
        String bookId = getArguments().getString("BOOK_ID");

        if (bookId == null || bookId.isEmpty()) {
            // Handle error, bookId is null or empty
            Log.e("CommentDialog", "Book ID is null or empty");
            return;
        }

        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setContent(commentText);
        commentRequest.setBookId(Long.parseLong(bookId));

        ApiService.apiService.create(ApiComment.class).addComment("Bearer " + token, commentRequest).enqueue(new Callback<ApiResponse<CommentResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<CommentResponse>> call, Response<ApiResponse<CommentResponse>> response) {
                if (response.isSuccessful() && listener != null) {
                    listener.onCommentSubmit();
                    dismiss();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<CommentResponse>> call, Throwable t) {
                // Handle failure
                Log.e("CommentDialog", "Failed to submit comment", t);
            }
        });
    }

}

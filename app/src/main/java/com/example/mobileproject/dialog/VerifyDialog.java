package com.example.mobileproject.dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileproject.activity.ErrorDialog;
import com.example.mobileproject.activity.auth.ForgetPasswordActivity;
import com.example.mobileproject.api.ApiAuthentication;
import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.R;
import com.example.mobileproject.dto.request.ForgetPasswordRequest;
import com.example.mobileproject.dto.request.ResetPasswordRequest;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.util.Exception;
import com.example.mobileproject.util.Util;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VerifyDialog extends Dialog {
    private EditText editText1, editText2, editText3, editText4, editText5, editText6;
    private TextView countDown;
    private MaterialButton verifyBtn;
    private LinearLayout resend, descriptionCountDown;
    private boolean resendEnable;
    private int resendTime = 60;
    private String emailSend;
    private int selectedEditTextPosition = 0;
    private ImageView backBtn;

private Context context;
    public VerifyDialog(@NonNull Context context, String email) {
        super(context);
        this.context = context;
        emailSend = email;
        resendEnable = false;
        resendTime = 60;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set transparent background
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        emailSend = String.valueOf(getContext().getIntent().getBundleExtra("email"));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setContentView(R.layout.verify_dialog);
        TextView email = findViewById(R.id.email);
        email.setText(emailSend);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);
        editText6 = findViewById(R.id.editText6);

        backBtn= findViewById(R.id.backBtn);

        countDown = findViewById(R.id.countDown);
        descriptionCountDown = findViewById(R.id.countDownLayout);
        verifyBtn = findViewById(R.id.verifyBtn);
        resend = findViewById(R.id.resend);
        editText1.addTextChangedListener(textWatcher);
        editText2.addTextChangedListener(textWatcher);
        editText3.addTextChangedListener(textWatcher);
        editText4.addTextChangedListener(textWatcher);
        editText5.addTextChangedListener(textWatcher);
        editText6.addTextChangedListener(textWatcher);


        backBtn.setOnClickListener(view -> {
            VerifyDialog.this.dismiss();
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resendEnable) {
                    resend(emailSend);
                }
            }
        });
        unableBtn();
        showKeyBoard(editText1);
        startCountDownTimer();

    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 0) {
                if (selectedEditTextPosition <5) {

                    switch (selectedEditTextPosition) {
                        case 0:
                            showKeyBoard(editText2);
                            break;
                        case 1:
                            showKeyBoard(editText3);
                            break;
                        case 2:
                            showKeyBoard(editText4);
                            break;
                        case 3:
                            showKeyBoard(editText5);
                            break;
                        case 4:
                            showKeyBoard(editText6);
                            break;
                    }
                    selectedEditTextPosition++;
                    System.out.println("selectedEditTextPosition: " + selectedEditTextPosition);
                } else {
                    enableBtn();
                }

            }
        }
    };

    private void enableBtn() {
        verifyBtn.setTextColor(getContext().getResources().getColor(R.color.brown));
        verifyBtn.setStrokeColor(ColorStateList.valueOf(getContext().getResources().getColor(R.color.organe)));
        verifyBtn.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.organe)));
        verifyBtn.setOnClickListener(view -> {
            String code = editText1.getText().toString() + editText2.getText().toString() + editText3.getText().toString() + editText4.getText().toString() + editText5.getText().toString() + editText6.getText().toString();
        resetPassword(code, emailSend);
        });
    }

    private void unableBtn() {
        verifyBtn.setTextColor(getContext().getResources().getColor(R.color.brown_light));
        verifyBtn.setStrokeColor(ColorStateList.valueOf(getContext().getResources().getColor(R.color.organe_light)));
        verifyBtn.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.organe_light)));
        verifyBtn.setOnClickListener(view -> {
            Toast.makeText(VerifyDialog.this.getContext(), "Please enter the code", Toast.LENGTH_SHORT).show();
        });
    }

    private void showKeyBoard(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void startCountDownTimer() {

        new CountDownTimer(resendTime * 1000, 1000) {
            @Override
            public void onTick(long l) {
                resendTime--;
                countDown.setText(String.valueOf(resendTime));
            }

            @Override
            public void onFinish() {
                resendEnable = true;
                descriptionCountDown.setVisibility(View.GONE);
                resend.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            unableBtn();
            switch (selectedEditTextPosition-1) {
                case 0:
                    showKeyBoard(editText1);
                    break;
                case 1:
                    showKeyBoard(editText2);
                    break;
                case 2:
                    showKeyBoard(editText3);
                    break;
                case 3:
                    showKeyBoard(editText4);
                    break;
                case 4:
                    showKeyBoard(editText5);
                    break;
                case 5:
                    showKeyBoard(editText6);
                    break;
            }
            if (selectedEditTextPosition > 0) {
                selectedEditTextPosition--;
            }
            System.out.println("selectedEditTextPosition del: " + selectedEditTextPosition);
        }
        return super.onKeyUp(keyCode, event);
    }

    public void resetPassword(String token, String email) {
        ApiService.apiService.create(ApiAuthentication.class)
                .resetPassword(
                        ResetPasswordRequest.builder()
                                .email(email)
                                .token(token)
                                .build()
                ).enqueue(new Callback<ApiResponse<Void>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                        if (response.isSuccessful()) {
//                            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
//                                    .setTitle(response.body().getMessage())
////                                    .setMessage("Welcome to EBook!")
//                                    .setPositiveButton("OK", (dialogInterface, i) -> {
//                                        dialogInterface.dismiss();
//                                        VerifyDialog.this.dismiss();
//                                        ((ForgetPasswordActivity)context).finish();
//                                    })
//                                    .create();
//                            alertDialog.show();
                            ErrorDialog dialog = new ErrorDialog(getContext(), response.body().getMessage(),"Ok","Success");
//                            dialog.setBtnText("OK");
//                            dialog.setTitle("Success");
                            dialog.show();

//                            VerifyActivity.this.dismiss();

                        } else {
                            System.out.println("still");
                            ApiResponse<?> apiResponse = Util.getInstance().convertErrorBody(response.errorBody());
                            Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Void>> call, Throwable throwable) {
                        VerifyDialog.this.dismiss();
                        ErrorDialog dialog = new ErrorDialog(getContext(), Exception.FAILURE_CALL_API.getMessage());
                        dialog.show();
                    }
                });
    }


    private void resend(String email) {

        ApiService.apiService.create(ApiAuthentication.class)
                .forgetPassword(ForgetPasswordRequest.
                        builder().
                        email(email).
                        build()
                ).enqueue(new Callback<ApiResponse<Void>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {

                        if (response.isSuccessful()) {
                            resendEnable = false;
                            resendTime = 60;
                            resend.setVisibility(View.GONE);
                            descriptionCountDown.setVisibility(View.VISIBLE);
                            startCountDownTimer();
//                            Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            ApiResponse<?> apiResponse = Util.getInstance().convertErrorBody(response.errorBody());
                            Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Void>> call, Throwable throwable) {
                        ErrorDialog dialog = new ErrorDialog(getContext(), Exception.FAILURE_CALL_API.getMessage());
                        dialog.show();
                    }
                });
    }
}
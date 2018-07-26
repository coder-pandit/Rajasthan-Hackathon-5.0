package com.example.kumar.mharorajasthan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSignupDetails extends AppCompatActivity {

    public static final int RC_SIGNUP_RESULT = 10;
    public static final String DISPLAY_NAME_EXTRA = "display_name";
    public static final String DISPLAY_IMAGE_EXTRA = "display_image";

    private CircleImageView displayPhoto;
    private TextInputEditText displayName;
    private Button submitDetailsBtn;

    private Uri resultUri;
    private FirebaseUser currentUser;
    private StorageReference storeProfileImageStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup_details);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        storeProfileImageStorageRef = FirebaseStorage.getInstance()
                .getReference()
                .child("Profile_Images");

        // getting views by id
        displayName = findViewById(R.id.display_name_upload);
        displayPhoto = findViewById(R.id.display_photo_upload);
        submitDetailsBtn = findViewById(R.id.user_details_upload_btn);

        displayPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(UserSignupDetails.this);
            }
        });

        submitDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(displayName.getText().toString())) {
                    displayName.setError("Enter display name");
                    return;
                }

                final ProgressDialog dialog = new ProgressDialog(UserSignupDetails.this);
                dialog.setMessage("Uploading credentials");
                dialog.show();

                // uploading image to firebase storage
                String currentUserId = currentUser.getUid();

                final StorageReference filePath = storeProfileImageStorageRef.child(currentUserId + ".jpg");

                if (resultUri != null) {
                    filePath.putFile(resultUri)
                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        filePath.getDownloadUrl()
                                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        submitDetails(dialog, uri);
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(UserSignupDetails.this, "Unable to save your profile picture", Toast.LENGTH_SHORT)
                                                .show();
                                        dialog.dismiss();
                                    }
                                }
                            });
                } else {
                    submitDetails(dialog, currentUser.getPhotoUrl());
                }
            }
        });
    }

    private void submitDetails(final ProgressDialog dialog, final Uri uri) {
        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName.getText().toString())
                .setPhotoUri(uri)
                .build();
        currentUser.updateProfile(profile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra(DISPLAY_NAME_EXTRA, displayName.getText()
                                .toString());
                        intent.putExtra(DISPLAY_IMAGE_EXTRA, uri);
                        setResult(RC_SIGNUP_RESULT, intent);
                        finish();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                this.resultUri = resultUri;
                Picasso.get()
                        .load(resultUri)
                        .placeholder(R.drawable.default_profile)
                        .error(R.drawable.default_profile)
                        .into(displayPhoto);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d("TAG", error.getMessage());
            }
        }
    }
}

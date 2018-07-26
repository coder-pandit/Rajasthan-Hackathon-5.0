package com.example.kumar.mharorajasthan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    private boolean isEditable = false;

    private CircleImageView displayImage;
    private TextView userNameView;
    private TextView emailView;
    private TextView phoneView;
    private FloatingActionButton editButton;

    private Uri resultUri;
    private FirebaseUser currentUser;
    private DatabaseReference usersRef;
    private StorageReference storeProfileImageStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        usersRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(currentUser.getUid());
        storeProfileImageStorageRef = FirebaseStorage.getInstance()
                .getReference()
                .child("Profile_Images");

        // getting views
        displayImage = findViewById(R.id.profile_image);
        userNameView = findViewById(R.id.profile_name);
        emailView = findViewById(R.id.profile_email);
        phoneView = findViewById(R.id.profile_phone);
        editButton = findViewById(R.id.profile_edit);

        userNameView.setTag(R.id.TAG_KEY_LISTENER_ID,userNameView.getKeyListener());
        userNameView.setTag(R.id.TAG_BACKGROUND_ID, userNameView.getBackground());
        emailView.setTag(R.id.TAG_KEY_LISTENER_ID,emailView.getKeyListener());
        emailView.setTag(R.id.TAG_BACKGROUND_ID, emailView.getBackground());
        phoneView.setTag(R.id.TAG_KEY_LISTENER_ID,phoneView.getKeyListener());
        phoneView.setTag(R.id.TAG_BACKGROUND_ID, phoneView.getBackground());

        // setting editable false
        userNameView.setKeyListener(null);
        emailView.setKeyListener(null);
        phoneView.setKeyListener(null);

        // setting background null
        userNameView.setBackground(null);
        emailView.setBackground(null);
        phoneView.setBackground(null);

        userNameView.setText(currentUser.getDisplayName());

        Picasso.get()
                .load(currentUser.getPhotoUrl())
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .into(displayImage);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (currentUser.getProviders().get(0).equals("phone")) {
                    if (dataSnapshot.child("email").exists()) {
                        emailView.setText(dataSnapshot.child("email").getValue().toString());
                    }
                    phoneView.setText(currentUser.getPhoneNumber());
                } else if (currentUser.getProviders().get(0).equals("google.com")) {
                    if (dataSnapshot.child("phone").exists()) {
                        phoneView.setText(dataSnapshot.child("phone").getValue().toString());
                    }
                    emailView.setText(currentUser.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        displayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditable) {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .start(ProfileActivity.this);
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditable) {
                    if (TextUtils.isEmpty(userNameView.getText().toString())) {
                        userNameView.setError("Enter display name");
                        return;
                    }

                    final ProgressDialog dialog = new ProgressDialog(ProfileActivity.this);
                    dialog.setMessage("Uploading credentials");
                    dialog.show();

                    isEditable = false;
                    editButton.setImageResource(android.R.drawable.ic_menu_edit);

                    // setting editable false
                    userNameView.setKeyListener(null);
                    emailView.setKeyListener(null);
                    phoneView.setKeyListener(null);

                    // setting background null
                    userNameView.setBackground(null);
                    emailView.setBackground(null);
                    phoneView.setBackground(null);

                    if (currentUser.getProviders().get(0).equals("phone")) {
                        usersRef.child("email").setValue(emailView.getText().toString());
                    } else if (currentUser.getProviders().get(0).equals("google.com")) {
                        usersRef.child("phone").setValue(phoneView.getText().toString());
                    }

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
                                            Toast.makeText(ProfileActivity.this, "Unable to save your profile picture", Toast.LENGTH_SHORT)
                                                    .show();
                                            dialog.dismiss();
                                        }
                                    }
                                });
                    } else {
                        submitDetails(dialog, currentUser.getPhotoUrl());
                    }
                } else {
                    isEditable = true;
                    editButton.setImageResource(R.drawable.fui_done_check_mark);

                    // setting editable true
                    userNameView.setKeyListener((KeyListener) userNameView.getTag(R.id.TAG_KEY_LISTENER_ID));
                    userNameView.setBackground((Drawable) userNameView.getTag(R.id.TAG_BACKGROUND_ID));

                    if (currentUser.getProviders().get(0).equals("phone")) {
                        emailView.setKeyListener((KeyListener) emailView.getTag(R.id.TAG_KEY_LISTENER_ID));
                        emailView.setBackground((Drawable) emailView.getTag(R.id.TAG_BACKGROUND_ID));
                    } else if (currentUser.getProviders().get(0).equals("google.com")) {
                        phoneView.setKeyListener((KeyListener) phoneView.getTag(R.id.TAG_KEY_LISTENER_ID));
                        phoneView.setBackground((Drawable) phoneView.getTag(R.id.TAG_BACKGROUND_ID));
                    }
                }
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
                        .into(displayImage);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d("TAG", error.getMessage());
            }
        }
    }

    private void submitDetails(final ProgressDialog dialog, final Uri uri) {
        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                .setDisplayName(userNameView.getText().toString())
                .setPhotoUri(uri)
                .build();
        currentUser.updateProfile(profile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                    }
                });

        updateUi();
    }

    private void updateUi() {

    }

}

package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipientRegisterationActivity extends AppCompatActivity {

    private TextView back_btn;
    private CircleImageView profile_image;
    private TextInputEditText registerationFullName, registerationIdNumber, registerationPhoneNumber, registerationEmail, registerationPassword;
    private Spinner bloodgroupSpinner;
    private Button registerbtn;

    private Uri resultUri;
    private ProgressDialog loader;

    //firebase
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabasereference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_registeration);

        back_btn = findViewById(R.id.back_bnt);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipientRegisterationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        profile_image = findViewById(R.id.profile_image);
        registerationFullName = findViewById(R.id.registerationFullName);
        registerationIdNumber = findViewById(R.id.registerationIdNumber);
        registerationPhoneNumber = findViewById(R.id.registerationPhoneNumber);
        registerationEmail = findViewById(R.id.registerationEmail);
        registerationPassword = findViewById(R.id.registerationPassword);
        bloodgroupSpinner = findViewById(R.id.bloodgroupSpinner);
        registerbtn = findViewById(R.id.registerbtn);
        loader = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        // fetching image from gallery
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fullName = registerationFullName.getText().toString().trim();
                final String idNum = registerationIdNumber.getText().toString().trim();
                final String ph_no = registerationPhoneNumber.getText().toString().trim();
                final String email = registerationEmail.getText().toString().trim();
                final String password = registerationPassword.getText().toString().trim();
                final String bloodGroups = bloodgroupSpinner.getSelectedItem().toString();

                if (TextUtils.isEmpty(fullName)) {
                    registerationFullName.setError("Full Name is required!");
                    return;
                }
                if (TextUtils.isEmpty(idNum)) {
                    registerationIdNumber.setError("Id is required!");
                    return;
                }
                if (TextUtils.isEmpty(ph_no)) {
                    registerationPhoneNumber.setError("Phone number is required!");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    registerationEmail.setError("Email is required!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    registerationPassword.setError("Password is required!");
                    return;
                }
                if (bloodGroups.equals("Select your blood groups")) {
                    Toast.makeText(RecipientRegisterationActivity.this, "Select blood groups", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    loader.setMessage("Registering you....");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    // auth firebase
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                String error = task.getException().toString();
                                Toast.makeText(RecipientRegisterationActivity.this, "error" + error, Toast.LENGTH_SHORT).show();
                            } else {
                                String currentUserId = mAuth.getCurrentUser().getUid();
                                userDatabasereference = FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(currentUserId);
                                HashMap userInfo = new HashMap();
                                userInfo.put("id", currentUserId);
                                userInfo.put("name", fullName);
                                userInfo.put("email", email);
                                userInfo.put("bloodGroups", bloodGroups);
                                userInfo.put("idno", idNum);
                                userInfo.put("phNo", ph_no);
                                userInfo.put("type", "recipient");
                                userInfo.put("search", "recipient" + bloodGroups);

                                userDatabasereference.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(RecipientRegisterationActivity.this, "Data set Successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(RecipientRegisterationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        finish();

                                    }
                                });

                                // upload image to firebase
                                if (resultUri != null) {
                                    final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                                            .child("profile image").child(currentUserId);

                                    Bitmap bitmap = null;
                                    try {
                                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                                    byte[] data = byteArrayOutputStream.toByteArray();
                                    UploadTask uploadTask = filePath.putBytes(data);

                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RecipientRegisterationActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            if (taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null) {
                                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String imageUrl = uri.toString();
                                                        Map newImageMap = new HashMap();
                                                        newImageMap.put("profilePictureUrl", imageUrl);
                                                        userDatabasereference.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(RecipientRegisterationActivity.this, "Image Url added to Database", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(RecipientRegisterationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        finish();
                                                        ;
                                                    }
                                                });
                                            }
                                        }
                                    });
                                    Intent intent = new Intent(RecipientRegisterationActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    loader.dismiss();
                                }


                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            resultUri = data.getData();
            profile_image.setImageURI(resultUri);
        }
    }
}
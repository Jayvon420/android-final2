package com.example.projectcode;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.UUID;
import android.widget.GridView;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.ListResult;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private EditText editTextDescription;
    private Button buttonLogout;
    private FirebaseAuth mAuth;

    private FirebaseUser currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Retrieve user ID from Intent extras
        String userId = getIntent().getStringExtra("userId");

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Get the current user
        currentUser = mAuth.getCurrentUser();

        // Your other initialization code...


        buttonLogout = findViewById(R.id.buttonLogout);
        editTextDescription = findViewById(R.id.editTextDescription);
        mAuth = FirebaseAuth.getInstance();

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });



    }

    public void chooseImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void uploadImage(View view) {
        if (currentUser != null && imageUri != null) {
            String userId = currentUser.getUid();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + userId + "/" + UUID.randomUUID().toString());
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "No user logged in or no image selected", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageURI(imageUri);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    public void viewImages(View view) {
        Intent intent = new Intent(this, DisplayImagesActivity.class);
        intent.putExtra("userId", currentUser.getUid());
        startActivity(intent);
    }

}
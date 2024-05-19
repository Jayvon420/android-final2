package com.example.projectcode;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class DisplayImagesActivity extends AppCompatActivity {

    private static final String TAG = "DisplayImagesActivity";
    private List<String> imageUrls;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_images);

        GridView gridView = findViewById(R.id.gridView);
        imageUrls = new ArrayList<>();
        imageAdapter = new ImageAdapter(this, imageUrls);
        gridView.setAdapter(imageAdapter);

        fetchImages();
    }

    private void fetchImages() {
        String userId = getIntent().getStringExtra("userId");
        if (userId == null || userId.isEmpty()) {
            Log.e(TAG, "User ID is null or empty");
            return;
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images/" + userId);

        storageRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d(TAG, "Fetched image URL: " + uri.toString());
                                    imageUrls.add(uri.toString());
                                    imageAdapter.notifyDataSetChanged();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Log.e(TAG, "Failed to fetch image URL", exception);
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to list images", e);
                    }
                });
    }
}

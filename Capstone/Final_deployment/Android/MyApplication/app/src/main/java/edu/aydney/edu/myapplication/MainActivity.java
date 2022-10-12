package edu.aydney.edu.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    private ImageView imageView;

    //  util to change text view
    public void DynamicSetTextTool(int stringId, Object changeText, int viewId) {
        String RefreshTime = getResources().getString(stringId);
        String FinalRefreshTime = String.format(RefreshTime, changeText);
        TextView RefreshTextObject = findViewById(viewId);
        RefreshTextObject.setText(FinalRefreshTime);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OpenCVLoader.initDebug();
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (data != null) {
                Uri uri = data.getData();
                Log.d("MainActivity", uri.toString());
                Log.d("Result", recog.startRecog(uri.toString()).toString());
                // change the medicine
                // change the volume

                DynamicSetTextTool(R.string.medicine,
                        "this is medicine",
                        R.id.medicine);
                DynamicSetTextTool(R.string.volume,
                        123321,
                        R.id.volume);
                imageView.setImageURI(uri);
            }
        }
    }

    private void initView() {
        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(v -> {
            // https://developer.android.google.cn/guide/components/intents-filters?hl=zh-cn#Building
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            // https://developer.android.google.cn/reference/android/provider/MediaStore.Images.Media?hl=zh-cn#EXTERNAL_CONTENT_URI
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            // https://developer.android.google.cn/guide/components/intents-filters?hl=zh-cn#ExampleSend
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }
}

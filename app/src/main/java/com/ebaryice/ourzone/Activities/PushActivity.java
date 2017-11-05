package com.ebaryice.ourzone.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.ebaryice.ourzone.Basics.BaseActivity;
import com.ebaryice.ourzone.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Ebaryice on 2017/11/3.
 */

public class PushActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.toolbar_text)
    TextView textView;
    @BindView(R.id.btn_back)
    ImageButton back;
    @BindView(R.id.push_image)
    ImageView push_img;
    @BindView(R.id.push)
    Button push;
    @BindView(R.id.push_text)
    MaterialEditText push_text;

    Button choosePhoto;
    Button takePhoto;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private View inflate;
    private Dialog dialog;
    private Uri imageUri;
    private String contentText;
    private int tag = 0;
    private String imagePath = null;
    List<String> userLiked = new ArrayList<>();
    private AVUser user;
    private int isOnclick = 0;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_push;
    }

    @Override
    protected void initialize() {
        textView.setText("编辑动态");
        back.setVisibility(View.VISIBLE);
        push_img.setOnClickListener(this);
        push.setOnClickListener(this);
        user = AVUser.getCurrentUser();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.push_image:
                showDialog(view);
                break;
            case R.id.push:
                contentText = push_text.getText().toString();
                if (contentText.isEmpty() || isOnclick == 0){
                    Toast.makeText(getActivity(),"你还没有编辑完整..",Toast.LENGTH_SHORT).show();
                } else{
                    if (tag == 0){
                        try {
                            upLoad(contentText,getRealFilePath(this,imageUri));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }else if (tag == 1){
                        try {
                            upLoad(contentText,imagePath);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;

            case R.id.choosePhoto:
                tag = 1;
                if (ContextCompat.checkSelfPermission(PushActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(PushActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    openAlbum();
                }
                break;

            case R.id.takePhoto:
                tag = 0;
                File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                if (outputImage.exists()){
                    outputImage.delete();
                }
                try {
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24){
                    imageUri = FileProvider.getUriForFile(PushActivity.this,"com.ebaryice.ourzone",outputImage);
                }else{
                    imageUri = Uri.fromFile(outputImage);
                }
                // 启动相机
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
                break;
            default:
        }
    }

    private void upLoad(final String contentText, String path) throws FileNotFoundException {
        final AVFile file = AVFile.withAbsoluteLocalPath("contentImage",path);
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                AVObject object = new AVObject("Story");
                object.put("userId", user.getObjectId());
                object.put("userIcon",user.getAVFile("userIcon").getUrl());
                object.put("username",user.getString("nickname"));
                object.put("sendTime",getCurrentTime());
                object.put("contentText",contentText);
                object.put("contentImage",file.getUrl());
                object.put("likes",0);
                object.put("comments",0);
                object.put("userLiked",userLiked);
                object.saveInBackground();
            }
        });
        getActivity().finish();
    }


    private void openAlbum(){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(this,"授权失败",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void showDialog(View view){
        dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.dialog_img, null);
        //初始化控件
        choosePhoto = inflate.findViewById(R.id.choosePhoto);
        takePhoto = inflate.findViewById(R.id.takePhoto);
        choosePhoto.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.y = 50;//设置Dialog距离底部的距离
        //将属性设置给窗体
        dialogWindow.setAttributes(layoutParams);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_CANCELED){
                    isOnclick = 0;
                    Toast.makeText(PushActivity.this,"取消",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(resultCode == RESULT_OK){
                    try{
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        push_img.setImageBitmap(bitmap);
                        isOnclick = 1;
                        dialog.dismiss();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK){
                    if (Build.VERSION.SDK_INT>=19){
                        isOnclick = 1;
                        // 4.4 及以上版本系统用该方法处理图片
                        handleImageOnKitkat(data);
                    }else {
                        isOnclick = 1;
                        handleImageBeforeKitkat(data);
                    }
                    dialog.dismiss();
                }
                else if (resultCode == RESULT_CANCELED){
                    isOnclick = 0;
                    Toast.makeText(this,"取消",Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            default:
        }
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitkat(Intent data){
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的Uri，则通过document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];   //解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri,null);
        }
        displayImage(imagePath);
    }
    private void handleImageBeforeKitkat(Intent data){
        Uri uri = data.getData();
        imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if (imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            push_img.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this,"获取失败",Toast.LENGTH_SHORT).show();
        }
    }
    private String getCurrentTime(){
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    //得到真实路径,使用相机时
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}

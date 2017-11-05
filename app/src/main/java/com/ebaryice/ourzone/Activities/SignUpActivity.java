package com.ebaryice.ourzone.Activities;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.bumptech.glide.Glide;
import com.ebaryice.ourzone.Basics.BaseActivity;
import com.ebaryice.ourzone.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import butterknife.BindView;


/**
 * Created by Ebaryice on 2017/11/4.
 */

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_text)
    TextView toolbar_text;
    @BindView(R.id.btn_back)
    ImageButton back;
    @BindView(R.id.signUp)
    Button signUp;
    @BindView(R.id.signUp_icon)
    RoundedImageView signUp_icon;
    @BindView(R.id.signUp_username)
    MaterialEditText signUp_username;
    @BindView(R.id.signUp_password)
    MaterialEditText signUp_password;
    @BindView(R.id.signUp_nickname)
    MaterialEditText signUp_nickname;
    @BindView(R.id.signUp_autograph)
    MaterialEditText signUp_autograph;
    private String imagePath = null;
    public static final int CHOOSE_PHOTO = 2;
    private Bitmap bitmap;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_signup;
    }

    @Override
    protected void initialize() {
        toolbar_text.setText("注册");
        back.setVisibility(View.VISIBLE);
        signUp_icon.setOnClickListener(this);
        back.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.signUp:
                String username = signUp_username.getText().toString();
                String nickname = signUp_nickname.getText().toString();
                String password = signUp_password.getText().toString();
                String autograph = signUp_autograph.getText().toString();
                if (username.length() < 11 || nickname.length() == 0
                        || password.length()<6 || autograph.length() == 0 || signUp_icon.getDrawable().getCurrent().getConstantState()==
                        getResources().getDrawable(R.drawable.icon).getConstantState()){
                    Toast.makeText(getActivity(),"信息未完善",Toast.LENGTH_SHORT).show();
                }else{
                    signUp(username,password,nickname,autograph,bitmap);
                }
                break;
            case R.id.signUp_icon:
                openAlbum();
                break;
            default:
        }
    }

    private void openAlbum(){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }
    private void signUp(String account,String psw,String nickname,String autograph,Bitmap icon) {
        AVUser user = new AVUser();
        user.setUsername(account);
        user.setPassword(psw);
        user.put("nickname",nickname);
        user.put("autograph",autograph);
        String iconPath = saveBitmap(icon,account);
        AVFile file = null;
        try{
            file = AVFile.withAbsoluteLocalPath(account,iconPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (iconPath != null){
            user.put("userIcon",file);
        }
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null){
                    Toast.makeText(getActivity(),"注册成功,快去登录吧",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public String saveBitmap(Bitmap bitmap, String name) {
        FileOutputStream foutput = null;
        String imagePath = null;
        try {
            File appDir = new File(Environment.getExternalStorageDirectory(), "Teller");
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            File headIcons = new File(appDir, "head");
            if (!headIcons.exists()) {
                headIcons.mkdir();
            }
//            if (file.exists()){
//                file.delete();
//            }
            File file = new File(headIcons, name + ".jpg");
            foutput = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, foutput);
            imagePath = file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return imagePath;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK){
                    if (Build.VERSION.SDK_INT>=19){
                        // 4.4 及以上版本系统用该方法处理图片
                        bitmap = handleImageOnKitkat(data);
                    }else {
                        bitmap = handleImageBeforeKitkat(data);
                    }
                }
                else if (resultCode == RESULT_CANCELED){
                    Toast.makeText(this,"取消",Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            default:
        }
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private Bitmap handleImageOnKitkat(Intent data){
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
        Bitmap bitmap = displayImage(imagePath);
        return bitmap;
    }

    private Bitmap handleImageBeforeKitkat(Intent data){
        Uri uri = data.getData();
        imagePath = getImagePath(uri,null);
        Bitmap bitmap = displayImage(imagePath);
        return bitmap;
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

    private Bitmap displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            signUp_icon.setImageBitmap(bitmap);
            return bitmap;
        }
        return null;
    }
}

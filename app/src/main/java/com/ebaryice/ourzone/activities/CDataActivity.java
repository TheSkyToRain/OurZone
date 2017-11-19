package com.ebaryice.ourzone.activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.ebaryice.ourzone.R;
import com.ebaryice.ourzone.basics.BaseActivity;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import butterknife.BindView;


/**
 * Created by Ebaryice on 2017/11/18.
 */

public class CDataActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_text)
    TextView toolbar_text;
    @BindView(R.id.btn_back)
    ImageButton back;
    @BindView(R.id.change_icon)
    RoundedImageView change_icon;
    @BindView(R.id.change_nickname)
    MaterialEditText change_nickname;
    @BindView(R.id.change_autograph)
    MaterialEditText change_autograph;
    @BindView(R.id.change)
    Button change;

    private String imagePath = null;
    public static final int CHOOSE_PHOTO = 2;
    private Bitmap bitmap;
    private int isOnclick = 0;
    public static FinishSave listener;


    public interface FinishSave{
        void onFinish(String msg);
    }
    @Override
    protected int getContentViewId() {
        return R.layout.activity_change_data;
    }

    @Override
    protected void initialize() {
        toolbar_text.setText("修改资料");
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        change_icon.setOnClickListener(this);
        change.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_back:
                finish();
                break;
            case R.id.change_icon:
                openAlbum();
                break;
            case R.id.change:
                String nickname = change_nickname.getText().toString();
                String autograph = change_autograph.getText().toString();
                if (isOnclick == 0|| nickname.isEmpty()||autograph.isEmpty()) Toast.makeText(getActivity(),"信息未完善",Toast.LENGTH_SHORT).show();
                else save(nickname,autograph,bitmap);
                break;
        }
    }

    private void save(String nickname, String autograph, Bitmap bitmap){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("正在修改，请稍等");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AVUser user = AVUser.getCurrentUser();
        user.put("nickname",nickname);
        user.put("autograph",autograph);
        String iconPath = saveBitmap(bitmap,user.getString("username"));
        AVFile file = null;
        try {
            file = AVFile.withAbsoluteLocalPath(user.getString("username"),iconPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (file != null) user.put("userIcon",file);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null){
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),"保存成功",Toast.LENGTH_SHORT).show();
                    finish();
                    if (listener != null) listener.onFinish("ok");
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public String saveBitmap(Bitmap bitmap, String name) {
        FileOutputStream foutput = null;
        String imagePath = null;
        try {
            File appDir = new File(Environment.getExternalStorageDirectory(), "Zone");
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

    private void openAlbum(){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK){
                    isOnclick = 1;
                    if (Build.VERSION.SDK_INT>=19){
                        // 4.4 及以上版本系统用该方法处理图片
                        bitmap = handleImageOnKitkat(data);
                    }else {
                        bitmap = handleImageBeforeKitkat(data);
                    }
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
            change_icon.setImageBitmap(bitmap);
            return bitmap;
        }
        return null;
    }
}

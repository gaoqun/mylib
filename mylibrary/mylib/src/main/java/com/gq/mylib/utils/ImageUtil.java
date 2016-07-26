package com.gq.mylib.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Lenovo on 2016/4/14.
 */
public class ImageUtil {
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        LogUtil.d("" + options.inSampleSize);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;


        return BitmapFactory.decodeFile(filePath, options);
    }

    //把bitmap转换成String
    public static String bitmapToString(String filePath) {
        Bitmap bm = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap getSmallBitmap(Uri filePath, Context context) {
        Bitmap bitmap = null;
        final BitmapFactory.Options options = new BitmapFactory.Options();

        ContentResolver cr = context.getContentResolver();
        try {
            InputStream input = cr.openInputStream(filePath);

            options.inSampleSize = 5;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(input, null, options);
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void saveImage(File file, Bitmap bitmap) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (out == null) {
                return;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static File savePhoto(Bitmap headImg, String name) {

        String path = Environment.getExternalStorageDirectory() + "/waterMelon";
        File f = new File(path);

        if (!f.exists()) {
            f.mkdir();
        }
        if (f != null) {
            for (File fileDel : f.listFiles()) {// 删除遗留的相片保证用户sd卡只有一张temp图
                if (null != fileDel)
                    if (fileDel.toString().endsWith(name)) {
                        fileDel.delete();
                    }
            }
        }
        String headName = System.currentTimeMillis() + name;
        String headPath = Environment.getExternalStorageDirectory() + "/waterMelon" + "/"
                + headName;
        // String upload = "/img/" + headName;
        File file = new File(headPath);
        // System.out.println("图片保存到sd卡" + headPath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fos != null) {

            headImg.compress(Bitmap.CompressFormat.JPEG, 75, fos);
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 将头像路径、上传路径保存到userInfo

        return file;
    }

    public static void saveImageToGallery(Context context, Bitmap bmp) throws IOException {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File appDir = new File(Environment.getExternalStorageDirectory(), "com.wiseweb");
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            File file = new File(appDir, fileName);

            FileOutputStream fos = new FileOutputStream(file);
            LogUtil.d("保存前");
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            LogUtil.d("保存后");
            fos.flush();
            fos.close();


//            // 其次把文件插入到系统图库

            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);

//            // 最后通知图库更新
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            context.sendBroadcast(intent);//


        } else {
            throw (new IOException());
        }
    }
}

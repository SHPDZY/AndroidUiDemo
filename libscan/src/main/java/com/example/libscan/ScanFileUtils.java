package com.example.libscan;

import static android.os.Environment.MEDIA_MOUNTED;
import static android.os.Environment.getExternalStorageDirectory;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ScanFileUtils {
    static final String TAG = ScanFileUtils.class.getName();


    public static final String BOOHEE_DIR = "zy_scan";
    public static final String IMAGES_DIR = "images";
    public static final String TEMP_IMAGE = "temp.jpg";

    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String HASH_ALGORITHM = "MD5";
    private static final int RADIX = 10 + 26; // 10 digits + 26 letters

    /*
     * 存储系统相机拍照的图片
     */
    public static File saveCameraImage(Bitmap btp) {
        if (btp == null) {
            return null;
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File tempFile = new File(getImagesDir(), fileName);
        FileOutputStream fileOut = null;
        try {
            // 将bitmap转为jpg文件保存
            fileOut = new FileOutputStream(tempFile);
            btp.compress(CompressFormat.JPEG, 100, fileOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return tempFile;
        } finally {
            try {
                if (fileOut != null) {
                    fileOut.flush();
                    fileOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return tempFile;
            }
        }
        return tempFile;
    }

    public static File saveCameraImagePng(Bitmap btp) {
        if (btp == null) {
            return null;
        }
        String fileName = System.currentTimeMillis() + ".png";
        File tempFile = new File(getImagesDir(), fileName);
        FileOutputStream fileOut = null;
        try {
            // 将bitmap转为jpg文件保存
            fileOut = new FileOutputStream(tempFile);
            btp.compress(CompressFormat.PNG, 100, fileOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return tempFile;
        } finally {
            try {
                if (fileOut != null) {
                    fileOut.flush();
                    fileOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return tempFile;
            }
        }
        return tempFile;
    }

    /**
     * 保存图片到本地并加入图库
     *
     * @param context
     * @param bmp
     * @param fileName
     * @return
     */
    public static String downloadImage2Gallery(Context context, Bitmap bmp, String fileName) {
        String path = null;
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            File file = saveImage(context, bmp, fileName);
            if (file != null && file.exists()) {
                path = file.getAbsolutePath();
                // 发广播更新图库
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri
                        .parse("file://" + path)));
            }
        }
        return path;
    }

    /**
     * 保存图片到本地并加入图库
     *
     * @param context
     * @param bmp
     * @param fileName
     * @return
     */
    public static String downloadImage2Gallery2(Context context, Bitmap bmp, String fileName) {
        String path = null;
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            File file = saveImage2(context, bmp, fileName);
            if (file != null && file.exists()) {
                path = file.getAbsolutePath();
                // 发广播更新图库
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri
                        .parse("file://" + path)));
            }
        }
        return path;
    }

    /**
     * 保存图片到本地并加入图库
     *
     * @param context
     * @param bmp
     * @param fileName
     * @return
     */
    public static File getPNGImageFile(Context context, Bitmap bmp, String fileName) {
        File file = null;
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            file = saveImage(context, bmp, fileName);
        }
        return file;
    }


    /**
     * 保存bitmap到图片并插入到图库
     *
     * @param context
     * @param bmp
     * @param fileName
     * @return
     */
    public static File saveImage(Context context, Bitmap bmp, String fileName) {
        File appDir = new File(getExternalStorageDirectory(), "Boohee");
        if (appDir != null && !appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }

    /**
     * 保存bitmap到图片
     *
     * @param context
     * @param bmp
     * @param fileName
     * @return
     */
    public static File saveImage2(Context context, Bitmap bmp, String fileName) {
        File appDir = new File(getExternalStorageDirectory(), "Boohee");
        if (appDir != null && !appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }

    /**
     * 插入图库并通知更新
     *
     * @param context
     * @param file
     */
    public static void insertToGalleryAndNotify(Context context, File file) {
        if (file == null || !file.exists()) return;
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri
                .parse("file://" + file.getAbsolutePath())));
//        try {
//            Helper.showLog("已通知图库更新");
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    file.getAbsolutePath(), file.getName(), null);
//            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri
//                    .parse("file://" + file.getAbsolutePath())));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }


    public static File saveFile(Context context, File sourceFile, String fileName) {
        File appDir = new File(getExternalStorageDirectory(), "Boohee");
        if (appDir != null && !appDir.exists()) {
            appDir.mkdir();
        }
        String type = getImageType(sourceFile);
        if (type != null && (type.equals("PNG") || type.equals("png"))) {
            File file = new File(appDir, fileName + ".jpeg");
            convertToJpg(sourceFile.getPath(), file.getPath());
            return appDir;
        }

        try {
            File file = new File(appDir, fileName + "." + type);
            copyFile(sourceFile, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appDir;
    }

    public static void convertToJpg(String pngFilePath, String jpgFilePath) {
        Bitmap bitmap = drawBg4Bitmap(Color.parseColor("#ffffff"), BitmapFactory.decodeFile(pngFilePath));
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(jpgFilePath))) {
            if (bitmap.compress(CompressFormat.JPEG, 80, bos)) {
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap drawBg4Bitmap(int color, Bitmap orginBitmap) {
        Paint paint = new Paint();
        paint.setColor(color);
        Bitmap bitmap = Bitmap.createBitmap(orginBitmap.getWidth(),
                orginBitmap.getHeight(), orginBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, orginBitmap.getWidth(), orginBitmap.getHeight(), paint);
        canvas.drawBitmap(orginBitmap, 0, 0, paint);
        return bitmap;
    }

    public static void copyFile(File src, File dest) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            if (!dest.exists()) {
                dest.createNewFile();
            }
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dest).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }


    public static File saveImage(Context context, File sourceFile, String fileName) {
        File appDir = new File(getExternalStorageDirectory(), "Boohee");
        if (appDir != null && !appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName + ".jpg");
        try {
            copyFile(sourceFile, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appDir;
    }

    /**
     * 保存bitmap到图片并插入到图库
     *
     * @param context
     * @param bmp
     * @param fileName
     * @return
     */
    public static File savePNGImage(Context context, Bitmap bmp, String fileName) {
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) return null;
        File appDir = new File(cacheDir, "Boohee");
        if (appDir != null && !appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName + ".png");
        if (file.exists()) file.delete();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }


    /**
     * 删除文件
     *
     * @param fileName
     */
    public static void delFile(String fileName) {
        File file = new File(fileName);
        delFile(file);
    }

    public static void delFile(File file) {
        try {
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {

        }

    }

    /**
     * 删除文件夹
     *
     * @param path
     */
    public static void deleteAllFilesOfDir(File path) {
        if (!path.exists())
            return;
        if (path.isFile()) {
            path.delete();
            return;
        }
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteAllFilesOfDir(files[i]);
        }
        path.delete();
    }

    /**
     * 检查有没有权限
     *
     * @param context
     * @return
     */
    public static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 保存字符串到文件
     */
    public static void saveStrToAPP(String contentString, String dir, String fileName) {
        File saveFile = new File(dir, fileName);
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(saveFile);
            outStream.write(contentString.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long 单位为K
     * @throws Exception
     */
    public static long getFolderSize(File file) {
        try {
            long size = 0;
            if (!file.exists()) {
                return size;
            } else if (!file.isDirectory()) {
                return file.length() / 1024;
            }
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + 1024 * getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
            return size / 1024;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static void copyFileWithWrite(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    /**
     * 剪切文件夹
     *
     * @param srcDir
     * @param destDir
     */
    public static void removeDirectoryToAnother(File srcDir, File destDir) {
        if (srcDir != null && srcDir.exists() && srcDir.isDirectory()) {
            srcDir.renameTo(destDir);
        }
    }

    /**
     * 文件夹复制 A/ -> B/
     *
     * @param sourceLocation A/
     * @param targetLocation B/
     * @throws IOException
     */
    public static void copyDirectory(File sourceLocation, File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists() && !targetLocation.mkdirs()) {
                throw new IOException("Cannot create dir " + targetLocation.getAbsolutePath());
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {
            File directory = targetLocation.getParentFile();
            if (directory != null && !directory.exists() && !directory.mkdirs()) {
                throw new IOException("Cannot create dir " + directory.getAbsolutePath());
            }
            copyFile(sourceLocation, targetLocation);
        }
    }

    public static File getCacheDir(Context context, String dirName) {
        File cacheDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
            // 使用 getExternalFileDir 容易被删
            cacheDir = new File(new File(dataDir, context.getPackageName()), dirName);
        }
        if (cacheDir == null) {
            cacheDir = FileCache.getCacheDirFile(context);
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

    public static File getSplashVideoCacheDir(Context context, String dir) {
        File cacheDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
            // 使用 getExternalFileDir 容易被删
            cacheDir = new File(new File(dataDir, context.getPackageName()), dir);
        }
        if (cacheDir == null) {
            cacheDir = FileCache.getCacheDirFile(context, dir);
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

    public static File getCacheDirectory(Context context, boolean preferExternal) {
        File appCacheDir = null;
        if (preferExternal && MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = context.getExternalCacheDir();
        }
        if (appCacheDir == null || (!appCacheDir.exists() && !appCacheDir.mkdirs())) {
            appCacheDir = context.getCacheDir();
        }
        return appCacheDir;
    }

    public static File getFileDirectory(Context context, String dir, boolean preferExternal) {
        File appFileDir = null;
        if (preferExternal && MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appFileDir = context.getExternalFilesDir(dir);
        }
        if (appFileDir == null || (!appFileDir.exists() && !appFileDir.mkdirs())) {
            appFileDir = new File(context.getFilesDir(), dir);
        }
        if (!appFileDir.exists() && !appFileDir.mkdirs()) {
            appFileDir = context.getFilesDir();
        }
        return appFileDir;
    }



    /**
     * md5 32 小写
     *
     * @param text
     * @return
     */
    public static String md5(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                int number = b & 0xff;
                String hex = Integer.toHexString(number);
                if (hex.length() == 1) {
                    sb.append("0" + hex);
                } else {
                    sb.append(hex);
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }


    public static String generateMD5FileName(String imageUri) {
        byte[] md5 = getMD5(imageUri.getBytes());
        BigInteger bi = new BigInteger(md5).abs();
        return bi.toString(RADIX);
    }

    private static byte[] getMD5(byte[] data) {
        byte[] hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(data);
            hash = digest.digest();
        } catch (NoSuchAlgorithmException e) {
        }
        return hash;
    }


    public static File getBooheeDir() {
        if (hasSdcard()) {
            File booheeDir = new File(Environment.getExternalStorageDirectory(), BOOHEE_DIR);
            if (!booheeDir.exists()) {
                booheeDir.mkdir();
            }
            return booheeDir;
        }
        return null;
    }

    public static File getImagesDir() {
        File booheeDir = getBooheeDir();
        if (booheeDir != null) {
            File imagesDir = new File(booheeDir, IMAGES_DIR);
            if (!imagesDir.exists()) {
                imagesDir.mkdir();
            }
            return imagesDir;
        }
        return null;
    }


    public static File getTempImage() {
        File booheeDir = getBooheeDir();
        if (booheeDir != null) {
            File tempImage = new File(booheeDir, TEMP_IMAGE);
            if (!tempImage.exists()) {
                try {
                    tempImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return tempImage;
        }
        return null;
    }

    /**
     * 是否挂载了sdcard
     *
     * @return
     */
    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    /**
     * 获取图片类型
     *
     * @param file 文件
     * @return 图片类型
     */
    public static String getImageType(final File file) {
        if (file == null) return null;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            return getImageType(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 流获取图片类型
     *
     * @param is 图片输入流
     * @return 图片类型
     */
    public static String getImageType(final InputStream is) {
        if (is == null) return null;
        try {
            byte[] bytes = new byte[8];
            return is.read(bytes, 0, 8) != -1 ? getImageType(bytes) : null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取图片类型
     *
     * @param bytes bitmap的前8字节
     * @return 图片类型
     */
    public static String getImageType(final byte[] bytes) {
        if (isJPEG(bytes)) return "JPEG";
        if (isGIF(bytes)) return "GIF";
        if (isPNG(bytes)) return "PNG";
        if (isBMP(bytes)) return "BMP";
        if (isWEBP(bytes)) return "WEBP";
        return null;
    }

    private static boolean isWEBP(byte[] buf) {
        byte[] markBuf = "RIFF".getBytes(); //WebP图片识别符
        return compare(buf, markBuf);
    }

    /**
     * 标示一致性比较
     *
     * @param buf     待检测标示
     * @param markBuf 标识符字节数组
     * @return 返回false标示标示不匹配
     */
    private static boolean compare(byte[] buf, byte[] markBuf) {
        for (int i = 0; i < markBuf.length; i++) {
            byte b = markBuf[i];
            byte a = buf[i];

            if (a != b) {
                return false;
            }
        }
        return true;
    }

    private static boolean isJPEG(final byte[] b) {
        return b.length >= 2
                && (b[0] == (byte) 0xFF) && (b[1] == (byte) 0xD8);
    }

    private static boolean isGIF(final byte[] b) {
        return b.length >= 6
                && b[0] == 'G' && b[1] == 'I'
                && b[2] == 'F' && b[3] == '8'
                && (b[4] == '7' || b[4] == '9') && b[5] == 'a';
    }

    public static boolean isGIF(final File file) {
        return "GIF".equals(getImageType(file));
    }

    private static boolean isPNG(final byte[] b) {
        return b.length >= 8
                && (b[0] == (byte) 137 && b[1] == (byte) 80
                && b[2] == (byte) 78 && b[3] == (byte) 71
                && b[4] == (byte) 13 && b[5] == (byte) 10
                && b[6] == (byte) 26 && b[7] == (byte) 10);
    }

    private static boolean isBMP(final byte[] b) {
        return b.length >= 2
                && (b[0] == 0x42) && (b[1] == 0x4d);
    }

}

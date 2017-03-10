package com.vlife.qrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button qrCreateButton;
    private ImageView qrImageView;
    private Button recoverButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setOnClick();
    }

    private void setOnClick() {
        qrCreateButton.setOnClickListener(this);
        recoverButton.setOnClickListener(this);
    }

    private void initView() {
        qrCreateButton = (Button) findViewById(R.id.qr_create_Button);
        qrImageView = (ImageView) findViewById(R.id.qr_imageView);
        recoverButton = (Button) findViewById(R.id.recover_button);
    }

    /**
     * 生成一个二维码
     * @param content 二维码内容
     * @param width 二维码的宽度
     * @param height 二维码的高度
     * @return
     */
    public static Bitmap generateQRCode(String content, int width, int height) {
        try {
            HashMap<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            // 设置编码方式utf-8
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //设置二维码的纠错级别为h
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix matrix = new MultiFormatWriter().encode(content,
                    BarcodeFormat.QR_CODE, width, height, hints);
            return bitMatrix2Bitmap(matrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == 1) {
            Log.d("TAG", "成功");
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public Bitmap bitmap;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qr_create_Button:
                bitmap = createQr();
                break;
            case R.id.recover_button:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qrfffff1).copy(Bitmap.Config.ARGB_8888, true);
                int height = bitmap.getHeight();
                int width = bitmap.getWidth();
                Log.d("bitmap", "height=" + height + ",width=" + width);
                for (int i = 0; i < height - 1; i++) {
                    for (int j = 0; j < width - 1; j++) {
                        int color = bitmap.getPixel(j, i);
                        int alpha = Color.alpha(color);
                        int red = Color.red(color);
                        int green = Color.green(color);
                        int blue = Color.blue(color);
                        Log.d("color", "color=" + color + ",对比=" + Color.argb(alpha, red, green, blue) + ",alpha=" + alpha + ",red=" + red + ",green=" + green + ",blue=" + blue);
                        Log.d("test", Color.argb(alpha, red, green, blue) + "");
                        boolean isAlpha = (alpha == 255);
//                        boolean isRed = red >200 && red <= 253;
//                        boolean isGreen = green >= 200 && green <= 253;
//                        boolean isBlue = blue >= 200 && blue <= 253;

                        boolean isRed = red >200;
                        boolean isGreen = green >= 200 ;
                        boolean isBlue = blue >= 200 && blue <244;
//                        if (isAlpha && isRed && isGreen && isBlue) {
                            Log.d("boolean", "true");
                            bitmap.setPixel(j, i, Color.argb(255, red,green,(blue-239)<0?0:(blue-239)*16));
                            Log.d("test2", Color.argb(alpha, red, green, blue) + "");
//                        } else {
//                            Log.d("TAG", "TAG");
//                        }
                    }
                }
                qrImageView.setImageBitmap(bitmap);
                break;
        }


//        int[] pix = new int[width * height];
//
//        for (int y = 0; y < height; y++)
//            for (int x = 0; x < width; x++)
//            {
//                int index = y * width + x;
//                int r = ((pix[index] >> 16) & 0xff)|0xff;
//                int g = ((pix[index] >> 8) & 0xff)|0xff;
//                int b =( pix[index] & 0xff)|0xff;
//                pix[index] = 0xff000000 | (r << 16) | (g << 8) | b;
//                //  pix[index] = 0xff000000;
//
//            }
//        bitmap.setPixels(pix, 0, width, 0, 0, width, height);

    }


    /**
     * 生成二维码
     */
    public Bitmap createQr() {
        Bitmap bitmap = generateQRCode("hello,world", 100, 100);
        qrImageView.setImageBitmap(bitmap);
        return bitmap;
//        qrImageView.setImageAlpha(1);
//        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
//        startActivityForResult(intent, 1);
    }

    private static Bitmap bitMatrix2Bitmap(BitMatrix matrix) {
        matrix = updateBit(matrix, 0);
//        int w = matrix.getWidth();
//        int h = matrix.getHeight();
//        int[] rawData = new int[w * h];
//        for (int i = 0; i < w; i++) {
//            for (int j = 0; j < h; j++) {
//                int color = Color.TRANSPARENT;
//                if (matrix.get(i, j)) {
//                    // 有内容的部分，颜色设置为黑色，当然这里可以自己修改成喜欢的颜色
//                    color = Color.BLACK;
//                }
//                rawData[i + (j * w)] = color;
//            }
//        }
//
//        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
//        bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
//        return bitmap;
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        // 按照二维码的算法，逐个生成二维码的图片，两个for循环是图片横列扫描的结果
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y))//范围内为黑色的
                    pixels[y * width + x] = 0xfffffff1; //此处是二维码内容的颜色
                else//其他的地方为白色
                    pixels[y * width + x] = Color.TRANSPARENT;
            }
        }
        // 生成二维码图片的格式，使用ARGB_8888
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        //设置像素矩阵的范围
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;

    }

    private static BitMatrix updateBit(BitMatrix matrix, int margin) {
        int tempM = margin * 2;
        int[] rec = matrix.getEnclosingRectangle(); // 获取二维码图案的属性
        int resWidth = rec[2] + tempM;
        int resHeight = rec[3] + tempM;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // 按照自定义边框生成新的BitMatrix
        resMatrix.clear();
        for (int i = margin; i < resWidth - margin; i++) { // 循环，将二维码图案绘制到新的bitMatrix中
            for (int j = margin; j < resHeight - margin; j++) {
                if (matrix.get(i - margin + rec[0], j - margin + rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }
        return resMatrix;
    }

}

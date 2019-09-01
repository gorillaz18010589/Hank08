package tw.brad.apps.brad09;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class MyView extends View {
    private Resources res;
    private float viewW, viewH;
    private boolean isInit;
    private Bitmap ballBmp;
    private float ballW, ballH, ballX, ballY, dx, dy;
    private Matrix matrix;
    private Timer timer;
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);  //我view是活在某個裡面,我就能拿到res

        res =  context.getResources();//取得res,
        matrix = new Matrix(); //選轉,縮放,變型的物件實體
        timer = new Timer(); //背景執行緒

    }

    //初始化設定
    private  void init(){//做一次就好的地方
        viewW = getWidth(); viewH = getHeight(); //第一次近來就取得寬高
        ballW = viewW / 12f; ballH = ballW; //球的大小式螢幕的8分之一

        ballBmp= BitmapFactory.decodeResource(res,R.drawable.balle);//球在不同資料夾,要靠工廠去解碼得到(1.res,顯示頁面資源區)
        matrix.reset();

        matrix.postScale(ballH/ballBmp.getWidth(), ballH/ballBmp.getWidth());// 新突/圓圖就拿到了

        ballBmp =  Bitmap.createBitmap(ballBmp,0,0,
                ballBmp.getWidth(),ballBmp.getHeight(),matrix,false);

        ballX = ballY = 10;
        dx = dy = 8;

        //抓到背景圖
        setBackgroundResource(R.drawable.bg);

        timer.schedule(new refreshView(),0,17);
        timer.schedule(new BallTask(),1*100,30);


        isInit = true; //因為跑完後是true,所以寬高抓一次

    }

    @Override//當開始畫的那刻
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!isInit)init(); //如果還未初始畫,去上面做出式化動作

        canvas.drawBitmap(ballBmp,ballX,ballY,null);//影像輸出物件(球的檔案書出位置,)
    }

    //移動球的速度
    private  class  BallTask extends  TimerTask{
        @Override
        public void run() {

            if (ballX < 0 || ballX + ballW > viewW){
                dx *= -1;
            }
            if (ballY < 0 || ballY + ballH > viewH){
                dy *= -1;
            }


            ballX += dx;
            ballY += dy;
        }
    }

    //球動的頻率
    private  class refreshView extends TimerTask{
        @Override
        public void run() {
            postInvalidate();//後期更新畫面
        }
    }
}

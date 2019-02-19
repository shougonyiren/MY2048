package my2048.imooc.com.a2048_19217;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static my2048.imooc.com.a2048_19217.R.drawable.shape_corner;

public class MainActivity extends AppCompatActivity implements  GestureDetector.OnGestureListener
{

    int Score;
    int BestScore;
    TextView textViewScore;
    TextView textViewBestScore;
    LinearLayout linearLayoutforFailure;//结束界面 失败界面
    LinearLayout linearLayout;//重来
    LinearLayout linearLayoutforback;//退出
    TextView textView[][]=new TextView[4][4];
    GestureDetector detector;
    Button startButton;
    SoundPool sp; // 声明SoundPool的引用
    HashMap<Integer, Integer> hm; // 声明一个HashMap来存放声音文件
    int currStreamId;// 当前正播放的streamId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Initialization();
        initSoundPool(); // 初始化声音池的方法
        ReadPersistentData();//读取持久化数据
        detector = new GestureDetector(this,this);
    }
    //读取持久化数据
    private void ReadPersistentData() {
        SharedPreferences sp = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        Score = sp.getInt("Score",MODE_PRIVATE);
        BestScore = sp.getInt("BestScore",MODE_PRIVATE);
        textViewScore.setText("Score\n"+Score);
        textViewBestScore.setText("Best\n"+BestScore);
        String[] matrix=getSharedPreference(this,"matrix");
        if(matrix==null){
            for(int x=0;x<4;x++)
                for (int y = 0; y < 4; y++) textView[x][y].setText(String.valueOf(""));
            GenerateRandom();//生成随机2或4
            GenerateRandom();//生成随机2或4
        }else {
            String[] matrix1=new String[16];//
            if(matrix.length<16){
                for (int i = 0; i < matrix1.length; i++) {
                    matrix1[i]="";
                }
                for (int i = 0; i < matrix.length; i++) {
                    matrix1[i]=matrix[i];
                }
            }
            for(int x=0;x<4;x++){
                for (int y = 0; y < 4; y++) {
                    textView[x][y].setText(matrix1[4*x+y]);
                    RefreshColor(x,y);
                }
            }
        }

    }
    //控件初始化
    private void Initialization() {
        linearLayoutforFailure=findViewById(R.id.FailureLiner);
        linearLayoutforFailure.setVisibility(View.GONE);
        textViewBestScore=findViewById(R.id.textViewBestScore);
        linearLayoutforback=findViewById(R.id.BackLiner);
        linearLayoutforback.setVisibility(View.GONE);
        linearLayout=findViewById(R.id.RestartLiner);
        linearLayout.setVisibility(View.GONE);
        textViewScore=findViewById(R.id.textViewScore);
        startButton=findViewById(R.id.restartbutton);
        textView[0][0]=findViewById(R.id.t00);
        textView[0][1]=findViewById(R.id.t01);
        textView[0][2]=findViewById(R.id.t02);
        textView[0][3]=findViewById(R.id.t03);
        textView[1][0]=findViewById(R.id.t10);
        textView[1][1]=findViewById(R.id.t11);
        textView[1][2]=findViewById(R.id.t12);
        textView[1][3]=findViewById(R.id.t13);
        textView[2][0]=findViewById(R.id.t20);
        textView[2][1]=findViewById(R.id.t21);
        textView[2][2]=findViewById(R.id.t22);
        textView[2][3]=findViewById(R.id.t23);
        textView[3][0]=findViewById(R.id.t30);
        textView[3][1]=findViewById(R.id.t31);
        textView[3][2]=findViewById(R.id.t32);
        textView[3][3]=findViewById(R.id.t33);
    }
    // 初始化声音池的方法
    public void initSoundPool() {
        sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 0); // 创建SoundPool对象
        hm = new HashMap<Integer, Integer>(); // 创建HashMap对象
        hm.put(1, sp.load(this,R.raw.slidinglock, 1)); // 加载声音文件musictest并且设置为1号声音放入hm中
    }

    // 播放声音的方法
    public void playSound(int sound, int loop) { // 获取AudioManager引用
        AudioManager am = (AudioManager) this
                .getSystemService(Context.AUDIO_SERVICE);
        // 获取当前音量
        float streamVolumeCurrent = am
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        // 获取系统最大音量
        float streamVolumeMax = am
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 计算得到播放音量
        float volume = streamVolumeCurrent / streamVolumeMax;
        // 调用SoundPool的play方法来播放声音文件
        currStreamId = sp.play(hm.get(sound), volume, volume, 1, loop, 1.0f);
    }
    //生成随机2或4
    private void GenerateRandom(){
        Random ran1 = new Random();
        int vaule;
        int z=ran1.nextInt(6);
        if (z==5){
            vaule=4;
        }else {
            vaule=2;
        }
        ArrayList <Integer>TisNull= new ArrayList<Integer>();
        for(int x=0;x<4;x++){
            for (int y = 0; y < 4; y++){
                if(textView[x][y].getText().equals("")){
                    Integer b=new Integer(4*x+y);
                    TisNull.add(b);
                }
            }
        }
        if(TisNull.isEmpty()){
            DecisionFailure();
            return;
        }
        Random ran2 = new Random();
        int  z1;
        int  x;
        int  y;
        if(TisNull.size()==1){
            z1=0;
        }else{
            z1 =ran2.nextInt(TisNull.size());
        }
        x=TisNull.get(z1)/4;
        y=TisNull.get(z1)%4;
        Animation animation;
        textView[x][y].setText(String.valueOf(vaule));
        animation= AnimationUtils.loadAnimation(this,R.anim.scale);
        textView[x][y].startAnimation(animation);
        RefreshColor(x,y);
    }

    private void DecisionFailure() {
        //boolean
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if(x-1>=0){
                    if(textView[x][y].getText().equals(textView[x-1][y].getText())) {
                        return;
                    }
                }
                if(x+1<=3){
                    if(textView[x][y].getText().equals(textView[x+1][y].getText())) {
                        return;
                    }
                }
                if(y-1>=0){
                    if(textView[x][y].getText().equals(textView[x][y-1].getText())) {
                        return;
                    }
                }
                if(y+1<=3){
                    if(textView[x][y].getText().equals(textView[x][y+1].getText())) {
                        return;
                    }
                }
            }
        }
        linearLayoutforFailure.setVisibility(View.VISIBLE);
    }

    private void RefreshColor(int x,int y) {
        Resources resources = getBaseContext().getResources();
        Drawable drawable = resources.getDrawable(shape_corner);
        if (textView[x][y].getText().equals("")) {
            drawable = resources.getDrawable(shape_corner);
            textView[x][y].setBackgroundDrawable(drawable);
            return;
        }
        Integer op=Integer.valueOf(textView[x][y].getText().toString());
        switch (op)
        {
            case 1024:{
                drawable = resources.getDrawable(R.drawable.shape_conner_1024);
                break;
            }
            case 128:{
                drawable = resources.getDrawable(R.drawable.shape_conner_128);
                break;
            }
            case 131072:{
                drawable = resources.getDrawable(R.drawable.shape_conner_131072);
                break;
            }
            case 16:{
                drawable = resources.getDrawable(R.drawable.shape_conner_16);
                break;
            }
            case 16384:{
                drawable = resources.getDrawable(R.drawable.shape_conner_16384);
                break;
            }
            case 2:{
                drawable = resources.getDrawable(R.drawable.shape_conner_2);
                break;
            }
            case 2048:{
                drawable = resources.getDrawable(R.drawable.shape_conner_2048);
                break;
            }
            case 256:{
                drawable = resources.getDrawable(R.drawable.shape_conner_256);
                break;
            }
            case 262144:{
                drawable = resources.getDrawable(R.drawable.shape_conner_262144);
                break;
            }
            case 32:{
                drawable = resources.getDrawable(R.drawable.shape_conner_32);
                break;
            }
            case 32768:{
                drawable = resources.getDrawable(R.drawable.shape_conner_32768);
                break;
            }
            case 4:{
                drawable = resources.getDrawable(R.drawable.shape_conner_4);
                break;
            }
            case 4096:{
                drawable = resources.getDrawable(R.drawable.shape_conner_4096);
                break;
            }
            case 512:{
                drawable = resources.getDrawable(R.drawable.shape_conner_512);
                break;
            }
            case 64:{
                drawable = resources.getDrawable(R.drawable.shape_conner_64);
                break;
            }
            case 65536:{
                drawable = resources.getDrawable(R.drawable.shape_conner_65536);
                break;
            }
            case 8192:{
                drawable = resources.getDrawable(R.drawable.shape_conner_8192);
                break;
            }
            case 8:{
                drawable = resources.getDrawable(R.drawable.shape_corner_for_8);
                break;
            }
            default:{
                drawable = resources.getDrawable(shape_corner);
                break;
            }
        }
        textView[x][y].setBackgroundDrawable(drawable);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float minMove = 200;         //最小滑动距离
        float minVelocity = 10;      //最小滑动速度
        float beginX = e1.getX();
        float endX = e2.getX();
        float beginY = e1.getY();
        float endY = e2.getY();
        if(beginX-endX>minMove&&Math.abs(velocityX)>minVelocity){   //左滑
            SlidingOperation(0,-1);
            System.out.println("左滑");
            // Toast.makeText(this,velocityX+"左滑",Toast.LENGTH_SHORT).show();
        }else if(endX-beginX>minMove&&Math.abs(velocityX)>minVelocity){   //右滑
            SlidingOperationReverse(0,1);
            System.out.println("右滑");
            // Toast.makeText(this,velocityX+"右滑",Toast.LENGTH_SHORT).show();
        }else if(beginY-endY>minMove&&Math.abs(velocityY)>minVelocity){   //上滑
            SlidingOperation(-1,0);
            System.out.println("上滑");
            // Toast.makeText(this,velocityX+"上滑",Toast.LENGTH_SHORT).show();
        }else if(endY-beginY>minMove&&Math.abs(velocityY)>minVelocity){   //下滑
            SlidingOperationReverse(1,0);
            System.out.println("下滑");
            //  Toast.makeText(this,velocityX+"下滑",Toast.LENGTH_SHORT).show();
        }
        GenerateRandom();
        return false;
    }
    private void SlidingOperation(int X1,int Y1) {
        boolean  dothis=false;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if(textView[x][y].getText().equals("")){
                    continue;
                }
                Integer integer=Integer.valueOf(textView[x][y].getText().toString());
                try{
                    int xtemp=x;
                    int ytemp=y;
                    while(textView[xtemp+X1][ytemp+Y1].getText().equals("")){
                        dothis=true;
                        textView[xtemp+X1][ytemp+Y1].setText(String.valueOf(textView[xtemp][ytemp].getText()));
                        textView[xtemp][ytemp].setText(String.valueOf(""));
                        RefreshColor(xtemp,ytemp);
                        xtemp=xtemp+X1;
                        ytemp=ytemp+Y1;
                        RefreshColor(xtemp,ytemp);
                        if(!(xtemp+X1>-1&&xtemp+X1<4&&ytemp+Y1>-1&&ytemp+Y1<4)){
                            System.out.println("发生了!(xtemp>-1&&xtemp<4&&ytemp>-1&&ytemp<4)");
                            break;
                        }
                    }
                    if(!(xtemp+X1>-1&&xtemp+X1<4&&ytemp+Y1>-1&&ytemp+Y1<4)){
                        System.out.println("在翻倍前发生了越界，跳过翻倍");
                        continue;
                    }
                    if(textView[xtemp+X1][ytemp+Y1].getText().equals(textView[xtemp][ytemp].getText())){
                        dothis=true;
                        Integer A=integer*2;
                        Score=Score+A;
                        textViewScore.setText("SCORE\n"+Score);
                        if(Score>BestScore){
                            BestScore=Score;
                            textViewBestScore.setText("Best\n"+BestScore);
                        }
                        textView[xtemp+X1][ytemp+Y1].setText(A.toString());
                        textView[xtemp][ytemp].setText("");
                        RefreshColor(xtemp,ytemp);
                        RefreshColor(xtemp+X1,ytemp+Y1);
                    }
                }catch (ArrayIndexOutOfBoundsException exception){
                    System.out.println("发生了ArrayIndexOutOfBoundsException");
                    continue;
                }
            }
        }
        if(dothis){
            System.out.println("playSound");
            playSound(1,0);
        }
    }
    private void SlidingOperationReverse(int X1,int Y1) {
        boolean  dothis=false;
        for (int x = 3; x >=0; x--) {
            for (int y = 3; y >=0; y--) {
                if(textView[x][y].getText().equals("")){
                    continue;
                }
                Integer integer=Integer.valueOf(textView[x][y].getText().toString());
                try{
                    int xtemp=x;
                    int ytemp=y;
                    while(textView[xtemp+X1][ytemp+Y1].getText().equals("")){
                        dothis=true;
                        textView[xtemp+X1][ytemp+Y1].setText(textView[xtemp][ytemp].getText());
                        textView[xtemp][ytemp].setText("");
                        RefreshColor(xtemp,ytemp);
                        xtemp=xtemp+X1;
                        ytemp=ytemp+Y1;
                        RefreshColor(xtemp,ytemp);
                        if(!(xtemp+X1>-1&&xtemp+X1<4&&ytemp+Y1>-1&&ytemp+Y1<4)){
                            System.out.println("发生了!(xtemp>-1&&xtemp<4&&ytemp>-1&&ytemp<4)");
                            break;
                        }
                    }
                    if(!(xtemp+X1>-1&&xtemp+X1<4&&ytemp+Y1>-1&&ytemp+Y1<4)){
                        System.out.println("在翻倍前发生了越界，跳过翻倍");
                        continue;
                    }
                    if(textView[xtemp+X1][ytemp+Y1].getText().equals(textView[xtemp][ytemp].getText())){
                        dothis=true;
                        Integer A=integer*2;
                        Score=Score+A;
                        textViewScore.setText("SCORE\n"+Score);
                        if(Score>BestScore){
                            BestScore=Score;
                            textViewBestScore.setText("Best\n"+BestScore);
                        }
                        textView[xtemp+X1][ytemp+Y1].setText(A.toString());
                        textView[xtemp][ytemp].setText("");
                        RefreshColor(xtemp,ytemp);
                        RefreshColor(xtemp+X1,ytemp+Y1);
                    }
                }catch (ArrayIndexOutOfBoundsException exception){
                    System.out.println("发生了反转的ArrayIndexOutOfBoundsException");
                    continue;
                }
            }
        }
        if(dothis){
            System.out.println("playSound");
            playSound(1,0);
        }
    }
    public String[] getSharedPreference(Context context,String key) {
        String regularEx = "#";
        String[] str =null;
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String values;
        values = sp.getString(key, "");
        str = values.split(regularEx);
        return str;
    }

    public void setSharedPreference(Context context,String key, String[] values) {
        String regularEx = "#";
        String str = "";
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        if (values != null && values.length > 0) {
            for (String value : values) {
                str += value;
                str += regularEx;
            }
            SharedPreferences.Editor et = sp.edit();
            et.putString(key, str);
            et.commit();
        }
    }
    @Override
    protected void onDestroy() {
       String[] resArray = new String[16];
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                resArray [4*x+y] = textView[x][y].getText().toString();
            }
        }
        setSharedPreference(this,"matrix", resArray);
        SharedPreferences sp = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putInt("Score",Score);
        et.putInt("BestScore",BestScore);
        et.commit();
        super.onDestroy();
    }

    //将该activity上的触碰事件交给GestureDetector处理
    public boolean onTouchEvent(MotionEvent me){
        return detector.onTouchEvent(me);
    }

    public void Restart(View view) {
        linearLayout.setVisibility(View.VISIBLE);
    }
    public void RestartTureonClick(View view) {
        for(int x=0;x<4;x++){
            for (int y = 0; y < 4; y++){
                textView[x][y].setText(String.valueOf(""));
                textView[x][y].setBackgroundResource(R.drawable.shape_corner);
            }
        }
        Score=0;
        textViewScore.setText("SCORE\n"+Score);
        GenerateRandom();//生成随机2或4
        GenerateRandom();//生成随机2或4
        linearLayoutforFailure.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
    }
    public void BackTureonClick(View view) {//确定退出
        super.onBackPressed();
    }
    @Override public void onBackPressed() {
        if(linearLayoutforback.getVisibility()==View.GONE)
        {
            linearLayoutforback.setVisibility(View.VISIBLE);
        }else {
            linearLayoutforback.setVisibility(View.GONE);
        }
        if(linearLayout.getVisibility()==View.VISIBLE)
        {
            linearLayout.setVisibility(View.GONE);
        }
        // super.onBackPressed();//注释掉这行,back键不退出activity
       }

    public void RestartCancelonClick(View view) {
        linearLayout.setVisibility(View.GONE);
    }

    public void BackCancelonClick(View view) {
        linearLayoutforback.setVisibility(View.GONE);
    }
}

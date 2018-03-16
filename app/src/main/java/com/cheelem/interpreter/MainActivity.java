package com.cheelem.interpreter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.asr.SpeechConstant;
import com.cheelem.interpreter.network.ConnectAction;
import com.cheelem.interpreter.network.JsonFactory;
import com.cheelem.interpreter.network.MySocketClient;
import com.cheelem.interpreter.sentence.SentenceManager;
import com.cheelem.interpreter.util.GetRandomString;
import com.translator.http.HttpPostParams;
import com.translator.trans.LANG;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PERMISSION_REQUEST_CODE = 10000;
    private static final int SHOW_RESULT = 1;
    //语音识别变量控制
    static String configuration = "{" +
            "\"accept-audio-data\":false," +
            "\"disable-punctuation\":false," +
            "\"accept-audio-volume\":true," +
            "\"pid\":1537," +
            "\"vad-endpoint-timeout\":0" +
            "}";
    EventManager recogManager;
    EventListener voiceListener;
    EditText result;
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == SHOW_RESULT) {
                String text = result.getText().toString();
                String translate = msg.obj.toString();

                result.append(translate + "\n");
                //result.setText(text + "\n" + translate);
                sentencemanager.addSentence(text, translate, client);
            }
        }
    };
    ProgressBar volumeBar;
    boolean isListening = false;
    String[] permissionNeeded = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };
    private static final SentenceManager sentencemanager = SentenceManager.getInstance();
    private MySocketClient client = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String id = UUID.randomUUID().toString();

        if (id == null || id.length() == 0) {
            id = GetRandomString.getRandomString(10);
            Log.i(this.getClass().getSimpleName(), "无法获取标识符");
        }

       // while (client == null) {
            client = ConnectAction.getClient(id);
        //}

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interpreter_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchStatus(view);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        result = findViewById(R.id.resultText);
        volumeBar = findViewById(R.id.volumeBar);

        result.setText("");
        result.setMovementMethod(ScrollingMovementMethod.getInstance());
        VoiceRecognizer.initialize(this);// 初始化识别事件管理器
        voiceListener = new EventListener() {
            @Override
            public void onEvent(String name, String params, byte[] data, int offset, int length) {
                if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_READY)) {
                    // 引擎就绪，可以说话，一般在收到此事件后通过UI通知用户可以说话了
                    Toast.makeText(MainActivity.this, "请讲话 ", Toast.LENGTH_LONG).show();
//                    client.send(JsonFactory.getInitJson(client.getId(), "ZH", "EN", null, client.getId()));
                }
                if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
                    //部分结果返回
                    try {
                        //Toast.makeText(MainActivity.this, params, Toast.LENGTH_LONG).show();
                        final JSONObject resultJSON = new JSONObject(params);
                      //  result.setText(resultJSON.optJSONArray("results_recognition").optString(0));
                        if (resultJSON.optString("result_type").equals("final_result")) {

                            result.append(resultJSON.optJSONArray("results_recognition").optString(0) + "\n");

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Looper.prepare();
                                    String translateResult = null;
                                    try {
                                        translateResult = VoiceRecognizer.getTranslator().get("jinshan").trans(LANG.ZH, LANG.EN, resultJSON.optJSONArray("results_recognition").optString(0));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    Message msg = new Message();
                                    msg.what = SHOW_RESULT;
                                    msg.obj = translateResult;

                                    handler.sendMessage(msg);
                                    System.out.println("[TRANS RES]:" + translateResult);
                                    /*Toast.makeText(MainActivity.this, "Trans Result: " + translateResult, Toast.LENGTH_LONG).show();
                                    result.setText(result.getText() + "\n" + translateResult);
                                    Looper.loop();*/
                                }
                            }).start();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_VOLUME)) {
                    try {
                        volumeBar.setProgress((new JSONObject(params)).optInt("volume-percent"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_FINISH)) {
                    // 识别结束
                    Toast.makeText(MainActivity.this, "监听已结束 ", Toast.LENGTH_LONG).show();
                    isListening = false;

                    volumeBar.setProgress(0);
                  //  client.send(JsonFactory.getEndJson(client.getSessionid(), client.getId()));
                }
            }
        };
        VoiceRecognizer.registerListener(voiceListener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.interpreter_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_backgroud) {
            // 处理菜单点击事件
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("[TEST REQUEST]:\n" + (new HttpPostParams().send2String("http://baidu.com")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void switchStatus(View view) {
        if (isListening) {
            VoiceRecognizer.getRecognizer().send(SpeechConstant.ASR_STOP, null, null, 0, 0); // 发送停止录音事件，提前结束录音等待识别结果
            Snackbar.make(view, "监听结束", Snackbar.LENGTH_LONG)
                    .setAction("OK", null).show();
            isListening = false;
        } else {
            //录音权限检验
            boolean isAllGranted = checkPermissionAllGranted(permissionNeeded);
            // 如果这3个权限全都拥有, 则直接执行备份代码
            if (isAllGranted) {
                VoiceRecognizer.getRecognizer().send(SpeechConstant.ASR_START, configuration, null, 0, 0);
                Snackbar.make(view, "请讲话", Snackbar.LENGTH_LONG)
                        .setAction("OK", null).show();
                isListening = true;
                return;
            }
            //若没有权限则请求权限
            // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
            ActivityCompat.requestPermissions(
                    this, permissionNeeded, PERMISSION_REQUEST_CODE
            );
        }
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }


}

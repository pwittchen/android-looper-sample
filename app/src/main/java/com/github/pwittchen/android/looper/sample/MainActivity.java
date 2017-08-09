package com.github.pwittchen.android.looper.sample;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * This is an exemplary application
 * written in order to play with Looper and Handler.
 * It's written for experimental purposes and may contain bugs!
 */
public class MainActivity extends AppCompatActivity implements HandlerCallback {

  private static final String BUNDLE_KEY = "handlerMsgBundle";
  private static int counter = 0; // we want to have single instance of counter
  private static int messageCounter = 0; // we want to have single instance of messageCounter
  private static String log; // we want to have single instance of log
  private TextView tvLog;
  private LooperThread looperThread;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    tvLog = (TextView) findViewById(R.id.tv_log);

    findViewById(R.id.b_post_runnable).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        postRunnable(++counter);
      }
    });

    findViewById(R.id.b_send_message).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        String value = String.format("handled message #%d", ++messageCounter);
        Message message = createMessage(value);
        looperThread.send(message);
      }
    });

    findViewById(R.id.b_clear_log).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        tvLog.setText("");
      }
    });

    looperThread = new LooperThread(this);
    looperThread.start();
  }

  @Override protected void onPause() {
    super.onPause();
    log = tvLog.getText().toString(); // onSavedInstanceState(...) is not called for some reason
  }

  @Override protected void onResume() {
    super.onResume();
    tvLog.setText(log);
  }

  @NonNull private Message createMessage(String value) {
    Message message = new Message();
    Bundle bundle = new Bundle();
    bundle.putString(BUNDLE_KEY, value);
    message.setData(bundle);
    return message;
  }

  private void postRunnable(final int id) {
    looperThread.post(new Runnable() {
      @Override public void run() {
        tryToSleepForFiveSecondsInNewThread(id);
      }
    });
  }

  private void tryToSleepForFiveSecondsInNewThread(final int id) {
    new Thread(new Runnable() {
      @Override public void run() {
        try {
          logMessageInUiThread("starting", id);
          Thread.sleep(5000);
          logMessageInUiThread("finished", id);
        } catch (InterruptedException e) {
          logMessageInUiThread("error", id);
        }
      }
    }).start();
  }

  private void logMessageInUiThread(String action, int id) {
    final String message = String.format("%s #%d\n", action, id);
    Log.d(getClass().getSimpleName(), message);

    // Known issue: code below doesn't work correctly on recreating
    // an Activity (screen rotation/resuming activity, etc.).
    // If call was started in vertical orientation,
    // we won't receive "finish" message in horizontal orientation.

    // suggestion: maybe it could be solved with event bus
    // or by persisting state in some storage (e.g. database, sharedprefs, etc.)

    runOnUiThread(new Runnable() {
      @Override public void run() {
        tvLog.append(message);
      }
    });
  }

  @Override public void handleMessage(final Bundle bundle) {
    runOnUiThread(new Runnable() {
      @Override public void run() {
        tvLog.append(String.format("%s\n", bundle.get(BUNDLE_KEY)));
      }
    });
  }
}

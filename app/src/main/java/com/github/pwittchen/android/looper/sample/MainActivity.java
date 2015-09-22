package com.github.pwittchen.android.looper.sample;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

  private LooperThread looperThread;
  private static int counter = 0; // we want to have single instance of counter

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.b_post_runnables).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        postRunnable(++counter);
      }
    });

    findViewById(R.id.b_send_message).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Message message = createMessage("hello handler!");
        looperThread.send(message);
      }
    });

    looperThread = new LooperThread();
    looperThread.start();
  }

  @NonNull private Message createMessage(String value) {
    Message message = new Message();
    Bundle bundle = new Bundle();
    bundle.putString("myMessageKey", value);
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
    Thread thread = new Thread(new Runnable() {
      @Override public void run() {
        try {
          Log.d(getClass().getSimpleName(), String.format("Starting #%s", id));
          Thread.sleep(5000);
          Log.d(getClass().getSimpleName(), String.format("Finishing #%s", id));
        } catch (InterruptedException e) {
          Log.d(getClass().getSimpleName(), String.format("Error while executing #%s", id));
          Log.e(getClass().getSimpleName(), e.getMessage());
        }
      }
    });

    // we shouldn't block UI (main) thread
    // that's why long running operation
    // is executed in a new thread

    thread.start();
  }
}

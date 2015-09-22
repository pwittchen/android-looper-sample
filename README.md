android-looper-sample
=====================

Exemplary Android app showing usage of Handler and Looper

In Android `Looper` and `Handler` can be used for creating simple queue of tasks. We have to remember that all tasks are executed in a single `LooperThread`, so if operation scheduled in queue will take a lot of time, it should be executed in a new thread. Moreover, instance of `Handler` should be static to avoid memory leaks. We can also post `Message` object to `LooperThread` and handle it via `Hanlder` in `handleMessage(Message msg)` method. `Message` could contain small pieces of data like primitive types or Strings in a `Bundle` object.

**What is Looper?**

Looper is a class which is used to execute the messages (`Runnables`) in a queue. Normal threads have no such queue, e.g. simple thread does not have any queue. It executes once and after method execution finishes, the thread will not run another Message(Runnable).

**Where can we use Looper class?**

If someone wants to execute multiple messages (`Runnables`) then he should use the `Looper` class which is responsible for creating a queue in the thread. For example, while writing an application that downloads files from the internet, we can use `Looper` class to put files to be downloaded in the queue.

**How does Looper work?**

There is `prepare()` method to prepare the `Looper`. Then you can use `loop()` method to create a message loop in the current thread and now your `Looper` is ready to execute the requests in the queue until you quit the loop.

**Please note**: usually `prepare()` and `loop()` are already called in the Activity, so we should perform the following check:

```java
if (Looper.myLooper() == null) {
  Looper.prepare();
}
```

**Quiting the Looper**

[Quoting](https://groups.google.com/forum/#!topic/android-platform/_jsUqXi6DKM) Google engineer, Christopher Tate - you can just leave the looper there until your app is destroyed, and it will go down with it. You don't need to worry about it.

*"Speaking very generally, never quit() your looper threads. That method exists mostly for historical and testing reasons. In Real Life™, I recommend that you continue to reuse the same looper thread(s) for the life of the process rather than creating/quitting them."*

References
----------
- [Looper in Android documentation](http://developer.android.com/reference/android/os/Looper.html)
- [Handler in Android documentation](http://developer.android.com/reference/android/os/Handler.html)
- [What is the purpose of Looper and how to use it? (via StackOverflow)](http://stackoverflow.com/questions/7597742/what-is-the-purpose-of-looper-and-how-to-use-it)
- [Where to quit the Looper? (via StackOverflow)](http://stackoverflow.com/questions/17617731/where-quit-the-looper)

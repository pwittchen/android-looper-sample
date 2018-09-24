android-looper-sample
=====================

Exemplary Android app showing usage of Handler and Looper

**Please note**: This project is an experiment and is not production ready. If you know, how to improve it, feel free to create an issue or pull request.

Contents
--------

- [Overview](#overview)
- [What is Looper?](#what-is-looper)
- [Where can we use Looper class?](#where-can-we-use-looper-class)
- [How does Looper work?](#how-does-looper-work)
- [Quiting the Looper](#quiting-the-looper)
- [References](#references)


Overview
--------

In Android `Looper` and `Handler` can be used for creating simple queue of tasks. We have to remember that all tasks are executed in a single `LooperThread`, so if operation scheduled in queue will take a lot of time, it should be executed in a new thread. Moreover, instance of `Handler` should be static to avoid memory leaks. We can also post `Message` object to `LooperThread` and handle it via `Hanlder` in `handleMessage(Message msg)` method. `Message` could contain small pieces of data like primitive types or Strings in a `Bundle` object.

What is Looper?
---------------

Looper is a class which is used to execute the messages (`Runnables`) in a queue. Normal threads have no such queue, e.g. simple thread does not have any queue. It executes once and after method execution finishes, the thread will not run another Message(Runnable).

What is Handler?
----------------

A Handler allows you to send and process Message and Runnable objects associated with a thread's MessageQueue. Each Handler instance is associated with a single thread and that thread's message queue. When you create a new Handler, it is bound to the thread / message queue of the thread that is creating it - from that point on, it will deliver messages and runnables to that message queue and execute them as they come out of the message queue.

There are two main uses for a Handler: (1) to schedule messages and runnables to be executed at some point in the future; and (2) to enqueue an action to be performed on a different thread than your own. 

Where can we use Looper class?
------------------------------

If someone wants to execute multiple messages (`Runnables`) then he should use the `Looper` class which is responsible for creating a queue in the thread. For example, while writing an application that downloads files from the internet, we can use `Looper` class to put files to be downloaded in the queue.

How does Looper work?
---------------------

There is `prepare()` method to prepare the `Looper`. Then you can use `loop()` method to create a message loop in the current thread and now your `Looper` is ready to execute the requests in the queue until you quit the loop.

**Please note**: usually `prepare()` and `loop()` are already called in the Activity, so we should perform the following check:

```java
if (Looper.myLooper() == null) {
  Looper.prepare();
}
```

Quiting the Looper
------------------

[Quoting](https://groups.google.com/forum/#!topic/android-platform/_jsUqXi6DKM) Google engineer, Christopher Tate - you can just leave the looper there until your app is destroyed, and it will go down with it. You don't need to worry about it.

*"Speaking very generally, never quit() your looper threads. That method exists mostly for historical and testing reasons. In Real Life™, I recommend that you continue to reuse the same looper thread(s) for the life of the process rather than creating/quitting them."*

References
----------
- [Looper in Android documentation](http://developer.android.com/reference/android/os/Looper.html)
- [Handler in Android documentation](http://developer.android.com/reference/android/os/Handler.html)
- [Communicate with the UI thread in Android documentation](https://developer.android.com/training/multiple-threads/communicate-ui)
- [What is the purpose of Looper and how to use it? (via StackOverflow)](http://stackoverflow.com/questions/7597742/what-is-the-purpose-of-looper-and-how-to-use-it)
- [Where to quit the Looper? (via StackOverflow)](http://stackoverflow.com/questions/17617731/where-quit-the-looper)
- [Understanding Android Core: Looper, Handler, and HandlerThread](https://blog.mindorks.com/android-core-looper-handler-and-handlerthread-bd54d69fe91a)
- [Post office simulator looper sample](https://github.com/MindorksOpenSource/post-office-simulator-looper-example)
- [Processes and threads overview](https://developer.android.com/guide/components/processes-and-threads)

# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/brzhang/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep public class * extends android.app.Fragment
-keep class android.databinding.BindingAdapter
# packer-ng 用到了反射
-keep class com.mcxiaoke.packer.** {*;}

-keep class com.qq.taf.**{*;}
-dontwarn com.qq.taf.**
-keep class com.qq.jce.**{*;}
-dontwarn com.qq.jce.**

-keep class tv.danmaku.ijk.media.player.**{*; }
-keep class tv.danmaku.ijk.media.player.IjkMediaPlayer.**{*; }
-keep class tv.danmaku.ijk.media.player.ffmpeg.FFmpegApi.**{*; }
-keep class .ijkplayer. {*; }

-keep class wseemann.media.**{*; }

-keep class com.daimajia.easing.** { *; }
-keep interface com.daimajia.easing.** { *; }

-keepclassmembers class * extends android.webkit.WebChromeClient{
       public void openFileChooser(...);
}

# wx sdk
-keep class com.tencent.mm.sdk.** {
   *;
}

# uploadlib
-keep class com.tencent.upload.** {*;}

# opensdk
-dontwarn com.tencent.connect.**
-dontwarn com.tencent.smtt.**
-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}

# retrofit http://square.github.io/retrofit/
#-dontwarn retrofit2.**
#-keep class retrofit2.** { *; }
#-keepattributes Signature
#-keepattributes Exceptions

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

# okio
-dontwarn okio.**

# gson
# https://github.com/google/gson/blob/master/examples/android-proguard-example/proguard.cfg
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

-keepclassmembers class ** {
    public void on*Event(...);
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-dontwarn  org.eclipse.jdt.annotation.**

# Gson specific classes
-keep class sun.misc.Unsafe { *; }

# RxJava
-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# mta
#-keep class com.tencent.stat.**  {* ;}
#-keep class com.tencent.mid.**  {* ;}
#-keep class org.apache.thrift.**  {* ;}
-keep class com.tencent.** { *; }

-dontwarn com.tencent.mta.track.**

# pinyin4j
-dontwarn demo.**

-keep class com.hp.hpl.sparta.** { *; }
-keep class net.sourceforge.pinyin4j.** { *; }
-keep class pinyindb.** { *; }
-keep class demo.** { *; }

# GlideModule
-keep public class * implements com.bumptech.glide.module.GlideModule

# TencentLocationSDK
-dontwarn  org.eclipse.jdt.annotation.**

# TencentMapSDK
# TencentSearch
-keep class com.tencent.lbssearch.** {*;}

# photoview
-dontwarn uk.co.senab.photoview.**

# videocache
-dontwarn com.danikula.videocache.**

-dontwarn okio.**
-dontwarn javax.annotation.**
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }

-dontwarn com.squareup.picasso.**
-dontwarn com.bumptech.glide.**

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# GlideModule
-keep public class * implements com.bumptech.glide.module.GlideModule

# Gson specific classes
-keep class sun.misc.Unsafe { *; }

# uploadlib
-keep class com.tencent.upload.** {*;}

# RxJava
-dontwarn sun.misc.**

#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.brzhang.fllipped.model.** { *; }

-dontwarn com.roughike.bottombar.**

-dontwarn com.nimbusds.srp6.cli.**

-dontwarn c.t.m.g.**
-dontwarn cn.bingoogolapple.refreshlayout.adapters.**


# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Androidsdk\sdk/tools/proguard/proguard-android.txt
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


#---------------------------------------基本配置---------------------------------------

#代码压缩比，在0-7之间，默认无5，一般不需要修改
-optimizationpasses 5

#混淆时不使用大小混写，混淆后的类名为小写
-dontusemixedcaseclassnames

#指定不去忽略非公共类
-dontskipnonpubliclibraryclasses

#指定不去忽略非公共的库的类成员
-dontskipnonpubliclibraryclassmembers

#不做预校验，preverify是proguard的4个步骤之一
#android不需要做preverify,去掉这一步可以加快混淆速度
-dontpreverify

#verbose这句话，混淆后就会生成映射文件
#包含类名->混淆后类名的映射关系
#使用printmapping指定映射文件
-verbose
-printmapping proguardMapping.txt

#指定混淆时使用的算法，后面的参数是一个过滤器
#这个过滤器是google推荐的算法，一般不改变
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#保护代码中Annotation不被混淆
-keepattributes *Annotation*

#避免混淆泛型
-keepattributes Signature

#抛出异常是保留代码行号
-keepattributes SourceFile,LineNumberTable

# ----------------------------------需要保留的东西----------------------------------

#保存所有的本地native方法不被混淆
#-keepclasseswithmembernames class * {
#    native <method>;
#}


# 处理support包
-dontnote android.support.**
-dontwarn android.support.**


# 保留四大组件，自定义的Application等这些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService



#枚举类型不能被混淆
-keepclassmembernames enum * {
    public static **[] values();
    public static **[] valuesOf(java.lang.String);
}


#保留Parcelable序列化的类不被混淆
-keep class * implements android.os.Parceable{
    public static final android.os.Parceable$Creator *;
}


#assume no side effects:删除android.util.Log输出的日志
-assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** d(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}



#-----------------------第三方库-------------------

#Retrofit
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-dontwarn okio.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault


#butterKnife
# Retain generated class which implement Unbinder.
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }

# Prevent obfuscation of types which use ButterKnife annotations since the simple name
# is used to reflectively look up the generated ViewBinding.
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }



#refrolamda
-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*


#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}


#base Adapter
-keep class com.chad.library.adapter.** {
   *;
}

#photoView
-keep public class com.github.chrisbanes.photoview.** {*;}

#circleImage
-keep public class de.hdodenhof.circleimageview.** {*;}

#likeButton
-keep public class com.like.** {*;}

#skinSupport
-keep public class skin.support.app.** {*;}

#rx
#-keep public class io.reactivex.** {*;}
#-keep public class com.zhanghao.gankio.rx.** {*;}


-keep public class com.zhanghao.gankio.entity.**{*;}

#budhd
#-libraryjars libs/Bughd_android_sdk_v1.3.7.jar
#-keep public class im.fir.sdk.** {*;}
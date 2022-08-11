package com.gdu.baselibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import kotlin.jvm.internal.Intrinsics;

public final class MMKVUtil {
   private static volatile MMKV instance;
   private static Lock mLock = new ReentrantLock();

   private MMKVUtil() {
   }

   private static MMKV getInstance() {
      if (instance == null) {
         mLock.lock();
         try {
            if (instance == null) {
               instance = MMKV.mmkvWithID("GduCommandPlatform");
            }
         } finally {
            mLock.unlock();
         }
      }
      return instance;
   }

   public static final void putInt(@NotNull String key, int value) {
      Intrinsics.checkNotNullParameter(key, "key");
      getInstance().encode(key, value);
   }

   public static final int getInt(@NotNull String key) {
      return getInstance().decodeInt(key);
   }

   public static final int getInt(@NotNull String key, int defaultValue) {
      return getInstance().decodeInt(key, defaultValue);
   }

   public static final void putFloat(@NotNull String key, float value) {
      Intrinsics.checkNotNullParameter(key, "key");
      getInstance().encode(key, value);
   }

   public static final float getFloat(@NotNull String key) {
      return getInstance().decodeFloat(key);
   }

   public static final float getFloat(@NotNull String key, float defaultValue) {
      return getInstance().decodeFloat(key, defaultValue);
   }

   public static final void putLong(@NotNull String key, long value) {
      Intrinsics.checkNotNullParameter(key, "key");
      getInstance().encode(key, value);
   }

   public static final long getLong(@NotNull String key) {
      return getInstance().decodeLong(key);
   }

   public static final long getLong(@NotNull String key, long defaultValue) {
      return getInstance().decodeLong(key, defaultValue);
   }

   public static final void putDouble(@NotNull String key, double value) {
      Intrinsics.checkNotNullParameter(key, "key");
      getInstance().encode(key, value);
   }

   public static final double getDouble(@NotNull String key) {
      return getInstance().decodeDouble(key);
   }

   public static final double getDouble(@NotNull String key, double defaultValue) {
      return getInstance().decodeDouble(key, defaultValue);
   }

   public static final void putString(@NotNull String key, @NotNull String value) {
      Intrinsics.checkNotNullParameter(key, "key");
      Intrinsics.checkNotNullParameter(value, "value");
      getInstance().encode(key, value);
   }

   @NotNull
   public static final String getString(@NotNull String key) {
      final String result = getInstance().decodeString(key);
      return CommonUtils.isEmptyString(result) ? "" : result;
   }

   @NotNull
   public static final String getString(@NotNull String key, @NotNull String defaultValue) {
      final String result = getInstance().decodeString(key, defaultValue);
      return CommonUtils.isEmptyString(result) ? "" : result;
   }

   public static final void putBoolean(@NotNull String key, boolean value) {
      Intrinsics.checkNotNullParameter(key, "key");
      getInstance().encode(key, value);
   }

   public static final boolean getBoolean(@NotNull String key) {
      Intrinsics.checkNotNullParameter(key, "key");
      return getInstance().decodeBool(key);
   }

   public static final boolean getBoolean(@NotNull String key, boolean defaultValue) {
      Intrinsics.checkNotNullParameter(key, "key");
      return getInstance().decodeBool(key, defaultValue);
   }

   public static final void putDrawable(@NotNull Context context, int resId, @NotNull String key) {
      Intrinsics.checkNotNullParameter(context, "context");
      Intrinsics.checkNotNullParameter(key, "key");

      try {
         Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         bitmap.compress(Bitmap.CompressFormat.PNG, 50, (OutputStream)baos);
         String imageBase64 = Base64.encodeToString(baos.toByteArray(), 0);
         Intrinsics.checkNotNullExpressionValue(imageBase64, "imageBase64");
         putString(key, imageBase64);
         baos.close();
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   @Nullable
   public static final Drawable getDrawable(@NotNull String key) {
      Intrinsics.checkNotNullParameter(key, "key");
      final String imageBase64Str = getString(key);
      if (CommonUtils.isEmptyString(imageBase64Str)) {
         return null;
      } else {
         Drawable drawable = null;
         try {
            ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(imageBase64Str.getBytes(), Base64.DEFAULT));
            drawable = Drawable.createFromStream(bais, "");
            bais.close();
         } catch (Exception e) {
            e.printStackTrace();
         }

         return drawable;
      }
   }

   public static final void putObjectByGson(@NotNull String key, @Nullable Object obj) {
      Intrinsics.checkNotNullParameter(key, "key");
      try {
         final String gsonStr = new Gson().toJson(obj);
         putString(key, gsonStr);
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   @Nullable
   public static final Object getObjectByGson(@NotNull String key, @NotNull Class clazz) {
      Intrinsics.checkNotNullParameter(key, "key");
      Intrinsics.checkNotNullParameter(clazz, "clazz");
      String objStr = getString(key);
      if (CommonUtils.isEmptyString(objStr)) {
         return null;
      } else {
         Object obj = null;

         try {
            obj = (new Gson()).fromJson(objStr, clazz);
         } catch (Exception e) {
            e.printStackTrace();
         }

         return obj;
      }
   }

   public static final void putArrayByGson(@NotNull String key, @NotNull List obj) {
      Intrinsics.checkNotNullParameter(key, "key");
      Intrinsics.checkNotNullParameter(obj, "obj");

      try {
         final String gsonStr = new Gson().toJson(obj);
         putString(key, gsonStr);
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   @Nullable
   public static final List getArrayByGson(@NotNull String key, @NotNull Class clazz) {
      Intrinsics.checkNotNullParameter(key, "key");
      Intrinsics.checkNotNullParameter(clazz, "clazz");
      String objStr = getString(key);
      return CommonUtils.isEmptyString(objStr) ? null : (List)(new Gson()).fromJson(objStr, (Type)(new ParameterizedTypeImpl(clazz)));
   }

   public static final class ParameterizedTypeImpl implements ParameterizedType {
      private final Class clz;

      @Override
      public Type getRawType() {
         return (Type)List.class;
      }

      @Override
      public Type getOwnerType() {
         return null;
      }

      @Override
      public Type[] getActualTypeArguments() {
         return new Type[]{(Type)this.clz};
      }

      public ParameterizedTypeImpl(@NotNull Class clz) {
         Intrinsics.checkNotNullParameter(clz, "clz");
         this.clz = clz;
      }
   }
}
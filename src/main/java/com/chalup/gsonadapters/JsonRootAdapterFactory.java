/*
 * Copyright (C) 2013 Jerzy Chalupski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chalup.gsonadapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class JsonRootAdapterFactory implements TypeAdapterFactory {

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  public @interface JsonRootName {
    String value();
  }

  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
    JsonRootName rootAnnotation = typeToken.getRawType().getAnnotation(JsonRootName.class);
    if (rootAnnotation == null) {
      return null;
    }

    TypeAdapter<T> delegateAdapter = gson.getDelegateAdapter(this, typeToken);
    return new Adapter<T>(delegateAdapter, rootAnnotation.value());
  }

  private final static class Adapter<E> extends TypeAdapter<E> {

    private final TypeAdapter<E> mDelegateAdapter;
    private final String mJsonRootName;

    private Adapter(TypeAdapter<E> delegateAdapter, String jsonRootName) {
      mDelegateAdapter = delegateAdapter;
      mJsonRootName = jsonRootName;
    }

    @Override
    public E read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }

      in.beginObject();
      in.nextName();
      E instance = mDelegateAdapter.read(in);
      in.endObject();
      return instance;
    }

    @Override
    public void write(JsonWriter out, E value) throws IOException {
      if (value == null) {
        out.nullValue();
        return;
      }

      out.beginObject();
      out.name(mJsonRootName);
      mDelegateAdapter.write(out, value);
      out.endObject();
    }
  }
}
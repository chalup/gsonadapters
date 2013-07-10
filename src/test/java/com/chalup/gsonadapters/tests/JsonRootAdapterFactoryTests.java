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

package com.chalup.gsonadapters.tests;

import static org.fest.assertions.Assertions.assertThat;

import com.chalup.gsonadapters.JsonRootAdapterFactory;
import com.chalup.gsonadapters.JsonRootAdapterFactory.JsonRootName;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class JsonRootAdapterFactoryTests {

  public static final String JSON = "{\"root\":{\"data\":42}}";
  public static final String JSON_COLLECTION = "{\"wrapped_objects\":[" + Joiner.on(",").join(JSON, JSON, JSON) + "]}";

  @JsonRootName("root")
  public static class WrappedObject {
    @SerializedName("data")
    private long mData;

    public long getData() {
      return mData;
    }

    public void setData(long data) {
      mData = data;
    }
  }

  public static class WrappedObjectsCollection {
    @SerializedName("wrapped_objects")
    private List<WrappedObject> mWrappedObjects;

    public List<WrappedObject> getWrappedObjects() {
      return mWrappedObjects;
    }
  }

  private Gson mGson;

  @Before
  public void setup() {
    mGson = new GsonBuilder()
        .registerTypeAdapterFactory(new JsonRootAdapterFactory())
        .create();
  }

  @Test
  public void shouldReadWrappedObjectFromJson() throws Exception {
    WrappedObject wrappedObject = mGson.fromJson(JSON, WrappedObject.class);

    assertThat(wrappedObject.getData()).isEqualTo(42);
  }

  @Test
  public void shouldWriteWrappedObjectToJson() throws Exception {
    WrappedObject wrappedObject = new WrappedObject();
    wrappedObject.setData(42);

    assertThat(mGson.toJson(wrappedObject)).isEqualTo(JSON);
  }

  @Test
  public void shouldReadWrappedObjectsCollectionFromJson() throws Exception {
    WrappedObjectsCollection collection = mGson.fromJson(JSON_COLLECTION, WrappedObjectsCollection.class);

    assertThat(collection.getWrappedObjects().size()).isEqualTo(3);
  }
}

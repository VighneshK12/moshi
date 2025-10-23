/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.moshi.recipes;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.Json;        // ⬅️ add this
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.ToJson;

public final class FromJsonWithoutStrings {
  public void run() throws Exception {
    String json =
        ""
            + "{\n"
            + "  \"title\": \"Blackjack tournament\",\n"
            + "  \"begin_date\": \"20151010\",\n"
            + "  \"begin_time\": \"17:04\"\n"
            + "}\n";

    Moshi moshi = new Moshi.Builder().add(new EventJsonAdapter()).build();
    JsonAdapter<Event> jsonAdapter = moshi.adapter(Event.class);

    Event event = jsonAdapter.fromJson(json);
    System.out.println(event);
    System.out.println(jsonAdapter.toJson(event));
  }

  public static void main(String[] args) throws Exception {
    new FromJsonWithoutStrings().run();
  }

  private static final class EventJson {
    String title;

    // ⬇️ renamed to camelCase, keep original JSON keys via @Json
    @Json(name = "begin_date")
    String beginDate;

    @Json(name = "begin_time")
    String beginTime;
  }

  public static final class Event {
    String title;
    String beginDateAndTime;

    @Override public String toString() {
      return "Event{"
          + "title='" + title + '\''
          + ", beginDateAndTime='" + beginDateAndTime + '\''
          + '}';
    }
  }

  private static final class EventJsonAdapter {
    @FromJson
    Event eventFromJson(EventJson eventJson) {
      Event event = new Event();
      event.title = eventJson.title;
      event.beginDateAndTime = eventJson.beginDate + " " + eventJson.beginTime; // ⬅️ updated
      return event;
    }

    @ToJson
    EventJson eventToJson(Event event) {
      EventJson json = new EventJson();
      json.title = event.title;
      json.beginDate = event.beginDateAndTime.substring(0, 8);   // ⬅️ updated
      json.beginTime = event.beginDateAndTime.substring(9, 14);  // ⬅️ updated
      return json;
    }
  }
}

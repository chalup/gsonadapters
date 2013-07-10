GsonAdapters
============

Collection of TypeAdapers and TypeAdapterFactories for [Gson](https://code.google.com/p/google-gson/).

JsonRootAdapterFactory
----------------------

To deserialize this kind of object:

```json
{
  "agent": {
    "name": "Smith"
  }
}
```

You'd normally have to use a wrapper class with the Agent class as a sole field:

```java
public class AgentWrapper {
  public static class Agent {
    private String name;
  }
  
  private Agent agent;
}
```

This boilerplate can be replaced with an annotation:
```java
@JsonRootName("agent")
public class Agent {
  private String name;
}
```

License
-------

    Copyright (C) 2013 Jerzy Chalupski

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

/*
 * Bach - Java Shell Builder
 * Copyright (C) 2018 Christian Stein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// default package

import java.nio.file.*;
import java.util.*;

/** Project build support. */
class Project {

  static Project.Builder builder() {
    return new Builder();
  }

  final String name;
  final Path target;
  final Path libs;
  final String version;
  final Map<String, ModuleSource> moduleSourceMap;

  private Project(Builder builder) {
    this.name = builder.name;
    this.version = builder.version;
    this.target = builder.target;
    this.libs = builder.libs;
    this.moduleSourceMap = Map.copyOf(builder.moduleSourceMap);
  }

  @Override
  public String toString() {
    return "Project{" +
            "name='" + name + '\'' +
            ", version='" + version + '\'' +
            ", target=" + target +
            ", libs=" + libs +
            '}';
  }

  static class ModuleSource {
    final String name;
    Path destination;
    List<Path> modulePath;
    List<Path> moduleSourcePath;

    ModuleSource(String name) {
      this.name = name;
      this.destination = Paths.get("bin", name, "mods");
      this.modulePath = List.of(Paths.get(".bach", "resolved"));
      this.moduleSourcePath = List.of(Paths.get("src", name, "java"));
    }
  }

  static class Builder {
    String name = Paths.get(".").toAbsolutePath().normalize().getFileName().toString();
    Path target = Paths.get("target", "bach");
    Path libs = target.resolve("libs");
    String version = "1.0.0-SNAPSHOT";

    Map<String, ModuleSource> moduleSourceMap = new TreeMap<>();

    Builder() {
       moduleSourceMap.put("main", new ModuleSource("main"));
       moduleSourceMap.put("test", new ModuleSource("test"));
    }

    Project build() {
      return new Project(this);
    }

    ModuleSource moduleSource(String name) {
      return moduleSourceMap.get(name);
    }
  }
}

# Project Chimera

Thingplug API

```kotlin
repository {
	maven {
		name = "mavenGithub"
		url = uri("https://maven.pkg.github.com/elex-project/chimera")
		credentials {
			username = project.findProperty("github.username") as String
			password = project.findProperty("github.token") as String
		}
	}
}
dependencies {
	implementation("com.elex_project:thingplug:3.0.0")
}
```

```java
LatestDataResponse response = ThingPlug
        .getLatestData(Ltid.of("xxx", "xxx"),
                UserKey.of("xxx"));
System.out.println(response.toString());
```

---
developed by Elex
https://www.elex-project.com

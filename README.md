# Maven-Uploader

作用: 简单便捷DSL 上传Android Libary 

Vesion：1.0.4

### 集成：
1.在项目的 build.gradle（项目根目录的 build.gradle 文件）设置 `buildscript dependencies` ：
```groovy
buildscript {
      ...
      repositories {
        //**重要**
        maven { url 'https://repo.souche-inc.com/repository/public/' }
      }
      dependencies {
          ...
          //**重要**
          classpath 'com.souche.android.sdk.plugincenter:maven-cn.cheney.uploader:latest.integration'
      }
  }
  allprojects {
      repositories {
          ...
      }
  }
```

2.在android library build.gradle 添加：

```groovy
apply plugin: 'com.android.library'
apply plugin: 'maven-cn.cheney.uploader'

pluginMaven {
    groupId = "xxx"
    artifactId = "xxx"
    version '1.0.0-SNAPSHOT'
}

android {
   ...
}

dependencies {
   ...
}

```

### DSL参数解释：
```groovy
pluginMaven {
    groupId = "Your Group"
    artifactId = "Your ArtifactId"
    //插件会根据‘-SNAPSHOT’后缀上传到不同的repository
    version '1.0.0-SNAPSHOT'
}
```
<a name="YWwdo"></a>
### 执行Task：
```groovy
//artifactId为pluginMaven配置所写
./gradlew publish{$artifactId}PublicationToMavenRepository
```


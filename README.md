![](https://travis-ci.org/geksong/zkdash.svg?branch=master) 
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/3e02e1ef647849838c7e513668dfc907)](https://www.codacy.com/app/geksong/zkdash?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=geksong/zkdash&amp;utm_campaign=Badge_Grade)
![GitHub tag (latest SemVer)](https://img.shields.io/github/tag/geksong/zkdash.svg)
![GitHub release](https://img.shields.io/github/release/geksong/zkdash.svg)

[ZkDash](https://github.com/geksong/zkdash) is an [zookeeper](https://zookeeper.apache.org/) path and data gui browser。Implement by swing and [sgroschupf zkclient](https://github.com/sgroschupf/zkclient)

## Quick Start
#### Install by binary package
- Download latest release version binary package from [here](https://github.com/geksong/zkdash/releases)
- Unzip the downloaded zip file
```
$ unzip zkdash-$version.zip
```
- Make sure you have install JDK8+ and had set JAVA_HOME environment.
- Run ```./zkdash.sh``` on Linux/Mac system, or run ```./zkdash.bat``` in Windows system.

#### Build from source
- Git clone this project
- Make sure you have install JDK8+ and Maven3+
- Run ```mvn package``` in project root directory
- Jar file will be build in ```target``` directory
- And then you can ```java -jar zkdash-$version.jar```


[![usage](https://github.com/geksong/zkdash/blob/master/doc/video1.0.1.png?raw=true)](http://player.youku.com/embed/XNDA4MjE1OTc0MA==)
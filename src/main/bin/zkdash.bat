@echo off

set ZKDASH_VERSION=1.0.1

IF "%JAVA_HOME%" == "" (
    echo "The JAVA_HOME environment variable is not defined correctly."
) ELSE (
    "%JAVA_HOME%"\bin\java -jar .\zkdash-"%ZKDASH_VERSION%".jar
)
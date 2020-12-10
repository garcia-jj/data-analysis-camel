# data-analysis-camel

A Camel PoC to explore ability to process files.


## How to build

This project uses Gradle as dependency management tool. So, to create an executable jar you can just run the following command:

```
./gradlew build
```

Also you can create a docker image with the following command:

```
./gradlew bootBuildImage
```

A docker image will be published over ´data-analysis-camel´ name.
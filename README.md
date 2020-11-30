# MPESA-Biz-No-Detector

The descrption of library

Examples

## Demos

## Prerequisite
- An Azure subscription - Create one for free
- Once you have your Azure subscription, create a `Computer Vision resource` in the Azure portal to get your key and endpoint. After it deploys, click `Go to resource`.
You will need the `key` and `endpoint` from the resource you create to connect your application to the Computer Vision service. You can use the free pricing tier (F0) to try the service, and upgrade later to a paid tier for production.

## Installation

Current Version: [![](https://jitpack.io/v/jama5262/)](https://jitpack.io/#jama5262/)

#### Gradle

Add the following

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

```
dependencies {
    implementation 'com.github.jama5262:'
}
```

#### Maven

```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```
<dependency>
    <groupId>com.github.jama5262</groupId>
	<artifactId></artifactId>
    <version></version>
</dependency>
```

Great the project has been setup 👍

## Usage

There are a two ways you can use the library. Either by starting a vision activity which requires the `CAMERA` feature or the detect 

### Start Vision Activity

### Detect from Function

## Support

Reach out to me at one of the following places!

- Email at jama3137@gmail.com
- Twitter [timedjama5262](https://twitter.com/timedjama5262)

## License

```
MIT License

Copyright (c) 2020 Jama Mohamed

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

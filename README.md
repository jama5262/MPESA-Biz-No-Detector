# MPESA-Biz-No-Detector

An android detection library that uses Azure Computer Vision Read API to detect MPESA business and/or account numbers

Examples

## Demos
Put demos here

## Prerequisite
- Understanding of Microsoft Azure
- An Azure subscription - Create one for free
- Once you have your Azure subscription, create a `Computer Vision resource` in the Azure portal to get your `key` and `endpoint`. After it deploys, click Go to resource.
You will need the `key` and `endpoint` from the resource you created to connect your application to the Computer Vision service. You can use the free pricing tier (F0) to try the service, and upgrade later to a paid tier for production.

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

There are a two ways you can use the library. Either by starting a vision activity which requires the `CAMERA` feature that uses Object Detection to crop out important part of the image and returns the result or by passing your own image to the `detect()` method that also returns the same results

```kotlin
const val CAMERA_REQUEST_CODE = 1
private val mpesaBizNoDetector = MPESABizNoDetector(
    AZURE_VISION_ENDPOINT,
    AZURE_VISION_KEY
)
```

### Start Vision Activity

```kotlin
private fun startVisionActivity() {
    mpesaBizNoDetector.startActivity(this@MainActivity, CAMERA_REQUEST_CODE)
}

// You can also start the vision activty from a Fragment
private fun startVisionActivity() {
    mpesaBizNoDetector.startActivity(this@Fragment, CAMERA_REQUEST_CODE)
}
```

```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == CAMERA_REQUEST_CODE) {
        if (resultCode == Activity.RESULT_OK) {
            val detectedBizNo = MPESABizNoDetector.getActivityResult(data!!)
            
        }
    }
}
```

### Detect from Function
```kotlin
private suspend fun detectFromBitmapImage() {
    try {
        val bitmap = getBitmap()
        val detectedBizNo = mpesaBizNoDetector.detect(bitmap)
        
    } catch (e: VisionException) {
        //Vision detection failed, request user to try again
        Log.e(TAG, "Vision Error -> ${e.message}")
    } catch (e: BizNoSearchException) {
        //Image detected but could not find a valid MPESA
        //business or account number from the image.
        //Request user to try another image and try again
        Log.e(TAG, "Biz no search error -> ${e.message}")
    } catch (e: Exception) {
//                Unknown error found
        Log.e(TAG, "Error found -> ${e.message}")
    }
}
```

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

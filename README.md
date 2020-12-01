# MPESA-Biz-No-Detector

[![](https://jitpack.io/v/jama5262/MPESA-Biz-No-Detector.svg)](https://jitpack.io/#jama5262/MPESA-Biz-No-Detector)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)


An android detection library that uses Azure Computer Vision Read API to detect MPESA business and/or account numbers from images

## Demos

Start Vision Activity | Pass a Bitmap
------------ | -------------
<img src="https://github.com/jama5262/MPESA-Biz-No-Detector/blob/1.0.0/art/art3.gif" height="500px"> | <img src="https://github.com/jama5262/MPESA-Biz-No-Detector/blob/1.0.0/art/art2.gif" height="500px">

## Prerequisite
- Understanding of Microsoft Azure Cloud Service
- An Azure subscription - [Create one for free](https://azure.microsoft.com)
- Once you have your Azure subscription, create a `Computer Vision resource` in the Azure portal to get your `key` and `endpoint`. After it deploys, click Go to resource.
You will need the `key` and `endpoint` from the resource you created to connect your application to the Computer Vision service. You can use the free pricing tier (F0) to try the service, and upgrade later to a paid tier for production.

## Try out the Example
To try out the example app, follow the instructions [here](https://github.com/jama5262/MPESA-Business-No-Detector/tree/1.0.0/app) to set up your Azure Vision `key` and `endpoint` environment variables and to run the example on your android phone

## Installation

Current Version: [![](https://jitpack.io/v/jama5262/MPESA-Biz-No-Detector.svg)](https://jitpack.io/#jama5262/MPESA-Biz-No-Detector)

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
    implementation 'com.github.jama5262:MPESA-Biz-No-Detector:1.0.0'
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
    <artifactId>MPESA-Biz-No-Detector</artifactId>
    <version>1.0.0</version>
</dependency>
```

Great the project has been setup 👍

## Usage

There are a two ways you can use the library. Either:-
1. Start a vision activity
2. Pass your own bitmap image

### Start vision activity

Add Permissions to Manifest
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA" />
```

Initialize `MPESABizNoDetector` by passing your Azure Vision credentials

```kotlin
const val CAMERA_REQUEST_CODE = 1

private val mpesaBizNoDetector = MPESABizNoDetector(
    "YOUR_AZURE_VISION_ENDPOINT",
    "YOUR_AZURE_VISION_KEY"
)
```

Start `MPESABizNoDetectorActivity` using the following code

```kotlin
//Make sure you have requested the user for CAMERA permission before continuing

private fun startVisionActivity() {
    mpesaBizNoDetector.startActivity(this@YourActivityClass, CAMERA_REQUEST_CODE)
}

// You can also start the `MPESABizNoDetectorActivity` from a Fragment. See below
private fun startVisionActivity() {
    mpesaBizNoDetector.startActivity(this@YourFragmentClass, CAMERA_REQUEST_CODE)
}
```

Override `onActivityResult` method in your activity or fragment to get the detected business number result

```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == CAMERA_REQUEST_CODE) {
        if (resultCode == Activity.RESULT_OK) {
            val detectedBizNo = MPESABizNoDetector.getActivityResult(data!!)

            val type = detectedBizNo.type // TILL_NUMBER or PAYBILL
            val businessNo = detectedBizNo.businessNo
            // The account number can return null if the type is TILL_NUMBER or
            // no account number was detected in the image
            val accountNo = detectedBizNo.accountNo // Can return null
        }
    }
}
```

### Pass your own bitmap image

Add Permissions to Manifest
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

Initialize `MPESABizNoDetector` by passing your Azure Vision credentials

```kotlin
private val mpesaBizNoDetector = MPESABizNoDetector(
    "YOUR_AZURE_VISION_ENDPOINT",
    "YOUR_AZURE_VISION_KEY"
)
```
To detect from a bitmap image, use the following code

```kotlin
private suspend fun detectFromBitmapImage() {
    try {
        val bitmap = getBitmap() // Your bitmap image
        val detectedBizNo = mpesaBizNoDetector.detect(bitmap)
    } catch (e: VisionException) {
        //Vision detection failed, request user to try again
        Log.e(TAG, "Vision Error -> ${e.message}")
    } catch (e: BizNoSearchException) {
        //Image read detected but could not find a valid MPESA
        //business or account number from the image.
        //Request user to try another image and try again
        Log.e(TAG, "Biz number search error -> ${e.message}")
    } catch (e: Exception) {
        //Unknown error occured
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

## Demos

Start Vision Activity | Pass a Bitmap
------------ | -------------
<img src="https://github.com/jama5262/MPESA-Business-No-Detector/blob/1.0.0/art/art3.gif" height="500px"> | <img src="https://github.com/jama5262/MPESA-Business-No-Detector/blob/1.0.0/art/art2.gif" height="500px">

## Prerequisite
- Understanding of Microsoft Azure Cloud Service
- An Azure subscription - [Create one for free](https://azure.microsoft.com)
- Once you have your Azure subscription, create a `Computer Vision resource` in the Azure portal to get your `key` and `endpoint`. After it deploys, click Go to resource.
You will need the `key` and `endpoint` from the resource you created to connect your application to the Computer Vision service. You can use the free pricing tier (F0) to try the service, and upgrade later to a paid tier for production.

## Try out the Example
Use this `README` to set up this example app and try it out on your phone or emulator

## Get your Azure Credentials

Get your Azure Vision `key` and `endpoint`

## Create a properties file

In the `app` folder, create a property file and name it

`azureVisionKeys.properties`

<img src="https://github.com/jama5262/MPESA-Business-No-Detector/blob/1.0.0/art/art1.PNG" height="200px">

And in the file add the following Azure Vision Credentials key and endpoint

```
AZURE_VISION_KEY = "YOUR_AZURE_KEY"
AZURE_VISION_ENDPOINT = "YOUR_AZURE_ENDPOINT"
```

## Rebuild and run the project

After the above step, rebuild the project and run the app on your android phone or emulator

That's it, you're done 👍
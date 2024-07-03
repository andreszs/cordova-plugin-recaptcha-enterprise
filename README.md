![npm](https://img.shields.io/npm/dt/cordova-plugin-recaptcha-v2) ![npm](https://img.shields.io/npm/v/cordova-plugin-recaptcha-v2) ![GitHub package.json version](https://img.shields.io/github/package-json/v/andreszs/cordova-plugin-recaptcha-v2?color=FF6D00&label=master&logo=github) ![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/andreszs/cordova-plugin-recaptcha-v2) ![GitHub top language](https://img.shields.io/github/languages/top/andreszs/cordova-plugin-recaptcha-v2) ![GitHub](https://img.shields.io/github/license/andreszs/cordova-plugin-recaptcha-v2) ![GitHub last commit](https://img.shields.io/github/last-commit/andreszs/cordova-plugin-recaptcha-v2)

# cordova-plugin-recaptcha-enterprise

Cordova plugin to implement the score-based [Google reCAPTCHA Enterprise API](https://cloud.google.com/recaptcha-enterprise/docs/overview "Google reCAPTCHA Enterprise API") on android and browser platforms.

# Platforms

- Android
- Browser

# Requirements

- [Prepare your environment](https://cloud.google.com/recaptcha-enterprise/docs/prepare-environment "Prepare your environment") and enable reCAPTCHA Enterprise API
- Create [score-based reCAPTCHA keys](https://cloud.google.com/recaptcha-enterprise/docs/keys "score-based reCAPTCHA keys") for [android](https://cloud.google.com/recaptcha-enterprise/docs/create-key-mobile "android") and [websites](https://cloud.google.com/recaptcha-enterprise/docs/create-key-website "websites") in your[ Google Cloud console](https://console.cloud.google.com/security/recaptcha?orgonly=true&project=plugin-demo-1718371135488&supportedpurview=organizationId,folder,project " Google Cloud console")

# Installation

| Plugin version | Cordova | cordova-android | minSdkVersion | [ReCaptcha](https://mvnrepository.com/artifact/com.google.android.recaptcha/recaptcha "recaptcha") |
| --- | --- | --- | --- | --- |
| 1.0.0 | >= 10.0.0 | >= 8.0.0 | 19 | 18.2.1 |

### Install latest version from NPM

```bash
  cordova plugin add cordova-plugin-recaptcha-enterprise
```

### Install latest version with custom [ReCaptcha](https://mvnrepository.com/artifact/com.google.android.recaptcha/recaptcha) version

```bash
  cordova plugin add cordova-plugin-recaptcha-enterprise --variable RECAPTCHA_VERSION=18.2.1
```

### Install latest version from master

```bash
  cordova plugin add https://github.com/andreszs/cordova-plugin-recaptcha-enterprise
```
⚠ Note that using ReCaptcha 18.5.0 or newer will enforce a minSdk level of 21.

# Methods

## verify

To invoke the reCAPTCHA API, you call the `verify()` method. Usually, this method corresponds to the user's selecting a UI element, such as a button, in your activity.

```javascript
cordova.plugins.Recaptcha.verify(onSuccess, onFailure, [args])
```

| **args** | Object with `siteKeyAndroid` and optional `siteKeyWeb` strings |
| --- | --- |
| siteKeyAndroid | **String**: A *score-based Android* key from the GCC. Required for the Android platform. |
| siteKeyWeb | **String**: A *score-based Website* key from the GCC. Optional for the browser platform. |

⚠ Keys created with the legacy [v3 Admin Console](https://www.google.com/recaptcha/admin/ "v3 Admin Console") do not work with the Enterprise API unless they are upgraded from the Google Cloud Console.

### Return values

- **reCAPTCHA response token**

For any type of reCAPTCHA key integration (checkbox or score), you must create an assessment from your backend by submitting the generated **token** to the assessment endpoint. reCAPTCHA processes the submitted token and reports the token's validity and score.

- [Create assessments for mobile applications](https://cloud.google.com/recaptcha-enterprise/docs/create-assessment-mobile "Create assessments for mobile applications") (android platform)
- [Create assessments for websites](https://cloud.google.com/recaptcha-enterprise/docs/create-assessment-website "Create assessments for websites") (browser platform)

### Example

 ```javascript
var onSuccess = function (strToken) {
    console.log(strToken);
};
var onFailure = function (strError) {
    console.warn(strError);
};
var args = {};
args.sitekeyAndroid = YOUR_ANDROID_KEY_ID;
args.sitekeyWeb = YOUR_WEBSITE_KEY_ID;
cordova.plugins.Recaptcha.verify(onSuccess, onFailure, args);
```

# Remarks

- Score-based keys do not display the *I'm not a robot* checkbox and never show CAPTCHA challenges
- Score-based return a token which you must use to create an assessment that returns a risk score.
- You cannot use Classic keys from the[ v3 Admin Console](https://www.google.com/recaptcha/admin " v3 Admin Console").
- The website key (optional for browser platform) should be created with the **localhost** domain.
- On certain devices, the first call to `verify()` could return [Internal Error](https://cloud.google.com/recaptcha-enterprise/docs/reference/android/com/google/android/recaptcha/RecaptchaErrorCode#INTERNAL_ERROR "Internal Error"). Retrying the operation will return a token.

# Plugin demo app

- [Compiled APK and reference](https://www.andreszsogon.com/cordova-recaptcha-enterprise-demo/)
- [Source code for www folder](https://github.com/andreszs/cordova-plugin-demos)

#### android
<img src="https://github.com/andreszs/cordova-plugin-demos/blob/main/com.andreszs.grecaptcha.demo/screenshots/android/grecaptcha-01.png?raw=true" width="200" /> <img src="https://github.com/andreszs/cordova-plugin-demos/blob/main/com.andreszs.grecaptcha.demo/screenshots/android/grecaptcha-02.png?raw=true" width="200" /> <img src="https://github.com/andreszs/cordova-plugin-demos/blob/main/com.andreszs.grecaptcha.demo/screenshots/android/grecaptcha-03.png?raw=true" width="200" />

#### browser
<img src="https://github.com/andreszs/cordova-plugin-demos/blob/main/com.andreszs.grecaptcha.demo/screenshots/browser/grecaptcha-01.png?raw=true" width="200" /> <img src="https://github.com/andreszs/cordova-plugin-demos/blob/main/com.andreszs.grecaptcha.demo/screenshots/browser/grecaptcha-02.png?raw=true" width="200" /> <img src="https://github.com/andreszs/cordova-plugin-demos/blob/main/com.andreszs.grecaptcha.demo/screenshots/browser/grecaptcha-03.png?raw=true" width="200" />

# Changelog

### 1.0.0

- First version
- Tested on Android 4.4 and up
- Tested on browser platform

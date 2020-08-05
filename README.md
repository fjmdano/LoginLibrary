# Mobile app components for Android

Creating customizable app components to build a modular mobile app
To see how the created libraries are integrated, please see `app/`

## Login Library
This library includes log-in UI that can have customizable text, input field, image, button, and third party sign in support (such as Firebase, Facebook, Google)

#### Location
Please see `loginlibrary/` for the complete source code

#### Setup
To include Login Library in your project, add the following to your app level build.gradle.
```
dependencies {
    implementation project(path: ':loginlibrary')
}
```

## KYC Library
This library includes KYC UI page/s that can have customizable text, input field, image, button, dropdown, list and media.

#### Location
Please see `kyclibrary/` for the complete source code

#### Setup
To include KYC Library in your project, add the following to your app level build.gradle.
```
dependencies {
    implementation project(path: ':kyclibrary')
}
```

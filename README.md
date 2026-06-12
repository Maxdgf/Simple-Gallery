# Simple Gallery
A simple android gallery app📱🖼️ based on **Jetpack Compose**.

## Previews🖼️
in light☀️/dark🌙 themes
<table>
    <tr>
        <td><img src="previews/preview (1).jpg"></td>
        <td><img src="previews/preview (2).jpg"></td>
        <td><img src="previews/preview (3).jpg"></td>
        <td><img src="previews/preview (4).jpg"></td>
    </tr>
<table>

## Tech stack📚
* **Coil** - asynchronous image loading
* **Accompanist** - permissions requesting
* **Jetpack Compose** - UI
* **Dagger-Hilt** - dependency injection

## Permissions⚙️
App requires access to the media.

## How it works🔧
The app uses a **content resolver** to parse media, as well as a **content observer** to observe changes. The **Accompanist** is used to check permissions, if all permissions are granted, you can use the app. If the message doesn't appear, the device will correct it.

## Android versions🔃
Android 8.0 and later.

## Upgrade plans🧾
* Add video display
* Add actions for each media(search, share, delete, show info...)
* Add the ability to customize the UI media grid
* Add "favorites" section
* Add "albums" section
* Add "trash can" section
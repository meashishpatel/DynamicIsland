# Dynamic Island Android App

A modern and dynamic Android app feature that mimics the functionality of iPhone's **Dynamic Island**. This app showcases a floating notification area that expands with details and collapses with just an icon. The feature is designed to be responsive, interactive, and supports notifications, currently playing songs, and more.

## Features
- **Dynamic Island Overlay**: A floating UI element that can expand and collapse based on the interaction.
- **Notification Support**: Displays incoming notifications with icons and content, with smooth animations.
- **Spotify Integration**: Displays the currently playing song with controls, expandable to show detailed track information.
- **Interactive**: Tap to expand, swipe to dismiss, or interact with the floating content.
- **Customization**: The layout is flexible to show different types of information, such as notifications and media playback.

## App Structure

The app uses the following components to achieve the Dynamic Island functionality:

### 1. **Foreground Service**
   - Manages the dynamic island UI, allowing it to stay on top of other apps and function even when the app is closed.
   
### 2. **WindowManager**
   - Used to create a floating view that can be moved and resized on the screen.
   
### 3. **AccessibilityService**
   - Listens for incoming notifications and automatically updates the island with relevant data, such as text and icons.
   
### 4. **NotificationListenerService**
   - Listens to system notifications and updates the dynamic island in real-time as new notifications arrive.
   
### 5. **Spotify Integration**
   - Integrates with the Spotify API to fetch and display the currently playing song. The song's details are shown in the dynamic island and can be expanded with play/pause controls.
   
### 6. **Animations**
   - The floating dynamic island icon expands and collapses with smooth animations, creating a seamless and visually appealing experience.

## Requirements
- **Android 10 (API level 29)** and above
- **Spotify SDK** (for Spotify functionality)
- **AndroidX libraries** for UI components and background services
- **Permissions**:
  - `SYSTEM_ALERT_WINDOW`: To display the floating UI.
  - `INTERNET`: For Spotify integration.
  - `ACCESS_NOTIFICATION_POLICY`: To access notifications.

## Installation

### Clone the Repository
```bash
git clone https://github.com/yourusername/dynamic-island-android.git
cd dynamic-island-android
```

## Build and Run

1. Open the project in Android Studio
2. Ensure all required dependencies are installed
3. Connect a device or use an emulator
4. Run the app

## Showcase

Here's a demonstration of the Dynamic Island in action:

![Dynamic Island Demo](screenrecording/demo.gif)

## How It Works

### 1. Dynamic Island Initialization
The dynamic island view is created and initialized in the foreground service. The floating icon appears as a small circle (or your custom icon) on the top of the screen.

### 2. Handling Notifications
As notifications arrive, the service listens to them and updates the dynamic island with:
- Notification icons 
- Notification content
- Expandable details on tap

### 3. Spotify Integration
When a song is played on Spotify:
- Fetches current track details
- Updates the dynamic island with:
  - Track name
  - Artist
  - Album art 
  - Media controls (play/pause, skip)

### 4. Interactions

| Interaction       | Description |
|-------------------|-------------|
| Tap to Expand     | Shows detailed content (full notification/song controls) |
| Tap to Collapse   | Returns to small icon to save screen space |
| Swipe to Dismiss  | Removes the island from screen |

## Customization

Customize the Dynamic Island with these options:

- **Icon Customization**
  - Change the floating island icon
  - Set different icons for different states

- **Expand/Collapse Behavior**
  - Adjust animation speed
  - Modify expansion size
  - Customize transition effects

- **Notification Layout**
  - Change notification display style
  - Customize text formatting
  - Modify background and colors

## Key Classes

| Class Name               | Responsibility |
|--------------------------|----------------|
| `DynamicIslandService`   | Manages floating island and animations |
| `NotificationListener`   | Handles system notifications updates |

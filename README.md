# Instatt - Mobile Attendance Management App

## Overview
**Instatt** is a mobile application designed to streamline the attendance management process in educational institutions. The app enables instructors to efficiently log attendance and provides students with a seamless way to track their attendance records. By leveraging Firebase for authentication and real-time data storage, **Instatt** eliminates manual errors, reduces workload, and enhances transparency.

## Features
- **User Authentication:** Secure login/logout with Firebase authentication.
- **Attendance Tracking:** Mark attendance in real-time when class status is unlocked.
- **Calendar-Based Attendance View:** Monthly and weekly views for attendance history.
- **Class Schedule Management:** View past and upcoming classes.
- **Absence Reporting:** Submit absence verification forms with file attachments.
- **Export Attendance Records:** Generate and download attendance reports in PDF format.
- **Notifications & Reminders:** Receive class reminders and absentee notifications.
- **Feedback System:** Submit feedback to improve app usability.
- **User Settings:** Manage notifications, change passwords, and update profile details.

## Architecture & Design
**Instatt** follows the **MVVM (Model-View-ViewModel)** architecture for a structured and scalable design.
- **Model:** Handles business logic and Firebase interactions.
- **View:** Comprises UI components (Activities, Fragments, XML layouts).
- **ViewModel:** Manages data presentation and UI interaction logic.

### Firebase Integration
- **Firebase Authentication:** Secure user authentication.
- **Firebase Realtime Database:** Store and retrieve attendance records and user data.
- **Cloud Storage:** Manage absence verification form attachments.

## Implementation Details
### 1. User Authentication
- Users log in with email and password.
- Secure session management and logout handling.

### 2. Attendance Tracking
- Teachers unlock class attendance.
- Students sign attendance via the app.
- Attendance status updates in real-time.

### 3. Class Schedule & Calendar View
- Displays class events in **weekly/monthly calendar** format.
- Users can view past and upcoming classes.

### 4. Absence Reporting & Verification
- Users can submit absence reports with reason and file attachments.
- Reports are sent via email to the institution.

### 5. Export Attendance History
- Generates a **PDF report** of attendance records.
- Allows students to download reports for academic purposes.

### 6. Notifications & Reminders
- Customizable notification alerts for upcoming classes.
- Automated reminders for absentee verification.

## Technology Stack
- **Programming Language:** Java (Android)
- **Backend:** Firebase (Authentication, Realtime Database, Cloud Storage)
- **UI Framework:** XML-based Android UI Components
- **Testing Framework:** JUnit, Espresso
- **Build System:** Gradle

## Installation & Setup
### Prerequisites
- Android Studio installed
- Firebase project configured

### Steps
1. Clone the repository:
   ```sh
   git clone https://github.com/your-repo/Instatt.git
   ```
2. Open the project in **Android Studio**.
3. Configure **Firebase** by adding `google-services.json` to the `app/` directory.
4. Build and run the app on an emulator or Android device:
   ```sh
   ./gradlew build
   ```

## How to Use
### Logging In
- Enter your email and password to sign in.
- If authenticated, you are redirected to the homepage.

### Viewing Attendance
- Navigate to the **Attendance History** section.
- Switch between **weekly/monthly views** for class records.

### Signing Attendance
- Tap the **Sign** button when the class status is **unlocked**.
- Attendance is automatically logged in the system.

### Reporting Absences
- Select a class from your attendance history.
- Fill out the **Absence Form** and attach supporting documents.
- Submit the report, which is sent to the institution.

### Exporting Attendance Data
- Go to **Module Details**.
- Tap **Export** to generate and download a **PDF report**.

### Changing Password
- Navigate to **Personal Details > Change Password**.
- Enter your current and new password to update credentials.

## Testing
### Unit & Integration Tests
- **JUnit & Espresso** for UI and functional testing.
- **Automated UI Tests** for login, attendance tracking, and export features.

### Example Test Cases
#### `LogInTest.java`
- Verify login with correct/incorrect credentials.
- Ensure secure session management.

#### `AttendanceHistoryTest.java`
- Validate calendar-based attendance tracking.
- Ensure real-time updates from Firebase.

#### `ExportAttendanceTest.java`
- Verify attendance export functionality.
- Ensure correct formatting of generated **PDF reports**.

## Deployment
### Steps for Release
1. **Generate Signed APK**
   ```sh
   ./gradlew assembleRelease
   ```
2. **Upload to Google Play Store**
3. **Monitor App Performance** using Firebase Analytics
4. **Regular Updates** for bug fixes and feature enhancements

## Future Enhancements
- **AI-based Attendance Analysis**: Predict attendance trends.
- **GPS & NFC Integration**: Enable location-based attendance tracking.
- **Offline Mode**: Allow attendance marking without internet.
- **Role-Based Access Control**: Separate admin, teacher, and student privileges.

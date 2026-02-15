# EECS4443 Lab 03
This repository is for EECS 4443 W2026 Lab 03 - Input Forms, Local Data Storage & Gesture Handling in Android
## Team Members
| Full Name | Section (Lab) | Student ID | Email |
|----------|----------|----------|----------|
| **Jorel Louie Chim**   | M (Tuesday Lab)   | 217207879   | jorelc@my.yorku.ca   |
| **Chan Woo Hwang**  | M (Tuesday Lab)   | 218972539   | htry02@my.yorku.ca   |
| **Shivraj Banwait**   | M (Tuesday Lab)   | 217279373   | shivrajb@my.yorku.ca   |
| **Asif Javed**   | M (Friday Lab)   | 219913433  | Asif2004@my.yorku.ca   |
## Team Contributions
| Team Member        | Contributions | 
|--------------------|------------------|
| **Jorel Louie Chim** | Main Activity UI, Detail Activity UI, List Items UI, Comments  |
| **Shivraj Banwait** | Main Activity UI, List Items UI, Error Handling|
| **Chan Woo Hwang** | Main Activity UI, Detail Activity UI, List Items UI, Backend Logic | 
| **Asif Javed** | Main Activity UI, Detail Activity UI, Comments | 
## Known Limitations
* For error handling, once the pop-up is shown, the app does not go back to the main activity on its own. Going back to the main activity after the pop up appears or crashing the app could be a better solution/more graceful way to handle errors.
* Every time the orientation changes on the Error Handling pop-up, it displays the pop-up view again.
## Architecture
This app follows the MVVM (Model-View-ViewModel) architectural pattern, leveraging Android Architecture Components.

*   **Model**: The `Contact` class defines the data structure of the application.
*   **View**: `Fragment`s (e.g., `ContactFragment`, `ContactDetailsFragment`) compose the UI and handle user input. They observe `LiveData` from the ViewModel to automatically update the UI upon data changes.
*   **ViewModel**: `ContactViewModel` manages UI-related data and business logic. It's lifecycle-aware, preserving data across configuration changes like screen rotations.
*   **Repository**: `ContactRepository` abstracts the data source, allowing the ViewModel to request data operations without direct knowledge of the underlying database (`ContactDBHelper`).

This structure enhances testability and maintainability by clearly separating concerns.

# Project

## Environment & Tools
### **Joel Lansgren**
#### Hardware
- Processor: AMD Ryzen 7 5800H - 3.20 GHz
- Installed RAM: 16,0 GB
- System type: 64-bit operating system, x64-based processor - Windows 11
- NVIDIA GeForce RTX 3060 Laptop GPU
- NVMe SAMSUNG MZVLQ512 - 475GB
#### Software
- **Vs Code** version 1.97.2
- **Git** version 2.45.1.windows.1
- **Git Bash**
- **Bitbucket**
- **Apache Maven** version 3.9.6
- **Windows**
    - Edition Windows 11 Home
    - Version 23H2
    - OS build 22631.4169
    - Experience Windows Feature Experience Pack 1000.22700.1034.0
- **Java**:
    - openjdk version "21.0.2" 2024-01-16 LTS
    - OpenJDK Runtime Environment Temurin-21.0.2+13 (build 21.0.2+13-LTS)
    - OpenJDK 64-Bit Server VM Temurin-21.0.2+13 (build 21.0.2+13-LTS, mixed mode, sharing)

### **Daniel Berg**
#### Hardware
- Processor: Intel Core i5-8400 @ 2.80 GHz
- RAM: 16 GB
#### Software
- IntelliJ IDEA 2024.1.7 (Ultimate Edition)
- Git version 2.45.1.windows.1
- Apache Maven 3.9.6
- Microsoft Windows 10 Home 22H2 (19045.5608)
- openjdk 21.0.2
- OpenJDK Runtime Environment (build 21.0.2+13-58)
- OpenJDK 64-Bit Server VM (build 21.0.2+13-58, mixed mode, sharing)

## PhotoChronicle
The **PhotoChronicle** is an application that aids people in organizing the images of a folder into subfolders of months and years. It accomplishes this by utilizing the [metadata-extractor by drewnoakes](https://github.com/drewnoakes/metadata-extractor).

As of now, the application only targets images that contain the **Exchangeable Image File Format (EXIF)** metadata, specifically using the **DateTimeOriginal** tag, which indicates when the image was taken. This is sufficient for most smartphones and digital cameras, as they typically store this metadata to ensure the correct date and time are associated with each photo. So, with the help of the metadata-extractor library, most images taken with a digital camera or smartphone can be processed.

The main users of the **PhotoChronicle** application will be individuals looking to clean up their phone's cloud storage or transfer images from their phone or camera to their computer. The app aims to automate the process of organizing backup albums in a simple, no-fuss, and lightweight manner.

To sort a folder the user will just pick the folder of choice with the help of a `JFileChooser`, then the program does the rest for you. Non-image files, folders or image files lacking EXIF metadata are ignored, and the sorting process continues for the images that pass the verification. Each image is then moved to its corresponding `year/month/` folder.

## Purpose
The overarching aim of this project is structured around a collaborative development process, starting with task management using a **Kanban board**, where all tasks begin in a backlog before any coding takes place, ensuring a well-organized and efficient workflow. Then development follows **Test-Driven Development (TDD)** principles, meaning that no production code is written before corresponding tests exist. This approach ensures reliability, maintainability, and a focus on well-defined functionality from the outset. And to maintain a clean and structured codebase, the **Feature Branch Workflow** will be followed, where all contributions to the master branch happens via **Pull Requests (PRs)** in Bitbucket. This ensures that every change is reviewed and integrated in a controlled manner, promoting collaboration and code quality.

The concrete outcome of this collaboration will be the **PhotoChronicle** application described above coded in Java. It will be able to sort a folder that contains images that have EXIF metadata into subfolders of years and months using the **DateTimeOriginal** tag.

### Concrete Goals
#### Collaboration Aspects
- Utilize a **Kanban board** to:
- Have a starting point to introduce team members to the project
- Create conventions that will be upheld
- Make sure development tasks will originate from a backlog and then traverse the lists until it reaches its final state in the "Completed" list.
- Work with a test-driven approach:
- Failing tests are written in **JUnit 5** before any production code is implemented.
- Each test should have only one point of failure
- Both positive and negative behaviours need to be tested
- Both behaviour and design integrity need to be tested
- Adhere to **Feature Branch Workflow** by:
- Implement all new code by branching off the master branch
- Create a PR for review before merging into master

#### General Aspects
- Adhere to Maven as our build system
- Make sure `mvn clean verify` produces a working **JAR**
- All commit messages need to have a header and body
- Both production code and test code need to be properly documented

#### PhotoChronicle
- All graphical setup and updates shall take place on the Event Dispatch Thread (EDT)
- The **MVC pattern** shall be followed. This entails:
- All entities that belongs in this pattern should be packaged accordingly
- No direct communication between views and models, instead actions need to be routed through a controller, and all communication back to the controller should be performed via listeners or callbacks
- Views and models should have no notion about each other or the controller's existence
- When the user selects a folder containing images with EXIF metadata and a **DateTimeOriginal** tag, the **PhotoChronicle** application shall create subfolders for each year, with nested subfolders for months, and move the images into their corresponding month folder based on their metadata.

## Procedures
### Workflow
This project focuses on collaboration and working methods, so the first thing to do is to set up a Trello board that will support an agile workflow. This board will consist of five lists, ordered as follows: **Manifest, Backlog, In Progress, Review, and Completed**.

The **Manifest** list serves as the foundation of the project, providing an introduction, setting up workflows, and establishing conventions and guidelines. The remaining four lists - **Backlog**, **In Progress**, **Review**, and **Completed** - form the core of the Kanban-based agile workflow and operate as follows:

All tasks or features to be implemented in the **PhotoChronicle** application will start in the **Backlog**. Once properly formulated, they will be moved to **In Progress**, where a team member will take ownership and create a new branch from `master`, named after the task for easy identification.

The assigned developer will then work on the feature. When it's ready, they will create a PR in Bitbucket, assign reviewers, and move the Trello card to **Review**. Another team member will then review the changes. If the PR requires further improvements, the task is moved back to **Backlog** with feedback, and the journey starts over. Once approved, the changes are merged into `master`, and the Trello card is moved to the final **Completed** list. With the general workflow set up, the next thing is to start coding

### PhotoChronicle
#### Maven Setup
As this project will be built in Java with Maven as the build system, the first thing that needs to be done is to establish a base setup. The structure divides the code into two main categories: application code and test code. Maven provides a standard directory layout, which helps to keep the project organized:

- **`src/main/java`**: Contains the core application code, including classes, interfaces, and other Java files that define the functionality of the project.
- **`src/main/resources`**: Contains resources such as configuration files, images, or templates that the application might use during runtime.

A key feature of Maven's project layout is that it encourages the mirroring of the **`main`** and **`test`** directories:
- **`src/test/java`**: Mirrors the structure of the `src/main/java` folder and contains unit tests and integration tests that validate the application’s behavior.
- **`src/test/resources`**: Mirrors the structure of `src/main/resources` and holds resources necessary for testing, such as test data files and folders depending on the needs. Since this project will be aimed at sorting folders containing images, `src/test/resources` will contain test folders containing images and files used for validation.

This structure ensures a clean separation between production code and test code, making it easier to manage and maintain the project as it grows.

The **`pom.xml`** file in the root directory is used to manage the project's build configuration, dependencies, and plugins. This file serves as the central configuration for the Maven build system and is used to specify things like:
- Dependencies required for the project.
- Build plugins and configurations for compiling and packaging the application.
- Profiles for managing different environments. If you want a lighter setup for production than developer for example, this can be utilized.

>  Be sure to check out [Maven Repository](https://mvnrepository.com/repos/central) to find the latest stable releases for the dependencies used.

For good measure, a `.gitignore` file specifying paths to be ignored by version control shall also be added to the root directory.

#### Application Development
**Since TDD dictates how unit tests** should be approached, with the basic idea that no production code is written before test to guide the implementation exists, a basic skeleton structure without any functionality can be implemented to create an overview of the project as well as a test file over the `Main` class. This will contain design integrity tests that uses reflection to ensure the class is structured according to best practices for utility classes with a proper entry point. It's main method should only be responsible of instantiating the `ChronicleController` wrapped inside the `Swingutilities.invokeLater()` method to ensure all swing components of the views are created and modified on the EDT.

**Moving on**, the `MainFrame` tests will be created with its accompanying production code. These tests will primarily consist of design integrity tests and component verification tests, ensuring that UI elements are properly initialized. Additionally, a functional test will be implemented to verify that the `JFrame` opens at the center of the screen.

This can be achieved by displaying the frame, retrieving its top-left position using the getLocationOnScreen() method, and comparing it to the expected position. The expected position is calculated by subtracting the frame's size from the screen's size, dividing the result by two, and storing the computed values as a `Point` object representing the expected **x** and **y** coordinates.

To finish the `MainFrame`, skeletons of all other views need to be created as well, and again, without any functionality to test, no test files will accompany them yet. The `MainFrame` itself is simple in its implementation, it has a `BorderLayout` with the different `JPanels` added to the north, center and south constraints as well as some basic setup.

**The `TopPanel`** is also simple in its implementation. The only component in it is a label with the name of the app, hence only design integrity and component verification tests are added before adding the functionality to the class. The `BottomPanel` is also quite straightforward but will contain a button that opens the `InfoDialog` that is used to show information about the app (or alternatively display errors if they occur while attempting to sort the folder). As so, a unit test to ensure that a listener is successfully attached through its `addInfoButtonListener()` method needs to be added as well. Both classes will extend `JPanel` and have `FlowLayout` with a horizontal and vertical gap set to them via a constant in the `AppConfig` file. Additionally, the top panel should be left aligned while the bottom panel should be right aligned.

**Since the `InfoDialog` was mentioned**, let's continue with that class. This is supposed to show information about how to use the app when the info button in the bottom panel is clicked as well as display error messages if something goes wrong with the image sorting and show the result of the sorting if it came out successful. To facilitate this functionality a `setMessage`method needs to be implemented that sets the `infoMessage` label text to the chosen message, this label then needs to be reset in a `hideDialog` method each time the dialog is closed to display the correct message when the info button is clicked. There shall also be a `showDialog` method that sets the location relative to the dialogs owner each time the dialog opens to ensure it's centered in the `JFrame`. Additionally, a close button must be included within the dialog, as it will be set to undecorated for aesthetic reasons. This removes the title bar, making it impossible to move or close the dialog using the standard window controls, including the "X" button.

Once again, before any production code is implemented, unit test that ensure these methods functions needs to exist. So, what needs to be tested is that `setMessage` updates the label, that `showDialog` displays the dialog, that `hideDialog` hides the dialog and resets the message label and that the `addInfoCloseBtnListener` method adds a listener to the close button. In addition to these the custom design integrity and component verification tests needs to be created as well.

The class should extend `JDialog` and have a `GridBagLayout`. It consist of the message label and close button. And since the dialog is undecorated a line border will be set on it to distinguish it from the background of the main frame. Since `JDialog` is a top level container just like a `JFrame` it don't have a `setBorder` method since the window decorations will take priority, however, if `getRootPane` is utilized a border can be set, and since the dialog is undecorated, this will work well. Now, to get the look and feel desired, all that needs to be done is to set some `GridBagConstraints` variables before adding the components to the dialog.

**Next up is the `MiddlePanel` class.** This is the view that will house the `JFileChooser`. It should also contain a label that displays a message that prompts the user to choose an image folder, or the path to the folder if it's selected (this also changes color to red if an error occurred sorting the folder), a clear button for the label and a "Choose Folder" button that opens the `JFileChooser`. So, it needs to contain methods for attaching listeners to the buttons, changing the color on the `JLabel`, clearing the label (which also will reset the color of the path label) and the method that opens the `JFileChooser`, which first of resets the color of the label (in case an error has happened before this and changed the color of the path to red), extracts the path of the selected folder, sets this to the label and uses a callback to the controller to start the `sortFolder` method in the model with the path as its argument. Each of these methods needs to have unit tests created before any production code exist, other than that the usual design integrity and component verification tests needs to exist as well. One thing worth mentioning is that the `showOpenDialog` method opens the `FileChooser`s dialog in modal mode by default, and a setter for changing this isn't available same as for the `JDialog`. This means that to be able to interact with the dialog during tests a `Robot` needs to be used through a background thread to close it, since the code execution halts until the dialog is closed.

The view itself extends `JPanel` just as the other views and shall have a `GridBagLayout` that automatically centers the components, which are: a 'JPanel' that acts as a wrapper for the `JLabel` and the clear button, the `chooseFolderBtn` and the `JFileChooser`. The wrapper is used to simulate a common field for the path label and clear button, which could become handy if let's say an `ImageIcon` with a transparent background will be implemented for the clear button in the future instead of the default. Other than that, the file chooser only needs to have its selection mode set to directories only and the same `AppConfig` constant that is used for the path label can be used for its dialog title bar text.

**The `ChronicleController`** is the class that eventually will connect the middle panel where the user selects a folder to sort with the model that does the actual sorting, besides that it will also coordinate interaction between the bottom panel and the info dialog so the information popup will show when the info button is clicked. It shall hold methods to initialize the listeners for the views buttons and eventually set the main frame as visible.

Since the `ChronicleController` is a middleman of sorts, integration test will be its core focus. It will test that the set listeners works as intended by opening and closing the dialog, that choosing a folder in the `MiddlePanel` class sets the Path variable in the model that is used to identify the image folder, that cancel operation in the `MiddlePanel` don’t set the path in the model, that a faulty folder displays an error message in the dialog, that a valid folder displays an info message in the dialog and that the `pathLabel` in the middle panel turns red when a folder emits an error. It will also have a unit test for the `showMainFrame` that verifies it sets its visibility to true.

To test the dialog the `doClick` method of the `JButton` components will be utilized inside `invokeAndWait` to ensure the interaction takes place on the EDT.  There will also be a similar test that the mainframe had, to verify it was opened in the middle of the screen. For the dialog though, it needs to check its opened in the center of the mainframe instead. To make sure this is the case and not just that both has the `setLocationRelativeTo(null)`, a random position will be set on the mainframe before making it visible.

To test the interaction between the model and the middle panel a `Robot` can be used again to simulate key presses on a background thread, then - for each operation, enter or escape, the path in the model needs to be validated against the expected result. Null if no folder was selected and equal to the path label's text if it was.

To test that the correct message is displayed in the info dialog the `sortFolder` helper method of the controller should be called with both a valid and invalid path to make sure that the right type of message is displayed. Additionally, to test that the path label in the middle panel turns red when a path generates an error, just pass an invalid path to the `sortFolder` method and check that the actual color of the label is red.

The `ChronicleController` constructor is responsible for instantiating all the necessary components for the application. It first creates the panels, as they are required by the `MainFrame` constructor, and should therefore be instantiated before the main frame itself. The `InfoDialog` is then instantiated after the main frame, as it needs the main frame as a parameter. Finally, the `ChronicleModel` is created as is, since it doesn't depend on the other components.

The class will contain a single public initialize method, which acts as the entry point to set up the application. This method should call the helper methods `initializeListeners` and `showMainFrame`. The `initializeListeners` method attaches listeners to the various components. In particular, it adds a listener to the "choose folder" button in the middle panel, and when a folder is selected, it invokes its `sortFolder` method as a callback.  Similarly, the **controller's** `sortFolder` method sets up the `displayInformation` and `displayError` methods as callbacks to the **model’s** `sortFolder` method, enabling the flow of information from the model back to the info dialog.

**Lastly there is the `ChronicleModel`.** This will be the class that utilizes the metadata-extractor library if a folder passes the `verifyAccess` method and it will be where the meat of the business logic takes place. It will have two public methods. One for setting the path that will be used throughout the class and the `sortFolder` method. The `sortFolder` method will be used to verify the path, reset variables upon each start of a sort cycle, detect if the folder contains files with **EXIF** metadata, and move any files where the **DateTimeOriginal** tag was found into the correct nested subdirectory of `year/month` as well as utilize the consumer callbacks to display messages if an error occur or statistics of the sort if it was successful. It will also contain a method to set error messages for read or write access and a `nullifyPath` method that shall be used by the test file to reset it after each test method.

Being the class with the meat of the business logic it also is the class with most unit tests, so test needs to be created that ensure the `setPath` method works as intended, that paths creates the correct messages in the `setErrorMessage` method together with its parameter, that exceptions are thrown for invalid paths and not for valid ones as well as checks that verify that valid and invalid paths generate the correct return messages.

To get correct test result for invalid folders the access level needs to be set programmatically through a `setFolderAccess` method. This method shall retrieve the systems access control list (ACL) for the current folder and store the original for later restoration in a tear down method that is executed after each test method. Then an ACL entry needs to be created of the `DENY` type for everyone, with the permission that is passed into the method as a parameter. This entry should then be added to the beginning of the ACL list. This is because windows stop checking as soon as it finds a matching rule, and since this one is applicable to everyone, it will take precedence. Once modified, the updated ACL list should be applied to the folder, making the restrictions take effect.

To verify that the correct information message is generated, the `statistics` array in the model can be modified to match the expected results based on the test folder in `resources`. To do this, the private enum declared in `ChronicleModel` needs to be made accessible. This is achieved through reflection by retrieving the declared classes of the model. Since it's an enum, its constants are accessed via the `getEnumConstants` method, which returns an array of objects. Each object in this array must be cast to an enum constant so that its `ordinal` method can be used to determine the corresponding index in the `statistics` array. The index to modify is determined by matching the enum constant's name with the appropriate case in the `switch` statement.  Once the `statistics` array has been updated, all that is left to assert that the message generated by the model’s `sortFolder` method matches the expected message. Since the sort method resets the statistics array on each run, this verification will reliably confirm that the correct information message is generated for the given folder.

Additionally, tests are required to ensure that directories are created correctly and that files are moved as expected. To achieve this, three helper-methods should be created. Each of these methods will return a Boolean value to verify specific aspects: first, to confirm that each year folder is created; second, to check that each month folder is present; and third, to validate that each file is moved into its appropriate folder. The results of these checks will then be asserted using `assertTrue`. All these methods rely on a map that mirrors the expected result after sorting the test folder in `resources`.

First, the `sortFolder` method will be called with the path to the test folder in `resources`. Then, the `forEach` method is used on the map to iterate over each year, month, or file. For each folder or file, the `Files.exists` method from Java NIO is called to check if it exists, and the result is stored in a locally created list. Each method then returns a boolean value indicating whether any `false` values exist in the list, confirming that all required directories and files are present.

Other than these the regular design integrity tests are implemented to ensure the class is constructed correctly

The 'ChronicleModel` class itself, as stated earlier, shall be responsible for sorting the chosen folder of images into nested subfolders of year and date if an EXIF metadata and **DateTimeOriginal** is found in the images in the folder. If errors occur during the sorting process, these should be communicated to the user via callbacks to the info dialog which will display them, the same goes for statistics of the sort once it has been performed. To achieve this a few things needs to fall into place.

First, a verify method that catches invalid paths needs to be employed. This method will check if the path is invalid in several ways and throw a corresponding checked exception, which will then be caught in the `sortFolder` method. The method will return a message to the info dialog to inform the user of what went wrong.

Second, a reset method will be implemented to clear the map that stores information about all eligible files for moving and reset the statistics array. This ensures that users can sort multiple folders without errors. The statistics array, an `int` array, will have 4 elements corresponding to the number of constants in the `StatsIndex` enum. Then, the `ordinal` method of the enum constants will be used to safely access and modify the appropriate index in the array.

The third step will be to detect if the files in the folder contain EXIF metadata and have the **DateTimeOriginal** tag present, if so, they will be added to the `eligibleFiles` map mentioned above. To do this the Java NIO library will be utilized to create a stream over the directory content and filter out each file that is not a directory and then check them in a dedicated `detectEXIFMetadataFiles` method. This method extracts the metadata with the help of the metadata-extractor library, and if the `ExifSubIFDDirectory` contains an original date, it is converted into a `LocalDate` object. The file is then organized in the eligibleFiles map by year and month, creating entries as needed before storing the file name.

And finally, the folders need to be created, and the images moved to their correct places. This will be achieved by calling the `forEach` method on the `eligibleFiles` map and for each year create a year path that builds upon the basepath set up at the beginning of the method. Then inside the `forEach` the same thing will be repeated on the months part of the map which also is a map consisting of an integer which represent the number of the month and a list of strings which represent the filenames for the images that belong to that month. Here the month path will be built upon the year path created one step above and then converted to a file object. Next, the method need to check whether the necessary directories exist, creating them if needed using `mkdirs`, which ensures that any missing parent folders are also created. Once the folder structure is in place, the process should continue with moving the files to their designated locations. The `forEach` method will be called on the files list within each month’s map, and for each file, the `moveFile` method is invoked with its original path and the corresponding destination inside the newly created month folder. The `moveFile` method then attempts to move the file using `Files.move`. If successful, the count of sorted files in the statistics array is incremented. If an error occurs, it is instead counted as an unsorted file.

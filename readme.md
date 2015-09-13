##Mobile Proto Lab 2


###Objectives

Build a photo feed app that should be capable of:

- Searching the internet for pictures using Google
- Displaying these pictures on an application
- Saving select pictures to a "feed" that can be viewed always
- Deleting photos from the feed

To accomplish these goals you will have to learn/use:

- Switching between fragments
- Saving infromation to a SQL database
- Make HTTP Requests using Volley
- Understand setup process for third party APIs


###Implementation Tips
Since this lab is longer we will give you some guidance on implementation:

- This lab should contain 1 activity and 2 fragments. One fragment should contain functionality necessary to search for images and add them to your feed and the other should display your feed.
- There are two ways to display images in Android, an ImageView and a WebView. WebView is likely easiest.
- To display multiple images the best looking way is likely a ScrollView, but it is fairly difficult to handle some things with a scroll view so we recommend using two buttons to change the currently displayed image. 

####Configuration for Google Custom Search API

####Saving data
While it is possible to save the necessary data here in SharedPreferences, we want you to set up the local SQL database on your phone. 
This link provides a very good guide
http://developer.android.com/training/basics/data-storage/databases.html
It is likely you do not want to save the actual image to the database. Find something that can be used to load the image instead

###Grading:

Functionality 75%

- Searching web for images
- Displaying images on app
- Saving images to database
- Loading images from database 
- Switch between fragments without crashing

Code Quality 25%

-Good MVC practices
- Android best practices


# ApplicationUsingOnlySystemAPIs
A android application that fetches articles from a static end point, displays it as a list and opens it in a WebView when clicked. 
This application has used mostly only system provided APIs as far as possible and I have avoided using additional third party libraries for networking and data storage. 
Though it does use Dagger 2 for dependency Injection.

App Name: Headlines
Languages Used : Java
Threading Mechanism : Java Concurrency API
Artchitecture: MVP - Dagger 2, Java Executor and SqliteOpenHelper used to set up the structure.

This app fetches articles from a static end point. Stores favourited articles in offline mode. 
This was made by me to implement MVP without the help of Retrofit 2, RxJava and Kotlin as I have done in my professional life and to learn Java Concurrency Apis 
in an actual application.


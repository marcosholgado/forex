# ForEx

*Note:* To activate the selection mode just do a long click in the first row that you want to select

## Architecture

I have been playing around with clean architecture lately and I wanted to give it a go, this a simplified version based on the following articles:

https://antonioleiva.com/clean-architecture-android/ 

https://fernandocejas.com/2018/05/07/architecting-android-reloaded/

Each layer has its own models that we have to map to move them from layer to another, this allows us to be able to switch to different data sources or add new logic with ease. Also by modularizing the codebase we get lower build times.
The architecture consists of 3 layers: domain, data, ui. The only module with Android dependencies is the ui module (aka our app). This allows us to easily unit test all the business logic and different implementations of the interfaces in a simple way.

### Domain
Here is where the use cases live so the business logic is isolated from the rest of the code. This layer doesn't depend on the other ones.

### Data
Here we define the abstractions of our data sources. In this case I have just created a `RemoteRepository` that's used in the `CurrencyDataRepository`. If we wanted to integrate a cache or database or any other type of data we could just create new interfaces and use them in the `CurrencyDataRepository`.

### UI
I have gone for a simple approach without splitting the UI in more modules (presenters, framework, etc). In this module we have the implementation of our data sources and our activities.

For the activities I'm using a MVVM pattern with the new architecture components (LiveData, ViewModel, etc).

## Dependency Injection
I like Dagger and that's what I've used as the DI framework. To reduce some boilerplate rather than using plain dagger I'm using dagger.android. Using DI allows us to inject all the needed dependencies and mock them to easily change their values for easy testing.

## Tests
All the modules are covered by unit tests. There are also a couple of integration tests in the `MainActivity` to demonstrate them.

## Security
The api key is harcoded in the `gradle.build` file but it should be put in an ENV variable inside CI.

## Android Studio
The app is built using Android Studio 3.2.1

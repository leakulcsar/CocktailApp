# Cocktail test app

This simple app contains 2 screens:
* [HomeScreen](app/src/main/java/com/example/cocktailapp/presentation/home/HomeScreen.kt):
    * Responsible for displaying the "Cocktail of the day" recommendation, a user's favorite cocktails "Favorite cocktails"
    * Also responsible for handling searching for cocktails and displaying search results
* [DetailScreen](app/src/main/java/com/example/cocktailapp/presentation/detail/DetailScreen.kt):
    * Displays the details of a cocktail once selected

The app is built using a layered architecture where we have the:
* presentation layer - responsible for retrieving the necessary data from domain for the UI and displaying it
* domain layer - defines the business logic domain of the app
* data layer - provides the data implementation details needed for by the domain layer

From a technical perspective the app uses:
* Compose for the UI components, screens and theming
* Kotlin Coroutines and Flows for async operations
* Single-activity structure where [MainActivity](app/src/main/java/com/example/cocktailapp/MainActivity.kt) set's up the Compose app
* AndroidX Navigation Compose is used for handling navigation between screens and the navigation backstack
* AndroidX Hilt (Dagger2) is used for dependency injection
* Retrofit and Moshi for the Network operations
* Room for saving cocktails locally in a SQLite database
* MVI architecture

## domain

The domain contains our model classes, data access contracts and re-usable business logic.

## data

The data provides the implementation details for the contracts defined by the domain layer.

## presentation

The presentation package contains everything related to UI
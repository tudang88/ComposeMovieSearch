# MovieSearch App

## 1. Basic Introduction

| No   | Screen         | Description                                                                                                           |
| :--- | :------------- | :-------------------------------------------------------------------------------------------------------------------- |
| 1    | SearchScreen   | The screen where user <br> can search a movie by typing the movie name                                                |
| 2    | FavoriteScreen | The screen where user <br> can check the list of favorite movie. User can delete from favorite by click favorite icon |
| 3    | DetailScreen   | The screen where user <br> can check details info of movie, mark as favorite or un-favorite                           |

## 2. Application Architecture
The app architecture applies the MVVM pattern as follows.
* Figure 1. App Achitecture
<img src="/doc/app_architecture.png" alt="App Architecture" />


## 3. Transition Flow
The app will perform the flowing operation base on the user's action.
* Figure 2. Reference transition flow
<img src="/doc/runing_flow.png" alt="Transtion Flow" />

## 4. Some special libraries used in this application
| No   | Item                 | Library   | Details                                                                     |
| :--- | :------------------- | :-------- | :-------------------------------------------------------------------------- |
| 1    | RestApi              | retrofit2 | The app use Restrofit2 for interacting with The Movie Database API          |
| 2    | Json Converter       | Moshi     | The app use Moshi as a convert when extract the response from Restrofit API |
| 3    | Dependency Injection | koin      | The app use koin as Domain Service Locator for Dependency Injection         |

## 5. Implement for testing
### 5.1 Unit Test
* The Unit Test is focused on testing live data on each ViewModel
* All the Unit Test source code are located in the below path.
  ```
  app/src/test/java/com/example/moviessearch
  ```
* Unit Test details implementation
  | No   | Category      | Test Class                       | Test Target                              |
  | :--- | :------------ | :------------------------------- | :--------------------------------------- |
  | 1    | LiveData Test | DetailsFragmentViewModelTest.kt  | Live Data of DetailsFragmentViewModel    |
  | 2    | LiveData Test | FavoriteFragmentViewModelTest.kt | Live Data of FavoriteFragmentViewModel   |
  | 3    | LiveData Test | SearchFragmentViewModelTest.kt   | Live Data of SearchFragmentViewModelTest |

### 5.2 Double Test And End To End test using AndroidTest
* The double test is focused on the test which could not be performed on the local machine. It requires an Android machine like an emulator or real device.
* The End To End test will perform operations like a real user by interacting with an Android machine but using test data.
*  All the test implementation source code are located in the below path.
  ```
  app/src/androidTest/java/com/example/moviessearch
  ```
* Test implementation details
  | No   | Category       | Test Class                   | Test Target                                                                                                 |
  | :--- | :------------- | :--------------------------- | :---------------------------------------------------------------------------------------------------------- |
  | 1    | Database Test  | FavoriteMovieDatabaseTest.kt | verify the operation of the database. During the test, the database will be created on memory for test only |
  | 2    | UI Test        | DetailsFragmentTest.kt       | confirm the UI of DetailsFragment using test data                                                           |
  | 3    | UI Test        | FavoriteFragmentTest.kt      | confirm the UI of FavoriteFragmentTest using test data                                                      |
  | 4    | UI Test        | SearchFragmentTest.kt        | confirm the UI of SearchFragmentTest using test data                                                        |
  | 5    | NavigationTest | AppNavigationTest.kt         | perform a scenario of navigation to all screens of the application                                          |
## 6. Require API KEY to run the app
The app require api key for accessing the movie database API.
Please replace the below setting in _gradle.properties_ then rebuild before running the app.
```
# the movies database api key
tmdb_api_key="API_KEY"
```
## 7. Demo Video
<video src="/doc/demo_video.webm" width=491 heigh=855/>

## 8. Consider
* Improve UI design
* Improve user experience by more animations
* Add more function like trending, allow watching trailer
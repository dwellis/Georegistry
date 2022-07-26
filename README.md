# Georegistry

Welcome to Georegistry, a location-based application for quick processing of user forms.

To run the application:

*REQUIRED
Android Studio 2021
Android API S or higher

- Clone the repo into your AndroidStudioProjects folder or directly from within Android Studio
- <b>Before Running: You must add your Google Maps API Key to local.properties</b>
  - You must make an account with Google Cloud
  - Go to your Google Maps Console and go to Credentials
  - On the Credentials page click Create > Credentials > API Key
  - Copy the created API key and open the local.properties file in your root directory
  - Add a new variable under the sdk that reads MAPS_API_KEY=<your key>
- <b>Before Running: You must add a google-services.json file to your app directory</b>
  - Log in to firebase and visit the console
  - Create a project that will store the database and provide authentication for the application
  - Go to Project Settings by clicking the project name or clicking the gear in the menu
  - Scroll down until you see the download for google-services.json (its a little down)
  - Take that downloaded file and paste it in your app directory
- Run the app

# Project Description

Our project is to create a Steam mobile application named “Steam inHand” that focuses on the game news and user profile. Users can use a search bar for finding some specific information about games and friends. Users can share the interesting news to your friends and share the searched user’s profile to friends using messages or other third-party apps. The current Steam application is well-designed but contains various functionality which is overwhelming and unfriendly to the new users. Thus, we are trying to build a user-friendly and single-purpose Steam app for the user profile by utilizing a small amount of information presented from the Steam Web API in a desirable way.

# API use
The API we will use is the Steam Web API.
Link: https://developer.valvesoftware.com/wiki/Steam_Web_API
URL:  	http://api.steampowered.com/ISteamNews/(API menthods)
http://api.steampowered.com/ISteamUser/(API methods)
API method lists:
GetNewsFromApp will be used for getting the news from the api. This will return the latest news of a game specified by the GameID.
GetPlayerSummaries will be used for getting the basic profile information of a user in Steam. This will return the specific user information by searching UserID. User’s information includes its SteamID(UserID), SteamUsername, SteamAvatar, lastlogoff, etc.
GetFriendList will be used for getting the friend list. This will return the friend list of the current user.
GetPlayerAchievements will be used for getting the achievement information. This will return a list of accomplishments for the specific UserID.
GetOwnedGames will be used for getting the owned game. This will return a list of games a player owns along with some playtime information.
GetRecentlyPlayedGames will be used for getting recent played information. This will return a list of games a player has played in the last two weeks.

# UI Design Insights
This app will have four main pages. The first one is for general information. It will show the picture of popular steam games and the new release game info and news. Second page will have a search bar for finding the news of specific game. Then shows the list of news below the search bar. When user click the news, it will show the content of news and there will be a share button on the right, top of the page. User can share the news to your friends via Message or some other app. Third page is for finding the specific user. UI design is the same as the second one. Same feature as searching news. Also includes share feature. The last one is a profile and setting page. It will show the current user info with profile picture and username. In setting page, user can change the notification preference, language, privacy and appearance(Light Mode or Dark Mode). There is a logout button on the bottom of this page.

# Additional Feature
We will implement an additional feature called dark mode. When the user turns the Android device into dark mode, the theme of the application will be majorly using dark gray, and the text throughout the application will become white.

# Design Prototype
https://www.figma.com/file/3jQD7cMSyHKb9bSC6hBEfV/visual-prototypes-for-cs492-final?node-id=0%3A1
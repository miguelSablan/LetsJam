# CodePathTeam
Unit 8: Group Milestone 
===

:::info
**Group Project README**
:::

# LET'S JAM

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)

## Overview
### Description
An app that allows users to watch videos together in a room and add videos to a queue playlist.

### App Evaluation
- **Category:** Music
- **Mobile:** This app would only be avaiable on mobile devices.
- **Story:** Allows user to come together and watch videos together
- **Market:** Anyone who enjoys watching videos with their friends.
- **Habit:** This app could be used as often as the user has one or more friends wanting to join together and watch videos.
- **Scope:** We would allow users to create a lobby and others will be able to join. This could evolve into sharing other forms of media

## Product Spec
### 1. User Stories (Required and Optional)

**Required Must-have Stories**

- [X] Users will be able to login/register.
- [X] Users will be able to create a room that will allow others to join.
- [X] Users will be able to watch YouTube Videos in room.
- [ ] A user will be able to control the room.
- [X] Users will be able to add to playlist queue.


**Optional Nice-to-have Stories**

- [ ] Users will be able to chat with each other in a room.
- [ ] Users can like or dislike a video.

### 2. Screen Archetypes

* Login 
* Register - User signs up or logs into their account
   * Upon downloading users will be able to register or login.
* Lobby Screen 
   * Allows users to join a room of their choice.
* Profile Screen 
   * Allows user to upload a photo
* Room Screen
   * Allows users to watch videos and add to queue.

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Home
* Profile


Optional:
* Chat
* Settings

**Flow Navigation** (Screen to Screen)
* Forced Log-in -> Account creation if no log in is available
* Lobby Selection -> Jumps to Room
* Room -> Music to be Queued 
* Profile -> Text field to be modified. 


## Wireframes
<img src="wireframe.png" width=800><br>

### [BONUS] Digital Wireframes & Mockups
<img src="youtubeapi2.gif" height=300 width=800>

## Schema 
### Models
#### Song

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | songID        | String   | ID of the song |
   | songTitle     | String   | The title of the Strong |
   | songImage     | File     | album image of the song |
   | songArtist    | String   | The song artist |


#### User

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | userName      | String   | The name of the user |
   | profileImage  | File     | profile picture of the user |
   | userID        | String   | ID of the user |

#### MusicPlayer

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | musicID       | String   | The ID of the Song |
   | albumCover    | Image    | Album cover of the song |
   
### Song List

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | songID        | String   | The title of the Song |
   | songList    | Pointer -> Song[]  | List of songs |


#### Listening Room

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | roomID        | String   | The ID of the room |
   | userList      | Pointer -> User | The list of users in the room
### Networking
#### List of network requests by screen
   - Login Screen
      - (Create/POST) Login in user
   - Signup Screen
      - (Create/POST) New user
   - Lounge Screen
      - (Read/GET) Get open rooms
      - (Create/POST) Create a new room
      - (Delete) Close your room
   - Room Screen
      - (Read/GET) Get all users in room
      - (Read/GET) Get music playing in room/ media player
      - (Read/GET) Get music playlist
      - (Read/GET) Get user favorites playlist
      - (Create/POST) Add song to music queue
      - (Delete) Close your room
      - (Delete) Exit room
   - Profile Screen
      - (Read/GET) Query logged in user object
      - (Update/PUT) Update user profile image
      - (Update/PUT) Update user albums
      - (Update/PUT) Update user artists
      - (Update/PUT) Update user genre


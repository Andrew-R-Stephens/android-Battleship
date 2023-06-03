<div style={'display:float' align='center'}>
    <img src="https://raw.githubusercontent.com/Andrew-R-Stephens/android_Battleship/main/app/src/main/res/drawable/battleship_banner.png"/>
    <h1 style="color: ghostwhite">Battleship</h1>
</div>

<br>

<div style="display:float" align="center">
    <img src="https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white&style=bold"/>
    <img src="https://img.shields.io/badge/Android%20Studio-3DDC84?logo=androidstudio&logoColor=white&style=bold"/>
    <img src="https://img.shields.io/badge/Kotlin-7F52FF?logo=kotlin&logoColor=white&style=bold"/>
    <img src="https://img.shields.io/badge/Firebase-Auth-FFCA28?logo=firebase&logoColor=white&style=bold"/>
    <img src="https://img.shields.io/badge/Firebase-Firestore-FFCA28?logo=firebase&logoColor=white&style=bold"/>
</div>

<br>

<p>Two player local versus mode with highscores registering into a Firestore database.</p>

<div style="display:float" align="center">
  <h1 style="color: ghostwhite">Documentation</h2>
</div>

### Overview
<p>This project is a digital rendition of the popular board game, Battleship. It allows for two-player gameplay shared on the same device.</p>

### How to Play
> The goal of both players is to design a layout of ships within their personal grid. The layout of ships within the grid should not be easily guessed by the other player.</p>
> One player will contest the other player’s grid by selecting a particular coordinate in order to probe that coordinate for a ship. Should the defending player have a ship at that coordinate, 
  the attacking player will receive another chance to repeat the action on another coordinate to guess another ship position. If the defending player does not find a ship at that coordinate, there will be a 
  turnover of control to the other player.</p>
> When one player has all their ships hit, the other player wins.

<br>

### Design
> The project follows a multi-activity lifecycle. The project also follows a multi-fragment lifecycle.

#### Landing Page (Main Activity)
> The first page of the application is the landing page. This is a fragment encapsulated within the main activity. The fragment layout consists of a simple start button. The device owner will accept that button to enter into a new pre-game match setup page.

#### Setup Page (Game Activity)
> The second page of the application contains a name input for both players. These names will be reflected within the other fragments to name specific players for their ownership or for requesting their action. This name will also be recorded to the Winners list in Firestore.
> This page also contains a grid with which the player will be asked to arrange the locations of their ships. The number of ships allowed is followed by the algorithm:

> gD = requested grid side size
>	nS = number of ships spaces in Hasbro’s Battleship
>	nG = number of grid spaces in Hasbro’s Battleship
>
> (gD * gD) * (nS / nG)
> This algorithm was done to avoid clutter and maintain a ratio similar to the original Battleship. In this game’s case, there are 7 grid slots, which gives us 49 grid spaces. In Hasbro’s game, there are 17 ship spaces and 100 grid spaces. This means that this game’s grid will allow for a total of 8 ships to be placed by each player.
> A player will have to insert all of their ships and their name in order to apply the changes.

##### Swap Page (Game Activity)
> To prevent screen watching, a swap page has been implemented. This obstructs the player being given the device for their turn from being able to see the information of the previous player.
> The swap page contains one confirmation button and a request to the previous player to hand off the device to the other player.
> The swap page is used for both the setup phase and the game phase. The labels are modified based on the stage of the match.

##### Game Page (Game Activity)
> The game page will contain two grids. One grid will be to display the current player’s grid. The other grid will be to display the enemy player’s obfuscated grid.
> For the player’s own grid which is located at the top right of the device, the imagery of the grid will contain a small 7x7 table of ship coordinates. One top row and one side row are added for grid coordinate labels. Each ship coordinate will be filled with the player’s own ships, unless their ships have been hit, in which case a hit indicator will be visible. Enemy misses will also be displayed.
> For the enemy player’s grid which is located centered at the bottom of the device, the imagery of the grid will contain a small 7x7 table of ship coordinates. One top row and one side row are added for grid coordinate labels. Each ship coordinate will be filled with blank spaces, unless the enemy ships have been hit, in which case a hit indicator will be visible. The player’s misses will also be displayed.

##### Score Page (Game Activity)

> The score page displays the statistics of both players. It also contains a list with the previous three most recent winners. This list is read from Firestore.
> There is also a button available which will allow the device owner to start a new match. This will wipe stored data and send the user back to the Setup Page.

<br>

### Low-Level Design

##### Lifecycle
> There will be two Activities containing Fragments. These Activities (Main Activity and Game Activity) and Fragments (Main Fragment, SetupFragment, GameFragment, AwaitSwapFragment, ScoreFragment) will be swapped between based on the game-flow.

> The Main Activity will contain a ViewModel (MatchViewModel) which will hold the data responsible for passing data between the game-state fragments. This ViewModel will persist throughout all Activities.

#### Views
> There will be 49 views for each grid. These views are inflated and added to other nested inflated views at runtime to minimize code repetition and allow for similar behavior across all views.
> Certain grid components contain listeners for the use of turn-based play. 

##### Animations
> Animations were used for added effects to the ship hits or misses.

#### Firebase
> Firebase was used to maintain a database of winners and allow for the game to read from that list to pull the most recent winners.

<br>

### Activity Classes

##### MainActivity
> The container for the landing page. Used almost as a stand-in.

##### GameActivity
> The container for all game-based classes

<br>

### Fragment Classes

##### MainFragment
> The fragment used for transition into the game state and GameActivity.

##### SetupFragment
> The fragment used for setting up player grids and player information.

##### AwaitSwapFragment
> The fragment used for information hiding during handoffs.

##### AGridFragment
> The fragment used as abstract base class for all Fragments using grids.

##### GameFragment
> The fragment used for the gameplay. This will be the second-most used fragment.

##### ScoreFragment
> The fragment used to display post-match information and Firestore data.

<br>

### Data Classes

#### MatchViewModel
> This class is a ViewModel, which exists between layout changes. It retains information between activity changes and is only cleared upon rematch.

##### Grid
> This class retains the grid data for any grid. The state of a coordinate is represented by enums which specify a potential ship state.

##### Player
> This class contains information for each player in the match. It also retains some data about their current state within the match, such as their shit and miss counters.

##### TurnHandler
> This class retains the current Players and acts to mediate their turns

### Resources

##### Layouts
> There are a number of layouts used for activities and fragments. Layouts have also been used for the inflation of reused views from within the same context.

##### Navigation Graphs
> Navigation graphs are used for flow controls between activities and fragments. There are two navigation graphs: navigation_main and navigation_game.

##### Drawables
> A large number of drawables were used for the graphics of views and other layouts and containers.

<br>

### Successes and Failures

##### Successes
> The original goal was to create an application which would be reminiscent of the original Battleship. This has been executed as proposed.
>
> There have been more graphical overhauls which were not originally intended. App functionality is as intended, and device layout modularity is within acceptable range.

##### Failures
> Unfortunately, fragment optimization performance is low. This is due to the large number of images and views which have to be created for the grids. There are also some lackluster layout designs in some pages.
>
> There were some changes to the used categories as well. This was due to unnecessary features which were originally proposed due to problematic time constraints. They have been replaced by more appropriate categories, however.

<br>

<h2 style="color: ghostwhite">Usage and License Limitations</h2>

<p style="color: indianred"><code>LICENSE WARNING:</code> Please read and understand the included <a href="https://github.com/asteph11/SoftwareEngineeringProject/blob/main/LICENSE.md">GPL License</a> before attempting to use this code.</p>
<p>This strict copyleft license is in place due to the wholesale cheating that goes on at SUNY College at Old Westbury. Please understand the License before you get yourself into trouble.</p>
<p><b>Do NOT</b> use any of this project's content <em>(structure /or source code /or libraries /or assets /or etc.)</em>, regardless of external modification, without citing Copyright where such content is used.</p>
<p><b>DO</b> contact <a href="mailto:asteph11@oldwestbury.edu">Andrew Stephens</a> for inquiries.</p>

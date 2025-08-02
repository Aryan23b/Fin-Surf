🎮 FinSurf – A Fish Surfing Game
FinSurf is a fun and lightweight Android game where a fish surfs through the water, collecting points and dodging hazards! Built entirely using Jetpack Compose and Material 3, this game demonstrates a smooth UI, state management, score tracking, and an intuitive navigation flow.

📱 Demo / Screenshots
<div align="center">
  <img src="https://github.com/user-attachments/assets/c2b58185-a8d7-44b8-b3de-81853c533c68" width="250" style="margin: 10px;" />
  <img src="https://github.com/user-attachments/assets/17172f15-06e2-412d-b68a-f3dca6a0b361" width="250" style="margin: 10px;" />
  <img src="https://github.com/user-attachments/assets/9590b79b-5787-409d-a93b-35aae2718186" width="250" style="margin: 10px;" />
</div>



✨ Features
Simple Controls: Intuitively guide the fish through the water with simple tap or drag gestures.

Dynamic Scoring:

🟢 Green Dots: +25 points

🟡 Yellow Dots: +10 points

🔴 Red Dots: -20 points

Life System: Start with 3 lives. Colliding with a red dot costs one life. The game ends when all lives are lost.

Persistent High Score: The app saves your highest score locally, giving you a record to beat every time you play.

Seamless Navigation: On game over, a Khel Khatam! toast message appears, and the app automatically navigates to a score screen showing your final score alongside the high score.

Play Again: A "Play Again" button on the score screen lets you jump right back into a new game.

🧱 Tech Stack

| Tech           | Description                              |
|----------------|------------------------------------------|
| Kotlin         | Primary language                         |
| Jetpack Compose| Modern UI toolkit for building the UI   |
| Material 3     | UI styling and components                |
| Navigation     | Compose Navigation for screen transitions |
| ViewModel + State| To manage lives, score, and game logic |
| SharedPreferences (or DataStore)| To persist High Score locally |


🚀 Getting Started
To get a local copy up and running, follow these simple steps.

Clone the repository:

Bash

git clone https://github.com/Aryan23b/Fin-Surf
Open in Android Studio:
Open the cloned project folder in the latest version of Android Studio.

Build and Run:
Let Gradle sync the dependencies, then build and run the app on an emulator or a physical Android device.

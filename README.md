# Thai Checkers Floating Board (prototype)

Android Studio starter project for a floating Thai-checkers board overlay.

## Included
- Android overlay permission flow.
- Floating 8x8 board.
- Tap-to-place pieces (empty -> red -> black) in setup mode.
- Manual move mode and AI suggestion button.
- Basic prototype move selector.

## Important limitations
- This is source code, not a prebuilt APK.
- The move selector is a basic heuristic, not a strong search engine.
- Thai checkers rules and king movement differ by ruleset; verify and replace `CheckersAI.kt` before relying on it.
- Board placement is manual; it does not read the underlying game's screen.
- Requires Android Studio to build an APK. On the phone, enable “Display over other apps” for this app.

## Build
Open this folder in Android Studio, allow Gradle sync, then Build > Build APK(s).

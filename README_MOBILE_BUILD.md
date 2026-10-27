# Build APK using only an Android phone (GitHub Actions)

This is still source code, not an APK. The included GitHub Actions workflow can build a debug APK in GitHub's cloud.

1. In your browser, sign in to GitHub and create a **new public or private repository** named `ThaiCheckersFloating`.
2. Upload the contents of this ZIP to the repository root. Make sure `.github/workflows/build-apk.yml` is uploaded too. On mobile GitHub, the easiest method may be to upload files via the repository's **Add file → Upload files** page; hidden dot-folders may be awkward, so verify that `.github/workflows/build-apk.yml` exists. If GitHub's web upload won't upload the workflow folder, use GitHub's web editor to create `.github/workflows/build-apk.yml` and paste its contents from this file.
3. Open the repository's **Actions** tab, select **Build Thai Checkers APK**, and tap **Run workflow**. If prompted, choose `main`.
4. Wait for the run to finish with a green check mark. Open the run and download the artifact named `ThaiCheckersFloating-debug-apk`.
5. Extract the downloaded artifact ZIP. It contains `app-debug.apk`. Tap it to install. Android may ask you to allow installation from that browser/files app.
6. Open the app, enable **Display over other apps**, then return and tap **เปิดกระดานลอย**.

Notes:
- GitHub may require email verification or account sign-in. Do not put passwords or secrets in the repository.
- This workflow builds the debug APK; it is not a Play Store release.
- The app is a prototype: the AI is basic and Thai-checkers rules/king logic need validation. Test in a private/offline game first. It does not read the game screen automatically.

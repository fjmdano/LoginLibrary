# Login Library
For creating your custom log-in component

### To use this library in an app:

1. Create instance of LoginHelper
```
var loginHelper = LoginHelper(applicationContext, APP_NAME, width, height)
```
2. Initialize third party libraries that will be used for logging in. Details can be retrieved in Facebook for Developers site or from google-services.json
```
loginHelper.useFacebook(APP_ID, PROTOCOL_SCHEME)
loginHelper.useFirebase(PROJECT_NUMBER, FIREBASE_URL, PROJECT_ID, STORAGE_BUCKET, APP_ID, API_KEY, CLIENT_ID)
```
3. Customize log-in UI
* `loginHelper.setPadding(LEFT, TOP, RIGHT, BOTTOM)`
* `loginHelper.setMargins(LEFT, TOP, RIGHT, BOTTOM)`
* `loginHelper.setBackground(BACKGROUND)`
* `loginHelper.setStyle(STYLE)`

4. Add items to log-in UI
Added items are displayed in stack i.e. first entered, first displayed.
The following items can be created, with customization similar to #3 (i.e. setPadding(), setMargins(), setBackground(), setStyle())
* `loginHelper.addImage(IMAGE, WIDTH, HEIGTH)`
* `loginHelper.addText(LABEL, WIDTH, HEIGTH)`
* `loginHelper.addInput(HINT, IS_PASSWORD, INPUT_TYPE,
                 WIDTH, HEIGTH, KEY)`
* `loginHelper.addLoginButton(LABEL, WIDTH, HEIGTH)`
* `loginHelper.addButton(LABEL, object: LoginHelper.CustomListener, WIDTH, HEIGTH)`
    * Button has custom onclick listener e.g.
    ```
    object: LoginHelper.CustomListener {
                    override fun onClick() {
                        // Do custom sequence i.e. startActivity(INTENT)
                    }
                }
    ```
* `loginHelper.addForgotPassword(LABEL, IMAGE, HEADER, SUBHEADER, INPUT_HINT, BUTTON_LABEL)`
    * IMAGE can be null
    * Elements in Forgot Password can be customized:
        * `forgotPassword.button` : button displayed in login page
        * `forgotPassword.image` : image in Forgot Password UI, displayed at the top of the page
        * `forgotPassword.header` : header in Forgot Password UI, dsplayed below the image
        * `forgotPassword.subheader` : subheader in Forgot Password UI, displayed below the header
        * `forgotPassword.inputField` : email inputField in Forgot Password UI, displayed below the subheader
        * `forgotPassword.button` : submit button in Forgot Password UI, displayed below the inputField
* `loginHelper.addGoogleSignIn(WIDTH, HEIGTH)`
* `loginHelper.addFacebookSignIn(WIDTH, HEIGTH)`


5. [Optional] For custom sign-in function:
```
loginHelper.setCustomSigninHandler(object: LoginHelper.CustomLoginHandler {
            override fun login(): Any? {
                // Do custom sequence i.e. http calls
                // return user details so that it can be accessed in the future from loginHelper.getUser()
            }
        })
```
6. In case there is no logged in user, display created login UI
```
loginIntent = loginHelper.getIntent(this)
startActivity(loginIntent)
```

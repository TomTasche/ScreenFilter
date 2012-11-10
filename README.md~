ScreenFilter
============

Attempt to mimic the screenfilter by 
https://play.google.com/store/apps/details?id=com.haxor&amp;hl=en.
Developer site: 

Because I think it's the most essential apps with simple user interaction. 
Thus is good as a learning project.

The design/idea used in this project is mostly directly from the apps mentioned above.

Some behaviors of the ScreenFilter apps:
- When started, it doesn't appear on the "Recent App" list. 
  > This is done by declaring the acitivity with:
      android:excludeFromRecents="true"
  
- Show 1 process and 1 service 

- It continue to function even in lock screen

- Should not be killed
  > This is done by calling startForeground
  > Example of startForeground: https://github.com/commonsguy/cw-android/tree/master/Notifications/FakePlayer
  
  
TODO:
- Implement functions related to color changed
- Add color selection to preference, and pass it to toastView

2012/11/08:
- Added Color selection dialog from ApiDemo (need touch up)

Closed: 
- The button to reenable filter after setting change doesn't work, even though the ReenableListener is registered.
  > Cause: The setContentView(preferences) was called twice. Once after the button listener is set.The listeners should be set after layout applied. 



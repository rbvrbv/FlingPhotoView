# FlingPhotoView
Extends PhotoView by Chris Banes. Added fling-to-close image processing with various animate effects and event handlers

[![Bintray](https://img.shields.io/bintray/v/rbv/libs/flingphotoview.svg?maxAge=3600)](https://bintray.com/rbv/libs/flingphotoview)

## Dependency

Add this in your root build.gradle file

``````````````
repositories {
    google()
    jcenter()
}

dependencies {
    implementation 'su.rbv:flingphotoview:1.0.0'
}  

``````````````

## Features

**Effects after fling gesture:**
* Flying to top
* Dropping to bottom
* Return to initial size and position

**Handle events:**
* onFlingDragStart - At start flinging picture
* onFlingDrag - During flinging on change vertical position
* onFlingReturnStart - At the release moment before the picture movement, if the vertical offset is less than the threshold and picture should return back
* onFlingReturnFinish - After the picture returned back
* onFlingDoneStart - At the  release moment before the picture movement, if the vertical offset is greather than the threshold and picture should fly out
* onFlingDoneFinish - after the picture flew out

**XML attributes:**
* fling_type = "top|bottom|rectangle" - how the picture should behave after fling gesture (default: top)
* end_scale = "[float]" - scale ration of the picture at the finish of the movement, for "top" or "bottom" fling_type (default: 1)
* return_duration = "[milliseconds]" - picture return duration (default: 100ms)
* done_duration = "[milliseconds]" - picture movement duration after succesfull flinging (default: 300ms)
* start_deltaY_threshold = "[dimension]" - Y offset for starting picture dragging (default: 13dp)
* done_deltaY_threshold = "[dimension]" - threshold of succesfull fling offset (default: 40dp)
* need_background = "[boolean]" - is background fading necessary during image movement (default: true)
* background = "[reference]" - reference to view object for fading (usually background). If it not defined, uses parent object

## Demo

![](demo/demo.gif)

See [Example](https://github.com/rbvrbv/FlingPhotoView/tree/master/sample) for all of that.


## License

````````````````````
Copyright 2019 Boris Ryaposov

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
````````````````````````````

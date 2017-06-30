# User Tutorial

![Example](https://github.com/ringcentraljerry/UserTutorial/blob/master/tip_example.png)

## Getting started

* Your target activity should be extended from one of activities which are TutorialActivity, TutorialFragmentActivity, and TutorialListActivity. Please make sure every activity in your project should base on one of these three activities if it wants to support this feature. 

* Your target view should be extended from one of views which are TutorialButton, TutorialImageButton, TutorialImageView, TutorialLinearLayout, TutorialRelativeLayout. Please make sure the target view in the layout should base on one of these views, and their id should be unique in the activity.

* Notice, don't override onKeyDown() or onKeyUp() to handle KEY_BACK or KEY_MENU events, you can just override onBackPressed() or onKeyDownOpenMenu() instead.

* In your project you need to add Assets/tutorial_tips.xml. 
```
id:             (Type Integer) the value is number order
screen:         (Type String) the value is activity's name
viewId:         (Type String) the target view id name
text:           (Type String) the resource id of text, which will be placed in the tip.
brand:          (Type String Array) when [] is empty value, which means no brand limitation.
permission:     (Type String Array) when [] is empty value, which means no permission limitation.
hotPadding:     (Type String) the resource id of dimension, the padding value of hotspot.
hotZoomIn:      (Type Boolean) true means hotspot is bigger than target view' s height.
eventName:      (Type String) the value is using for Flurry, Event ID.
```
* Take an example, all what you need to do is:

```
{
"tips" : [
{
"id" : "1",
"screen" : "target screen",
"viewId" : "target view id", //res
"text" : "tip string id", //res
"brand": [],
"permission": [],
"hotPadding":"tutorial_tip_hot_padding_0dp",
"hotZoomIn": false,
"eventName":""
}
]
}
```

## License <a name="license"></a>

Copyright (C) 2017 RingCentral

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

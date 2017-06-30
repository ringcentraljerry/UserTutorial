# Tutorial

The main components of User Tutorial Framework:

## Activity:

TutorialActivity
TutorialFragmentActivity
TutorialListActivity
Please make sure every activity in our project should base on one of these three activities if it wants to support this feature. 

## Target View:

TutorialButton
TutorialImageButton
TutorialImageView
TutorialLinearLayout
TutorialRelativeLayout
Please make sure the target view in the layout should base on one of these views, and their id should be unique in a activity.

Attention: Don't override onKeyDown() or onKeyUp() to handle KEY_BACK or KEY_MENU events, override onBackPressed() or onKeyDownOpenMenu()  instead.

##  Assets/tutorial_tips.xml:

id:                         (Type Integer) the value is number order
screen:                 (Type String) the value is activity's name
viewId:                  (Type String) the target view id name
text:                      (Type String) the resource id of text, which will be placed in the tip.
brand:                   (Type String Array) when [] is empty value, which means no brand limitation.
permission:           (Type String Array) when [] is empty value, which means no permission limitation.
hotPadding:          (Type String) the resource id of dimension, the padding value of hotspot.
hotZoomIn:           (Type Boolean) true means hotspot is bigger than target view' s height.
eventName:          (Type String) the value is using for Flurry, Event ID.
Take an example, all what you need to do is:

Firstly, the target activity should be base on one of the activities list above;

Secondly, the target view which needs show a tip, it should base on one of the target views list above, and its id should be like this "xxx_view_id";

Thirdly, add the key-value of "tip_for_profile_layout", "tutorial_tip_hot_padding_0dp" into "res/string_tutorial.xml" or "res/dimens_tutorial.xml";

Finally, we need to add the tip info into the Json file:

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

Ok, everything is done!

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

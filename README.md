# progress_circle
Progress Circle View for Andoid

<img src="" width="150 height="150"/>
<img src="" width="150 height="150"/>
<img src="" width="150 height="150"/>
<img src="" width="150 height="150"/>

##Instruction

Add 'ProgressCircle' view to your layout
```xml
<com.tomergoldst.android.views.progresscircle.ProgressCircle
        android:id="@+id/circle_progress"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="32dp"
        app:progressColor="@color/colorPrimary"
        app:textColor="@color/colorPrimary"
        app:outlineWidth="16dp"
        app:ProgressWidth="10dp"/>
```

Adjust attributes to fit your needs
```xml
<declare-styleable name="ProgressCircle">
        <attr name="textColor" />
        <attr name="progressColor" />
        <attr name="ProgressWidth" />
        <attr name="outlineColor" />
        <attr name="outlineWidth" />
        <attr name="maxProgressValue" />
        <attr name="maxAnimationDuration" />
        <attr name="minAnimationDuration" />
    </declare-styleable>
```

Set progress from code
```java
mCircleProgress = (ProgressCircle) findViewById(R.id.circle_progress);
mCircleProgress.setProgress(90);
```


See the demo app for more information

### License
```
Copyright 2016 Tomer Goldstein

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```  


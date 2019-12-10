# progress circle
Progress Circle View for Andoid

<img src="https://cloud.githubusercontent.com/assets/19874536/16689892/c2d785ee-452d-11e6-8828-2ee197c931ef.gif" width="150" height="250"/><img src="https://cloud.githubusercontent.com/assets/19874536/16689732/02e8a902-452d-11e6-9fbf-ea4d0095f208.png" width="150" height="250"/><img src="https://cloud.githubusercontent.com/assets/19874536/16689731/02e55e64-452d-11e6-90e8-f7887625fb2b.png" width="150" height="250"/><img src="https://cloud.githubusercontent.com/assets/19874536/16689730/02e3996c-452d-11e6-802e-bbe4eb8d596e.png" width="150" height="250"/>

## Instruction

Add a dependency to your app build.gradle
```groovy
dependencies {
    compile 'com.tomergoldst.android:progress-circle:1.0.2'
}
```

Add 'ProgressCircle' view to your layout
```xml
<com.tomergoldst.progress_circle.ProgressCircle
        android:id="@+id/circle_progress"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:progressColor="?attr/colorPrimary"
        app:textColor="?attr/colorPrimary"
        app:outlineWidth="16dp"
        app:ProgressWidth="10dp"/>
```

Adjust attributes to fit your needs, below is the list of attributes available
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


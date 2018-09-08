package com.weiwobang.paotui.JPush;

import android.os.Bundle;

public  interface  JpushContract {
    void doProcessPushMessage(Bundle bundle);

    void doProcessPusNotify(Bundle bundle);

    void doOpenPusNotify(Bundle bundle);
}

package com.yangtzeu.ui.view;

import com.yangtzeu.ui.fragment.ChatFragment1;
import com.yangtzeu.ui.fragment.ChatFragment2;
import com.yangtzeu.ui.fragment.ChatFragment3;

public interface ChatView {
    ChatFragment1 getFragmentPart1();

    ChatFragment2 getFragmentPart2();

    ChatFragment3 getFragmentPart3();

}

package com.yangtzeu.ui.view;

import com.yangtzeu.ui.adapter.VoaListAdapter;

public interface VoaListView {

    String getFromUrlHeader();

    int getPage();

    boolean is_archiver();

    VoaListAdapter getAdapter();
}

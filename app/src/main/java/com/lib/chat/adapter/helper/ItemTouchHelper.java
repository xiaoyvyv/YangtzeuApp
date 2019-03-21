package com.lib.chat.adapter.helper;

public interface ItemTouchHelper {
    //数据删除
    void onItemMiss(int position, String userType, String chat_id);

}

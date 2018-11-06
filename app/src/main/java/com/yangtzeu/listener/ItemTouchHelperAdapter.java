package com.yangtzeu.listener;


public interface ItemTouchHelperAdapter {
    //数据交换
    void onItemMove(int fromPosition,int toPosition);
    //数据删除
    void onItemMiss(int position);
}
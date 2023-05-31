package com.tss;

import com.tss.entities.data.TaskList;

import java.util.Comparator;

public class OrderComparator implements Comparator<TaskList> {
    @Override
    public int compare(TaskList list1, TaskList list2) {
        int list1Order = list1.getList_order();
        int list2Order = list2.getList_order();

        return Integer.compare(list1Order, list2Order);
    }
}

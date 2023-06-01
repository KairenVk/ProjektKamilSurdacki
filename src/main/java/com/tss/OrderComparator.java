package com.tss;

import com.tss.entities.data.TaskList;

import java.util.Comparator;

public class OrderComparator implements Comparator<TaskList> {
    @Override
    public int compare(TaskList list1, TaskList list2) {
        int list1Order = list1.getListOrder();
        int list2Order = list2.getListOrder();

        return Integer.compare(list1Order, list2Order);
    }
}

package com.nhaarman.listviewanimations.itemmanipulation;

// TODO integrate in ExpandableListItemAdapter
public interface ExpandCollapseListener {

    void onItemExpanded(int position);

    void onItemCollapsed(int position);

}

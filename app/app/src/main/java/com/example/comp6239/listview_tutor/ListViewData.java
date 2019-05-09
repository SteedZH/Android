package com.example.comp6239.listview_tutor;

public class ListViewData {
    private String list_name;
    private String start_time;
    private String end_time;
    private String grid_name;

    public ListViewData(String list_name, String start_time, String end_time){
        this.list_name = list_name;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public ListViewData(String grid_name)
    {
        this.grid_name = grid_name;
    }

    public String getGrid_name() {
        return grid_name;
    }

    public String getList_name(){
        return list_name;
    }

    public String getStart_time(){
        return start_time;
    }
    public String getEnd_time(){
        return end_time;
    }
}

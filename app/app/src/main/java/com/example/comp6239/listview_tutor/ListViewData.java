package com.example.comp6239.utility;

public class ListViewData {
    private String list_name;
    private String list_time;
    private String grid_name;

    public ListViewData(String list_name, String list_time){
        this.list_name = list_name;
        this.list_time = list_time;
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

    public String getList_time(){
        return list_time;
    }
}

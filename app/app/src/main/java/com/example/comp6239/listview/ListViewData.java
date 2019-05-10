package com.example.comp6239.listview;

import android.widget.ListView;

public class ListViewData {
    private String arg1;
    private String arg2;
    private String arg3;
    private String arg4;
    private String arg5;
    private String arg6;
    private String arg7;

    public ListViewData(String arg1, String arg2, String arg3) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
    }

    public ListViewData(String arg1, String arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public ListViewData(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public String getArg3() {
        return arg3;
    }

    public String getArg4() {
        return arg4;
    }

    public String getArg5() {
        return arg5;
    }

    public String getArg6() {
        return arg6;
    }

    public String getArg7() {
        return arg7;
    }
}

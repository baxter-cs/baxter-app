package org.baxter_academy.flex;

/**
 * @author wilstenholme
 * @since 2/2/16
 */
public class Constants {
    public static final String app_name = "Flex";

    public static String title_todo = "To Do";
    public static String title_doing = "In Process";
    public static String title_done = "Done";

    public static String task_title_bg = "#F8BBD0";
    public static String task_text_bg = "#FAD2E0";
    public static String task_titleCol = "#515151";
    public static String task_textCol = "#4E4E4E";

    public static String server_address_upgradeStatus = "http://192.168.0.13:8000/upgradeTask";
    public static String server_address_newTask = "http://192.168.0.13:8000/newTask";
    public static String server_address_getTasks = "http://192.168.0.13:8000/getTasks";
    public static String server_address_deleteTask = "http://192.168.0.13:8000/deleteTask";
}

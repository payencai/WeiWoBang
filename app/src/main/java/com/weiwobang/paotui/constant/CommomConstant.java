package com.weiwobang.paotui.constant;

import android.os.Environment;

public class CommomConstant {
    public final static String DIR_PACKAGE = "com.yichan.libpro";
    public  static class Runner{
         public  static  String account="13249183950";
        public static  String password="88888888";
    }
    public static  class Seller{
        public  static  String account="15069187173";
        public static  String password="88888888";
    }
    public  class Car{
        public static final String TYPE_STR_MINI_BUS = "小面包车";
        public static final String TYPE_STR_MIDDLE_BUS = "中面包车";
        public static final String TYPE_STR_SMALL_TRACK = "小货车";
        public static final String TYPE_STR_MIDDLE_TRACK = "中货车";
        public static final String TYPE_STR_MOTORBIKE = "摩托车";
        public static final String TYPE_STR_SEDAN = "小轿车";
        public static final String TYPE_STR_TRICYCLE = "三轮车";
    }

    public class FilePath {
        public final static String DIR_ROOT = "weiwobang/";        // 根目录
        public final static String DIR_IMG_PART="partimg/";// 电子图册文件夹路径


    }
    /**
     * 文件位置
     *
     * @ClassName: FileLocation
     * @Description:
     */
    public final static class FileLocation {
        public final static String FILE_SAVE_PATH = "/data/data/" + DIR_PACKAGE + "/filedr/image";// 内存中图片存放的位置
        public final static String ESD = Environment.getExternalStorageDirectory().toString();// 根目录
    }
    public enum TASK_STATE{

        UNSIGNED (1), SIGNED (2), PROCESS_PICKUP(3), PROCESS_DELIVERY(4); //未取货，已取货，已送货

        private final int value;

        public int getValue() {
            return value;
        }

        TASK_STATE(int value){
            this.value = value;
        }
    }
}

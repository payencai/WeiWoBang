//
// Created by Administrator on 2018/9/27 0027.
//
#include <stdio.h>
#include "com_weiwobang_paotui_jnitools_JniTools.h"

int add(int x,int y);
JNIEXPORT jint JNICALL Java_com_weiwobang_paotui_jnitools_JniTools_Add
  (JNIEnv * env, jclass cls, jint x, jint y){
        return add(x,y);
  }
int add(int x,int y){
   return x+y;
}
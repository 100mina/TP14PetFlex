package com.m0103.tp14petflex;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class G {
    public static int login=0;
    public static String nickname=null;

    //랭킹 불러올 날짜(전달 1~말일) 구하기
    public static LocalDate last= LocalDate.now().minusMonths(1); //저번달
    public static LocalDate start= last.with(TemporalAdjusters.firstDayOfMonth()); //시작일
    public static LocalDate end= last.with(TemporalAdjusters.lastDayOfMonth()); //마지막일
    public static int lastMonth= LocalDate.now().minusMonths(1).getMonthValue();

}

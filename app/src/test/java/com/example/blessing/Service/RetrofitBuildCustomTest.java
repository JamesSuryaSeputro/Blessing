package com.example.blessing.Service;

<<<<<<< HEAD
import org.junit.Test;

import static org.junit.Assert.assertEquals;
=======
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
>>>>>>> 73837329fd07894514e9194bf88f89881d1a8dff

public class RetrofitBuildCustomTest {

  @Test
  public void testsingleton() throws Exception {
    int a, b;
    RetrofitBuildCustom retrofitBuildCustom1 = RetrofitBuildCustom.getInstance();
    a = retrofitBuildCustom1.hashCode();
    RetrofitBuildCustom retrofitBuildCustom2 = RetrofitBuildCustom.getInstance();
    b = retrofitBuildCustom2.hashCode();
    System.out.print(a + " " + b);
    assertEquals(a, b);
//    System.out.println("Success JAMES ");

  }

  @Test
  public void testretrofithash() throws Exception {
    System.out.println(RetrofitBuildCustom.getInstance().getRetrofit().hashCode()+ " "+RetrofitBuildCustom.getInstance().getRetrofit().hashCode());
  }
  @Test
  public void testservicehash() throws Exception {
    System.out.println(RetrofitBuildCustom.getInstance().getService().hashCode()+ " "+RetrofitBuildCustom.getInstance().getService().hashCode());
  }
}
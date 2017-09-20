package com.github.wglanzer.annosave.impl.util;

import com.github.wglanzer.annosave.api.IType;
import org.junit.*;

/**
 * @author W.Glanzer, 20.09.2017
 */
public class Test_TypeFactory
{

  @Test
  public void test_string()
  {
    IType cs = TypeFactory.create(String.class.getName());
    Assert.assertNotNull(cs);
    Assert.assertEquals("java.lang.String", cs.getClassName());
    Assert.assertFalse(cs.isPrimitive());
    Assert.assertEquals(String.class, cs.getInstance());
  }

  @Test
  public void test_primitive()
  {
    IType cs = TypeFactory.create(int.class.getName());
    Assert.assertNotNull(cs);
    Assert.assertEquals("int", cs.getClassName());
    Assert.assertTrue(cs.isPrimitive());
    Assert.assertEquals(int.class, cs.getInstance());
  }

  @Test
  public void test_primitiveArray()
  {
    IType cs = TypeFactory.create(double[].class.getName());
    Assert.assertNotNull(cs);
    Assert.assertEquals("[D", cs.getClassName());
    Assert.assertFalse(cs.isPrimitive());
    Assert.assertEquals(double[].class, cs.getInstance());
  }

  @Test
  public void test_noSourcesAvailable()
  {
    IType cs = TypeFactory.create("my.super.class.Main");
    Assert.assertNotNull(cs);
    Assert.assertEquals("my.super.class.Main", cs.getClassName());
    Assert.assertFalse(cs.isPrimitive());
    Assert.assertEquals(null, cs.getInstance());
  }

}

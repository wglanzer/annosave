package com.github.wglanzer.annosave.api.testcontainer;

/**
 * @author W.Glanzer, 04.09.2017
 */
public @interface ObsoleteVersion
{

  int version();

  String pkgName() default "";

  String id() default "";

  Class<?> type() default Void.class;

  Class<?>[] parameters() default Void.class;

}

package com.github.wglanzer.annosave.api.testcontainer;

import com.github.wglanzer.annosave.processor.AnnoPersist;

/**
 * @author W.Glanzer, 04.09.2017
 */
@AnnoPersist
public @interface ObsoleteVersion
{

  int version();

  String pkgName() default "";

  String id() default "";

  Class<?> type() default Void.class;

  Class<?>[] parameters() default Void.class;

}

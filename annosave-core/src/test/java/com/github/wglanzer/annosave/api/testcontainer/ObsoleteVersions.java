package com.github.wglanzer.annosave.api.testcontainer;

import java.lang.annotation.*;

/**
 * @author W.Glanzer, 04.09.2017
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface ObsoleteVersions
{

  ObsoleteVersion[] value();

}

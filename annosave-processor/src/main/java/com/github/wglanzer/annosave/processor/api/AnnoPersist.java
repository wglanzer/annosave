package com.github.wglanzer.annosave.processor.api;

import java.lang.annotation.*;

/**
 * Annotation which marks an other annotation to be
 * automatically persisted by AnnoSaveProcessor
 *
 * @author W.Glanzer, 15.09.2017
 */
@Target(ElementType.ANNOTATION_TYPE)
public @interface AnnoPersist
{
}

package com.github.wglanzer.annosave.processor;

import com.google.auto.common.AnnotationMirrors;

import javax.lang.model.element.*;
import java.util.Map;
import java.util.function.Predicate;

/**
 * This filter checks, if an annotation should be automatically persisted.
 *
 * @author W.Glanzer, 15.09.2017
 */
class PersistenceFilter implements Predicate<AnnotationMirror>
{

  @Override
  public boolean test(AnnotationMirror pAnnotationMirror)
  {
    if(pAnnotationMirror.getAnnotationType().asElement().getAnnotation(AnnoPersist.class) != null)
    {
      for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : AnnotationMirrors.getAnnotationValuesWithDefaults(pAnnotationMirror).entrySet())
      {
        if (entry.getKey().getSimpleName().contentEquals("serialize"))
          return Boolean.TRUE.equals(entry.getValue().getValue());
      }

      // if no "serialize"-Method found which returns false => return true
      return true;
    }

    return false;
  }

}

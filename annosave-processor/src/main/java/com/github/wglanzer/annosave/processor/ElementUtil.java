package com.github.wglanzer.annosave.processor;

import javax.lang.model.element.Element;
import javax.lang.model.util.ElementFilter;
import java.util.Collections;

/**
 * @author W.Glanzer, 15.09.2017
 */
public class ElementUtil
{

  public static boolean isMethod(Element pElement)
  {
    return !ElementFilter.methodsIn(Collections.singleton(pElement)).isEmpty();
  }

  public static boolean isField(Element pElement)
  {
    return !ElementFilter.fieldsIn(Collections.singleton(pElement)).isEmpty();
  }

  public static boolean isConstructor(Element pElement)
  {
    return !ElementFilter.constructorsIn(Collections.singleton(pElement)).isEmpty();
  }

}

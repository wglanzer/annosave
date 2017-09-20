package com.github.wglanzer.annosave.processor;

import javax.lang.model.element.Element;
import javax.lang.model.type.*;
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

  public static boolean isArray(TypeMirror pType)
  {
    return pType instanceof ArrayType;
  }

  public static String getClassName(TypeMirror pType)
  {
    if(isArray(pType))
      return _getArrayClassName(((ArrayType) pType).getComponentType());
    return pType.toString().replaceAll("<[^>]*>", "");
  }

  private static String _getArrayClassName(TypeMirror componentType)
  {
    switch (componentType.getKind())
    {
      case BOOLEAN:
        return "[Z";
      case BYTE:
        return "[B";
      case SHORT:
        return "[S";
      case INT:
        return "[I";
      case LONG:
        return "[J";
      case CHAR:
        return "[C";
      case FLOAT:
        return "[F";
      case DOUBLE:
        return "[D";
      default:
        // must be an object non-array class
        return "[L" + getClassName(componentType) + ";";
    }
  }

}

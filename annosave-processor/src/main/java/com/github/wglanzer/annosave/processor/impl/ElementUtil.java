package com.github.wglanzer.annosave.processor.impl;

import org.jetbrains.annotations.Nullable;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.ElementFilter;
import java.lang.reflect.Method;
import java.util.Collections;

/**
 * @author W.Glanzer, 15.09.2017
 */
class ElementUtil
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

  public static boolean isType(Element pElement)
  {
    return !ElementFilter.typesIn(Collections.singleton(pElement)).isEmpty();
  }

  public static boolean isArray(TypeMirror pType)
  {
    return pType instanceof ArrayType;
  }

  public static String getClassName(TypeMirror pType)
  {
    if(isArray(pType))
      return _getArrayClassName(((ArrayType) pType).getComponentType());

    String defaultName = pType.toString().replaceAll("<[^>]*>", "");
    if(pType instanceof DeclaredType)
      return flatName(((DeclaredType) pType).asElement(), defaultName);
    return defaultName;
  }

  @Nullable
  public static String flatName(Element pElement, String pDefault)
  {
    try
    {
      Method meth = pElement.getClass().getDeclaredMethod("flatName");
      meth.setAccessible(true);
      return ((Name) meth.invoke(pElement)).toString();
    }
    catch(Exception e)
    {
      return pDefault;
    }
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

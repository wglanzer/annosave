package com.github.wglanzer.annosave.api;

import org.jetbrains.annotations.*;

import java.util.Arrays;

/**
 * Represents a single annotation-interface
 *
 * @author W.Glanzer, 13.09.2017
 */
public interface IAnnotation
{

  /**
   * @return Name of the annotation. Mainly the classname
   */
  @NotNull
  String getName();

  /**
   * @return Type of this annotation
   */
  @NotNull
  IType getType();

  /**
   * @return all available parameters/methods of the representing annotation
   */
  @NotNull
  IAnnotationParameter[] getParameters();

  /**
   * Returns the parameter with the given name or <tt>null</tt> if nothing found
   *
   * @param pName Name of the searched parameter
   * @return the parameter, or <tt>null</tt>
   */
  @Nullable
  default IAnnotationParameter findParameter(String pName)
  {
    try
    {
      return getParameter(pName);
    }
    catch(NullPointerException npe)
    {
      return null;
    }
  }

  /**
   * Returns the parameter with the given name. NullPointerException if nothing is found
   *
   * @param pName Name of the searched parameter
   * @return the parameter
   */
  @NotNull
  default IAnnotationParameter getParameter(String pName)
  {
    return Arrays.stream(getParameters())
        .filter(pParam -> pParam.getName().equals(pName))
        .findFirst().orElseThrow(NullPointerException::new);
  }

  /**
   * Returns the value of the parameter with the given name
   *
   * @param pParameterName Name of the parameter
   * @return the value, or <tt>null</tt> if no parameter is found or the value is null
   */
  @Nullable
  default Object getParameterValue(@NotNull String pParameterName)
  {
    IAnnotationParameter param = findParameter(pParameterName);
    if(param == null)
      return null;
    return param.getValue();
  }

  /**
   * Returns the value of the parameter with the given name
   *
   * @param pParameterName Name of the parameter
   * @param pType          Type of the parameter in which the result should be cast to
   * @return the value, or <tt>null</tt> if no parameter is found or the value is null
   */
  @Nullable
  default <T> T getParameterValue(@NotNull String pParameterName, @NotNull Class<T> pType)
  {
    Object value = getParameterValue(pParameterName);
    if(value == null)
      return null;

    if(pType.equals(String.class))
      //noinspection unchecked
      return (T) value.toString();

    return pType.cast(value);
  }

}

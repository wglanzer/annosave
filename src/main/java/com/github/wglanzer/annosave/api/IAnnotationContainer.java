package com.github.wglanzer.annosave.api;

/**
 * An AnnotationContainer is a container which contains annotations, cpt. obvious :)
 * It can be a Java-Class, -SubClass, -Method or -Field.
 *
 * @author W.Glanzer, 13.09.2017
 */
public interface IAnnotationContainer
{

  /**
   * @return Name of the container. Mainly the class-, method- or fieldname
   */
  String getName();

  /**
   * @return all annotations on the container
   */
  IAnnotation[] getAnnotations();

  /**
   * @return child-classes
   */
  IAnnotationContainer[] getChildren();

}

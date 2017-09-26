package com.github.wglanzer.annosave.processor.impl;

import com.github.wglanzer.annosave.api.AnnoSaveGZip;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.StandardLocation;
import java.io.File;
import java.net.URI;
import java.util.*;

/**
 * @author W.Glanzer, 15.09.2017
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("*")
public class AnnoSaveProcessor extends AbstractProcessor
{

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
  {
    if (roundEnv.processingOver())
      return false;

    List<Element> elements = new ArrayList<>();
    for (Element element : roundEnv.getRootElements())
    {
      boolean hasAny = element.getAnnotationMirrors().stream().anyMatch(new PersistenceFilter());
      if(hasAny)
        elements.add(element);
    }

    try
    {
      URI uri = processingEnv.getFiler().getResource(StandardLocation.CLASS_OUTPUT, "", "annosave.zip").toUri();
      AnnoSaveGZip.write(elements.toArray(new Element[elements.size()]), new AnnoSaveConverterImpl(), new File(uri));
    }
    catch (Exception pE)
    {
      pE.printStackTrace();
    }

    return false;
  }

}

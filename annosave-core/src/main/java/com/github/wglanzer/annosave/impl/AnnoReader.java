package com.github.wglanzer.annosave.impl;

import com.github.wglanzer.annosave.impl.structure.SAnnotationContainer;
import com.github.wglanzer.annosave.impl.util.GsonUtil;
import com.google.gson.Gson;

import java.io.*;

/**
 * Reads an IAnnotationContainer from JSON
 *
 * @author W.Glanzer, 13.09.2017
 */
public class AnnoReader
{

  private final InputStream stream;

  public AnnoReader(InputStream pStream)
  {
    stream = pStream;
  }

  public SAnnotationContainer read()
  {
    Gson gson = GsonUtil.createGson();
    return gson.fromJson(new InputStreamReader(stream), SAnnotationContainer.class);
  }

}

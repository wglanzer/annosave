package com.github.wglanzer.annosave.impl;

import com.github.wglanzer.annosave.impl.structure.SAnnotationContainer;
import com.github.wglanzer.annosave.impl.util.GsonUtil;
import com.google.gson.Gson;

import java.io.*;

/**
 * Writer for annotations
 *
 * @author W.Glanzer, 13.09.2017
 */
public class AnnoWriter
{

  private final OutputStream stream;

  public AnnoWriter(OutputStream pStream)
  {
    stream = pStream;
  }

  public void write(SAnnotationContainer pContainer)
  {
    Gson gson = GsonUtil.createGson();
    String jsonString = gson.toJson(pContainer);

    try(OutputStreamWriter writer = new OutputStreamWriter(stream))
    {
      writer.write(jsonString);
    }
    catch (IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

}

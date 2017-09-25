package com.github.wglanzer.annosave.impl;

import com.github.wglanzer.annosave.impl.structure.SAnnotationContainer;
import com.github.wglanzer.annosave.impl.util.GsonUtil;
import com.google.gson.*;

import java.io.*;
import java.util.List;

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

  public void write(List<SAnnotationContainer> pContainer)
  {
    Gson gson = GsonUtil.createGson();

    JsonArray topLevelArray = new JsonArray();
    pContainer.forEach(pCont -> topLevelArray.add(gson.toJsonTree(pCont)));
    String jsonString = gson.toJson(topLevelArray);

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

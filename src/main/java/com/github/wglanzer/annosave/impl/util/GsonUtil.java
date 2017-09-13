package com.github.wglanzer.annosave.impl.util;

import com.github.wglanzer.annosave.impl.structure.SAnnotationParameter;
import com.google.common.primitives.Primitives;
import com.google.gson.*;

/**
 * @author W.Glanzer, 13.09.2017
 */
public class GsonUtil
{

  /**
   * @return Creates the Gson-Object with all typeAdapters registered
   */
  public static Gson createGson()
  {
    GsonBuilder b = new GsonBuilder();

    // Type-Adapter for "Class"-Type
    b.registerTypeAdapter(Class.class, (JsonSerializer<Class>) (src, typeOfSrc, context) -> {
      JsonObject obj = new JsonObject();
      obj.add("name", context.serialize(Primitives.wrap(src).getName()));
      obj.add("primitive", context.serialize(src.isPrimitive()));
      return obj;
    }).registerTypeAdapter(Class.class, (JsonDeserializer<Class>) (src, typeOfSrc, context) -> {
      try
      {
        JsonObject obj = src.getAsJsonObject();
        String name = context.deserialize(obj.get("name"), String.class);
        Boolean primitive = context.deserialize(obj.get("primitive"), Boolean.class);
        Class<?> clazz = Class.forName(name);
        if(Boolean.TRUE.equals(primitive))
          clazz = Primitives.unwrap(clazz);
        return clazz;
      }
      catch (ClassNotFoundException pE)
      {
        pE.printStackTrace();
        return null;
      }
    });

    // Type-Adapter for "SAnnotationParameter"-Type because of additional "type"-Field
    b.registerTypeAdapter(SAnnotationParameter.class, (JsonSerializer<SAnnotationParameter>) (src, typeOfSrc, context) -> {
      JsonObject obj = new JsonObject();
      obj.add("name", context.serialize(src.getName()));
      obj.add("type", context.serialize(src.getType()));
      obj.add("valuetype", context.serialize(src.getValue().getClass()));
      obj.add("value", context.serialize(src.getValue()));
      return obj;
    }).registerTypeAdapter(SAnnotationParameter.class, (JsonDeserializer<SAnnotationParameter>) (src, typeOfSrc, context) -> {
      JsonObject obj = src.getAsJsonObject();
      SAnnotationParameter parameter = new SAnnotationParameter();
      parameter.setName(context.deserialize(obj.get("name"), String.class));
      parameter.setType(context.deserialize(obj.get("type"), Class.class));
      Class valuetype = context.deserialize(obj.get("valuetype"), Class.class);
      parameter.setValue(context.deserialize(obj.get("value"), valuetype));
      return parameter;
    });

    return b.create();
  }

}

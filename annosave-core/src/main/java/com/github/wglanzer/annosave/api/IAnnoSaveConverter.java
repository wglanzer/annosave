package com.github.wglanzer.annosave.api;

import java.util.List;

/**
 * @author W.Glanzer, 15.09.2017
 */
public interface IAnnoSaveConverter<Root>
{

  List<IAnnotationContainer> createContainer(Root pRoot);

}

package com.github.wglanzer.annosave.api;

/**
 * @author W.Glanzer, 15.09.2017
 */
public interface IAnnoSaveConverter<Root>
{

  IAnnotationContainer createContainer(Root pRoot);

}

package de.phyrone.voicechatapp.server

import com.google.auto.service.AutoService
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.internal.MainDispatcherFactory

@OptIn(InternalCoroutinesApi::class)
@AutoService(MainDispatcherFactory::class)
class MainLoopFactory : MainDispatcherFactory {
  override val loadPriority: Int = 0

  override fun createDispatcher(
      allFactories: List<MainDispatcherFactory>,
  ): MainCoroutineDispatcher = MainLoop
}
